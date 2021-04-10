import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseController {
    public static final BaseController Instance= new BaseController();
    Parent root;
    Stage stage;
    Scene scene;
    Label userLabel;
    public String filename="";
    //default constructor
    public BaseController(){

    }


    public void MenuSwitch(javafx.event.ActionEvent actionEvent) throws IOException{
        String text = ((Button)actionEvent.getSource()).getText();

        switch(text){
            case "Home":
                filename="HomePage.fxml";break;
            case "Diet":
                filename="DietPage.fxml";break;
            case "Workout":
                filename="WorkoutPage.fxml";break;
            case "Groups":
                filename="GroupsPage.fxml";break;
            case "User Information":
                filename="UserPage.fxml";break;
            case "Edit group":
                filename="EditGroupPage.fxml"; break;
            case "Create group":
                filename="CreateGroupPage.fxml";break;
            case "Add Diet Item":
                filename="AddDietItemPage.fxml"; break;
            case "EditWorkoutPage":
                filename="EditWorkoutPage.fxml";break;
            case "AddWorkoutPage":
                filename="AddWorkoutPage.fxml";
                break;
            case "EditUserPage":
                filename="EditUserPage.fxml";break;
        }
        root = FXMLLoader.load(getClass().getResource(filename));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void Switch(javafx.event.ActionEvent actionEvent,String filename) throws IOException{
        root = FXMLLoader.load(getClass().getResource(filename));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
