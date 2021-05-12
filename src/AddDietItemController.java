import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

enum Food{
    Bread,
    Rice,
    Pasta,
    Lasagne,
    Biscuit,
    Potatoes,
    Orange,
    FrostFlakes,
    Vegetables,
    Chicken_kurry,
    sausages
}
enum Drink{
    Coke,
    Fanta,
    Malt,
    Water,
    Juice,
    Pepsi,
    Vimto,
    Alcohol,
    Red_Bull,
    Tea,
    Coffee,

}
enum MealType{
    English_breakfast,
    Mac_and_cheese_with_chicken,
    Roast_potatoes_and_chicken,
    Sandwich_and_drink,
    Roasted_lamb,
    fish_and_chips

}

public class AddDietItemController extends BaseController{

    @FXML ComboBox<String> setItemType;
    @FXML ComboBox<String> setItemName;
    @FXML TextField setCalorieCount;
    @FXML Label errorMessage;
    Meal selectedMeal;
    String itemname="nothing";
    ArrayList foodCustomItems = new ArrayList();
    ArrayList drinkCustomItems = new ArrayList();
    ArrayList mealCustomItems = new ArrayList();


    public void initialize(){
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        selectedMeal = DietPageController.selectedMeal;
    }

    public void selectItemType() {
        if(setItemType.getItems().isEmpty()) {
            setItemType.getItems().addAll("Food", "Drink", "Meal");
        }
        if(setItemType.getValue().contentEquals("Food")){
            listFood();

        }else if(setItemType.getValue().contentEquals("Drink")){
            listDrink();
        }else if(setItemType.getValue().contentEquals("Meal")){
            listMeal();
        }
    }
    public void listFood(){
       setItemName.getItems().clear();
            for (Food food : Food.values()) {
                setItemName.getItems().add(food.name());
            }
            for(int i=0; i< foodCustomItems.size(); i++) {
                setItemName.getItems().add((String) foodCustomItems.get(i));
            }
    }
    public void listDrink(){
        setItemName.getItems().clear();
        for (Drink drink : Drink.values()) {
            setItemName.getItems().add(drink.name());
        }
        for(int i=0; i< drinkCustomItems.size(); i++) {
            setItemName.getItems().add((String) drinkCustomItems.get(i));
        }
    }

    public void listMeal(){
        setItemName.getItems().clear();
        for (MealType meal : MealType.values()) {
            setItemName.getItems().add(meal.name());
        }
        for(int i=0; i< mealCustomItems.size(); i++) {
            setItemName.getItems().add((String) mealCustomItems.get(i));
        }
    }

    public void addCustomerItem(){
        String newItem = "nothing";
        String[] options = {"Food", "Drink", "Meal"};
       /* int optionChosen = (int) JOptionPane.showInputDialog(null, "Add Custom item to one of the following",
                "Adding custom item", JOptionPane.QUESTION_MESSAGE, icon, options, options[0]);*/
         int optionChosen= JOptionPane.showOptionDialog(null, "Add custom items to one of the following:",
                "Adding custom items",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch(optionChosen){
            case 0 :
                 newItem = JOptionPane.showInputDialog("Type custom food item");
                foodCustomItems.add(newItem);
                break;
            case 1:
                 newItem = JOptionPane.showInputDialog("Type custom drink item");
                drinkCustomItems.add(newItem);
                 break;
            case 2:
                newItem = JOptionPane.showInputDialog("Type custom meal item");
                mealCustomItems.add(newItem);
                break;

        }


    }

    public Meal addMeal(){

        Meal newMeal = new Meal();

        try {
            String SQL_QUERY = "INSERT INTO meal (timeconsumed, userid) VALUES (?, ?)";
            Connection connection = DBsession.INSTANCE.OpenConnection();
            PreparedStatement pst = connection.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
            pst.setTime(1, Time.valueOf(newMeal.getTimeConsumed()));
            pst.setInt(2, User.INSTANCE.getUserid());
            pst.executeUpdate();

            ResultSet keys = pst.getGeneratedKeys();
            keys.next();
            int newKey = keys.getInt(1);
            newMeal.setMealid(newKey);

            DBsession.INSTANCE.OpenConnection().close();

            //add meal to user's daily activity
            User.INSTANCE.dailyActivity.addMeal(newMeal);
        }
        catch (Exception e){
            System.out.println("error- meal could not be added.");
        }

        return newMeal;
    }

    public void addDietItem(javafx.event.ActionEvent actionEvent){

        if (selectedMeal == null) {
            selectedMeal = addMeal();
        }

        DietItem itemToAdd;
        DietItem.Type itemType= null;

        try{
            if (setItemType.getValue().equals("Food")) {
                itemType = DietItem.Type.FOOD;
            }
            else  if (setItemType.getValue().equals("Drink")) {
                itemType = DietItem.Type.DRINK;
            }
            else  if (setItemType.getValue().equals("Meal"))  {
                itemType = DietItem.Type.MEAL;
            }


            itemToAdd = new DietItem(itemname, Integer.parseInt(setCalorieCount.getText()), itemType);
            selectedMeal.addDietItem(itemToAdd);

            try{
                String SQL_QUERY = "INSERT INTO dietitem (itemtype, itemname, caloriecount) VALUES (?, ?, ?)";
                Connection connection = DBsession.INSTANCE.OpenConnection();
                PreparedStatement pst = connection.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, itemType.toString().toLowerCase());
                pst.setString(2, setItemName.getValue());
                pst.setInt(3, Integer.parseInt(setCalorieCount.getText()));
                pst.executeUpdate();

                ResultSet keys = pst.getGeneratedKeys();
                keys.next();
                int newKey = keys.getInt(1);

                pst.close();

                SQL_QUERY = "INSERT INTO mealitem (mealid, itemid) VALUES (?, ?)";
                pst = connection.prepareStatement(SQL_QUERY);
                pst.setInt(1, selectedMeal.getMealid());
                pst.setInt(2, newKey);
                pst.executeUpdate();

                pst.close();

                DBsession.INSTANCE.OpenConnection().close();
            }
            catch (Exception e){
                System.out.println("error inserting into database");
            }

            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/DietPage.fxml")));
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            //reset current selected meal
            DietPageController.selectedMeal = null;
        }
        catch (Exception e){
            errorMessage.setText("Item cannot be added - some values may be empty.");
        }
    }
}
