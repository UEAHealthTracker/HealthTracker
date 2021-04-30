import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DietPageController extends BaseController {

    @FXML TableView<Meal> dietTable;
    @FXML TableColumn<Meal, LocalTime> timeConsumed;
    @FXML TableColumn<Meal, String> food;
    @FXML TableColumn<Meal, String> drink;
    @FXML TableColumn<Meal, Integer> calorieCount;
    static Meal selectedMeal = null;

    public void initialize(){
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        populateDietTable();
    }

    //add data to diet table
    public void populateDietTable(){

        //diet page
        ObservableList<Meal> mealData = FXCollections.observableArrayList();

        /*try {
            String SQL_QUERY = "SELECT meal.mealid AS mealid, meal.timeconsumed AS timeconsumed, GROUP_CONCAT(dietitem.itemname) AS itemname, GROUP_CONCAT(dietitem.itemtype) AS itemtype, SUM(dietitem.caloriecount) AS caloriecount " +
                    "FROM mealitem JOIN meal ON meal.mealid = mealitem.mealid JOIN dietitem ON dietitem.itemid = mealitem.itemid " +
                    "WHERE meal.userid = ? " +
                    "GROUP BY mealitem.mealid " +
                    "ORDER BY meal.timeconsumed";

            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setInt(1, User.INSTANCE.getUserid());
            ResultSet rs = pst.executeQuery();

            ArrayList<DietItem> dietItems = new ArrayList<>();
            DietItem.Type itemtype;

            while(rs.next()){

                List<String> items = Arrays.asList(rs.getString("itemname").split("\\s*,\\s*"));
                List<String> itemtypes = Arrays.asList(rs.getString("itemtype").split("\\s*,\\s*"));

                for(int i = 0; i < items.size(); i++){
                    if(itemtypes.get(i).equals("food")){
                        itemtype = DietItem.Type.FOOD;
                    }
                    else if(itemtypes.get(i).equals("drink")){
                        itemtype = DietItem.Type.DRINK;
                    }
                    else {
                        itemtype = null;
                    }

                    dietItems.add(new DietItem(items.get(i), rs.getInt("caloriecount"), itemtype));
                }

                LocalTime time = rs.getTime("timeconsumed").toLocalTime().withSecond(0);

                mealData.add(new Meal(Integer.parseInt(rs.getString("mealid"), dietItems, time, rs.getInt("caloriecount")));
            }
            DBsession.INSTANCE.OpenConnection().close();

        }catch (Exception e){
            System.out.println("error retrieving data from database");
        }*/
        timeConsumed.setCellValueFactory(new PropertyValueFactory<>("timeConsumed"));
        food.setCellValueFactory(new PropertyValueFactory<>("foods"));
        drink.setCellValueFactory(new PropertyValueFactory<>("drinks"));
        calorieCount.setCellValueFactory(new PropertyValueFactory<>("calorieCount"));

        mealData.addAll(User.INSTANCE.dailyActivity.getMeals());
        dietTable.setItems(mealData);
    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem() {
        //check if user has selcted a meal
        if (dietTable.getSelectionModel().getSelectedItem() != null) {
            selectedMeal = dietTable.getSelectionModel().getSelectedItem();

            //try removing meal data from database
            try{
                /*String SQL_QUERY="DELETE FROM mealitem WHERE mealid=?;";
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
                pst.setInt(1, selectedMeal.getMealid());
                pst.executeUpdate();
                DBsession.INSTANCE.OpenConnection().close();*/

                //delete meal from user's daily activity
                User.INSTANCE.dailyActivity.removeMeal(selectedMeal);
            }
            catch(Exception e){
                System.out.println("error deleting meal");
            }
        }
        //update table
        populateDietTable();
    }

    public void openAddDietItem(javafx.event.ActionEvent actionEvent) throws IOException{

        if (dietTable.getSelectionModel().getSelectedItem() != null) {

            selectedMeal = dietTable.getSelectionModel().getSelectedItem();
        }

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/AddDietItemPage.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
