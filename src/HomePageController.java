import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class HomePageController extends BaseController implements Initializable {

    @FXML
    private TableView<Goal> tbData = new TableView<>();

    @FXML
    public TableColumn<Goal, String> goalName;

    @FXML
    public TableColumn<Goal, String> goalDate;

    @FXML
    public TableColumn<Goal, String> goalStatus;

    @FXML
    public TableColumn<Goal, String> goalGroups;

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){
        Goal data = tbData.getSelectionModel().getSelectedItem();
        if (data != null) {
            data = tbData.getSelectionModel().getSelectedItem();

            for(int i = 0; i < user.getGoals().size(); i++){
                if(user.getGoals().get(i) == data){
                    user.removeGoal(user.getGoals().get(i));
                }
            }
        }
    }

    public void openSelectGoalTypePage(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SelectGoalType.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openAddGoalPage(javafx.event.ActionEvent actionEvent) throws IOException {
        //if user selects weight goal then open AddWeightGoal page
        //else if user selects workout goal then open AddWorkoutGoal page
        root = FXMLLoader.load(getClass().getResource("AddWeightGoal.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openEditGoalPage(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("EditGoal.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {

            populateGoalTable();

        });

    }

    //Method to populate the goal table for the page
    public void populateGoalTable(){
        goalName.setCellValueFactory(new PropertyValueFactory<>("goalName"));
        goalDate.setCellValueFactory(new PropertyValueFactory<>("goalDate"));
        goalDate.setCellValueFactory(new PropertyValueFactory<>("goalStatus"));
        goalGroups.setCellValueFactory(new PropertyValueFactory<>("goalGroups"));

        tbData.setItems(getGoals());
    }


    private ObservableList<Goal> getGoals(){
        ObservableList<Goal> goals = FXCollections.observableArrayList();
        goals.addAll(user.getGoals());
        return goals;
    }
}
