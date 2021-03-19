import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.text.TableView;
import java.io.IOException;

public class HomePageController extends BaseController {

    private TableView goalsTable;

    //add data to goal table
    public void populateGoalsTable(){

    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){

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
}
