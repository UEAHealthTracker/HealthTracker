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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DietPageController extends BaseController {

    @FXML TableView<Meal> dietTable;
    @FXML TableColumn<Meal, Timestamp> timeConsumed;
    @FXML TableColumn<Meal, ArrayList<Food>> foods;
    @FXML TableColumn<Meal, ArrayList<Drink>> drinks;
    @FXML TableColumn<Meal, Integer> calorieCount;

    //add diet item page
    @FXML ComboBox<String> setItemType;
    @FXML TextField setItemName;
    @FXML TextField setCalorieCount;

    public void initialize(){
        populateDietTable();
    }

    //add data to diet table
    public void populateDietTable(){

        //diet page
        ObservableList<Meal> mealData = FXCollections.observableArrayList();

        String SQL_QUERY = "SELECT mealid, timeconsumed, food.foodName AS foodname, drink.drinkName AS drinkname, food.caloriecount + drink.caloriecount AS caloriecount \n" +
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

                LocalDateTime time = rs.getTimestamp("timeconsumed").toLocalDateTime().withSecond(0);
                food.add(new Food(rs.getString("foodname")));
                drink.add(new Drink(rs.getString("drinkname")));

                mealData.add(new Meal(Integer.parseInt(rs.getString("mealid")), food, drink, time, rs.getInt("caloriecount")));
            }

            timeConsumed.setCellValueFactory(new PropertyValueFactory<>("timeConsumed"));
            foods.setCellValueFactory(new PropertyValueFactory<>("foods"));
            drinks.setCellValueFactory(new PropertyValueFactory<>("drinks"));
            calorieCount.setCellValueFactory(new PropertyValueFactory<>("calorieCount"));

            dietTable.setItems(mealData);

            DBsession.INSTANCE.OpenConnection().close();

        }catch (Exception e){
            System.out.println(e);
        }
    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(javafx.event.ActionEvent actionEvent) throws IOException{
        /*if (dietTable.getSelectionModel().getSelectedItem() != null) {
            Meal selectedMeal = dietTable.getSelectionModel().getSelectedItem();
            String SQL_QUERY="DELETE FROM Meal WHERE mealid=?;";
            try{
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
                pst.setInt(1, selectedMeal.getMealid());
                pst.executeUpdate();
                DBsession.INSTANCE.OpenConnection().close();
            }catch(Exception e){System.out.println(e);}

        }*/

    }

    public void selectItemType() {
        setItemType.getItems().addAll("Food", "Drink");
    }

}
