import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DietPageController extends BaseController {

    //diet page
    @FXML TableView<Meal> dietTable;
    @FXML TableColumn<Meal, Timestamp> mealTime;
    @FXML TableColumn<Meal, ArrayList<Food>> mealFood;
    @FXML TableColumn<Meal, ArrayList<Drink>> mealDrink;
    @FXML TableColumn<Meal, Integer> mealCalories;

    //add diet item page
    @FXML ComboBox<String> itemType;
    @FXML TextField itemName;
    @FXML TextField calorieCount;

    public void initialize(){
        populateDietTable();
    }

    //add data to diet table
    public void populateDietTable(){

        ObservableList mealData = FXCollections.observableArrayList();

        String SQL_QUERY = "SELECT timeconsumed, food.foodName AS foodname, drink.drinkName AS drinkname, food.caloriecount + drink.caloriecount AS caloriecount \n" +
                            "FROM meal JOIN food ON food.foodid = meal.foodid JOIN drink ON drink.drinkid = meal.drinkid \n" +
                            "WHERE userid = ? \n" +
                            "GROUP BY mealid \n" +
                            "ORDER BY timeconsumed";
        try {
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, Integer.toString(User.INSTANCE.getUserid()));
            ResultSet rs = pst.executeQuery();

            ArrayList<Food> food = new ArrayList<>();
            ArrayList<Drink> drink = new ArrayList<>();
            while(rs.next()){
                LocalDateTime timeConsumed = LocalDateTime.parse(rs.getString("timeconsumed"));
                food.add(new Food(rs.getString("foodname")));
                drink.add(new Drink(rs.getString("drinkname")));

                mealData.add(new Meal(food, drink, timeConsumed, rs.getInt("caloriecount")));
            }

            mealTime.setCellValueFactory(new PropertyValueFactory<>("mealTime"));
            mealFood.setCellValueFactory(new PropertyValueFactory<>("mealFood"));
            mealDrink.setCellValueFactory(new PropertyValueFactory<>("mealDrink"));
            mealCalories.setCellValueFactory(new PropertyValueFactory<>("mealCalories"));

            dietTable.setItems(mealData);

            DBsession.INSTANCE.OpenConnection().close();

        }catch (Exception e){
            System.out.println(e);
        }
    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){

    }

    public void selectItemType(){
        itemType.getItems().addAll("Food", "Drink");
    }

}
