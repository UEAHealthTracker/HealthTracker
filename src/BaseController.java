import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseController {
    public static final BaseController Instance= new BaseController();
    Parent root;
    Stage stage;
    Scene scene;
    @FXML
    Label userLabel;
    public String filename="";
    //default constructor
    public BaseController(){

    }

    public void MenuSwitch(javafx.event.ActionEvent actionEvent) throws IOException{
       String text = ((Button)actionEvent.getSource()).getText();
       switch(text){
           case "Home":
                filename= "FXML/HomePage.fxml";break;
           case "Diet":
                filename= "FXML/DietPage.fxml";break;
           case "Workout":
                filename= "FXML/WorkoutPage.fxml";break;
           case "Groups":
                filename= "FXML/GroupsPage.fxml";break;
           case "User Information":
                filename= "FXML/UserPage.fxml";break;
           case "Edit group":
                filename= "FXML/EditGroupPage.fxml"; break;
           case "Create group":
                filename= "FXML/CreateGroupPage.fxml";break;
           case "Add Diet Item":
                filename= "FXML/AddDietItemPage.fxml"; break;
           case "EditWorkoutPage":
                filename= "FXML/EditWorkoutPage.fxml";break;
           case "AddWorkoutPage":
                filename= "FXML/AddWorkoutPage.fxml";break;
           case "EditUserPage":
                filename= "FXML/EditUserPage.fxml";break;
            case "Join Group":
                filename= "FXML/JoinGroup.fxml";break;
        }
        root = FXMLLoader.load(getClass().getClassLoader().getResource(filename));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void Switch(javafx.event.ActionEvent actionEvent,String filename) throws IOException{
        root = FXMLLoader.load(getClass().getClassLoader().getResource(filename));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
