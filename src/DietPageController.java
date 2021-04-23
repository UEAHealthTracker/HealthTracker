import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DietPageController extends BaseController {

    @FXML TableView<Meal> dietTable;
    @FXML TableColumn<Meal, Timestamp> timeConsumed;
    @FXML TableColumn<Meal, ArrayList<DietItem>> items;
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

        //TODO add meal to User so it can be retrieved more easily?

        //diet page
        ObservableList<Meal> mealData = FXCollections.observableArrayList();

        String SQL_QUERY = "SELECT meal.mealid AS mealid, meal.timeconsumed AS timeconsumed, GROUP_CONCAT(dietitem.itemname) AS itemname, GROUP_CONCAT(dietitem.itemtype) AS itemtype, SUM(dietitem.caloriecount) AS caloriecount FROM mealitem JOIN meal ON meal.mealid = mealitem.mealid JOIN dietitem ON dietitem.itemid = mealitem.itemid WHERE meal.userid = ? GROUP BY mealitem.mealid ORDER BY meal.timeconsumed";

        try {
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, Integer.toString(User.INSTANCE.getUserid()));
            ResultSet rs = pst.executeQuery();

            ArrayList<DietItem> dietItems = new ArrayList<>();
            DietItem.Type itemtype;

            while(rs.next()){

                if (rs.getString("itemtype").equals("food")){
                    itemtype = DietItem.Type.FOOD;
                }
                else if (rs.getString("itemtype").equals("drink")){
                    itemtype = DietItem.Type.DRINK;
                }
                else {
                    itemtype = null;
                }

                LocalDateTime time = rs.getTimestamp("timeconsumed").toLocalDateTime().withSecond(0);
                dietItems.add(new DietItem(rs.getString("itemname"), rs.getInt("caloriecount"), itemtype));

                mealData.add(new Meal(Integer.parseInt(rs.getString("mealid")), dietItems, time, rs.getInt("caloriecount")));
            }

            timeConsumed.setCellValueFactory(new PropertyValueFactory<>("timeConsumed"));
            items.setCellValueFactory(new PropertyValueFactory<>("items"));
            calorieCount.setCellValueFactory(new PropertyValueFactory<>("calorieCount"));

            dietTable.setItems(mealData);

            DBsession.INSTANCE.OpenConnection().close();

        }catch (Exception e){
            System.out.println(e);
        }
    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(javafx.event.ActionEvent actionEvent) throws IOException{
        if (dietTable.getSelectionModel().getSelectedItem() != null) {
            Meal selectedMeal = dietTable.getSelectionModel().getSelectedItem();
            String SQL_QUERY="DELETE FROM Meal WHERE mealid=?;";
            try{
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
                pst.setInt(1, selectedMeal.getMealid());
                pst.executeUpdate();
                DBsession.INSTANCE.OpenConnection().close();
            }catch(Exception e){System.out.println(e);}

        }

    }

    public void addMeal(){

        Meal newMeal = new Meal();

        try {
            String SQL_QUERY = "INSERT INTO meal (timeconsumed, userid) VALUES (?, ?)";
            Connection connection = DBsession.INSTANCE.OpenConnection();
            PreparedStatement pst = connection.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
            pst.setTimestamp(1, Timestamp.valueOf(newMeal.getTimeConsumed()));
            pst.setInt(2, User.INSTANCE.getUserid());
            pst.executeQuery();
            ResultSet generatedKey = pst.getGeneratedKeys();

            newMeal.setMealid(generatedKey.getInt(1));
        }
        catch (Exception e){
            System.out.println("error- meal could not be added.");
        }

    }

    public void addDietItem(){

        if (dietTable.getSelectionModel().getSelectedItem() != null) {

            Meal selectedMeal = dietTable.getSelectionModel().getSelectedItem();

            //open add diet item page

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

                String SQL_QUERY = "INSERT INTO dietitem (itemtype, itemname, caloriecount) VALUES (?, ?, ?)";
                Connection connection = DBsession.INSTANCE.OpenConnection();
                PreparedStatement pst = connection.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
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
                System.out.println("Item cannot be added - some values may be empty.");
            }

        }
        else {
            addMeal();
        }
    }

    public void selectItemType() {
        setItemType.getItems().addAll("Food", "Drink");
    }

}
