import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Objects;

public class AddDietItemController extends BaseController{

    @FXML ComboBox<String> setItemType;
    @FXML TextField setItemName;
    @FXML TextField setCalorieCount;
    @FXML Label errorMessage;
    Meal selectedMeal;

    public void initialize(){
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        selectedMeal = DietPageController.selectedMeal;
    }

    public void selectItemType() {
        setItemType.getItems().addAll("Food", "Drink");
    }

    public Meal addMeal(){

        Meal newMeal = new Meal();

        try {
            /*String SQL_QUERY = "INSERT INTO meal (timeconsumed, userid) VALUES ('?', ?)";
            Connection connection = DBsession.INSTANCE.OpenConnection();
            PreparedStatement pst = connection.prepareStatement(SQL_QUERY);
            pst.setTime(1, Time.valueOf(newMeal.getTimeConsumed()));
            pst.setInt(2, User.INSTANCE.getUserid());
            pst.executeQuery();

            ResultSet generatedKey = pst.getGeneratedKeys();
            newMeal.setMealid(generatedKey.getInt(1));*/

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
        DietItem.Type itemType;

        try{
            if (setItemType.getValue().equals("Food")) {
                itemType = DietItem.Type.FOOD;
            }
            else {
                itemType = DietItem.Type.DRINK;
            }

            itemToAdd = new DietItem(setItemName.getText(), Integer.parseInt(setCalorieCount.getText()), itemType);
            selectedMeal.addDietItem(itemToAdd);

            /*try{
                String SQL_QUERY = "INSERT INTO dietitem (itemtype, itemname, caloriecount) VALUES (?, ?, ?)";
                Connection connection = DBsession.INSTANCE.OpenConnection();
                PreparedStatement pst = connection.prepareStatement(SQL_QUERY);
                pst.setString(1, setItemName.getText());
                pst.setInt(2, Integer.parseInt(setCalorieCount.getText()));
                pst.setString(3, itemType.toString());
                pst.executeQuery();
                ResultSet generatedKey = pst.getGeneratedKeys();
                pst.close();

                SQL_QUERY = "INSERT INTO mealitem VALUES (?, ?)";
                pst = connection.prepareStatement(SQL_QUERY);
                pst.setInt(1, selectedMeal.getMealid());
                pst.setInt(2, generatedKey.getInt(1));
                pst.executeQuery();
                pst.close();
            }
            catch (Exception e){
                System.out.println("error inserting into database");
            }*/

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
