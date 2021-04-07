import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController extends BaseController implements Initializable {

    @FXML
    private TableView<Goal> goalTableView;

    @FXML
    public TableColumn<Goal, String> goalName;

    @FXML
    public TableColumn<Goal, String> goalDate;

    @FXML
    public TableColumn<Goal, String> goalStatus;

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){
        Goal data = goalTableView.getSelectionModel().getSelectedItem();
        if (data != null) {
            data = goalTableView.getSelectionModel().getSelectedItem();

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

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddWeightGoal.fxml"));

        //Load the desired page/controller
        Parent root = (Parent) fxmlLoader.load();

        //Get the new controller for the desired page
        BaseController baseController = fxmlLoader.getController();

        //Pass the current user object to the new controller
        baseController.setUser(user);

        //Set the label of the new page to the user's name
        baseController.setUserLabel();

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
        System.out.println(user.getGoals().toString());
        for(int i = 0; i < user.getGoals().size(); i++){
            System.out.println(user.getGoals().get(i).goalName);
        }
        if (!user.getGoals().isEmpty()) {
            goalName.setCellValueFactory(new PropertyValueFactory<>("goalName"));
            goalDate.setCellValueFactory(new PropertyValueFactory<>("goalDate"));
            goalStatus.setCellValueFactory(new PropertyValueFactory<>("goalStatus"));

            goalTableView.setItems(getGoals());
        } else {
            //skip population
        }

    }


    private ObservableList<Goal> getGoals(){
        ObservableList<Goal> goals = FXCollections.observableArrayList();
        goals.addAll(user.getGoals());
        return goals;
    }
}
