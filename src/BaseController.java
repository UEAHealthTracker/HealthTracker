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
    Parent root;
    Stage stage;
    Scene scene;
    Label userLabel;

    //default constructor
    public BaseController(){

    }

    //connect to current user once logged in
    /*
    public BaseController(User user){
        displayUsername(User);
    }*/

    //display current logged in user's first name
    /*
    private void displayUsername(User user){
        userLabel.setText(user.firstName);
    }*/

    /**
     * Menu Functions just in case
     */
//    public void openHomePage(javafx.event.ActionEvent actionEvent) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
//        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public void openDietPage(javafx.event.ActionEvent actionEvent) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("DietPage.fxml"));
//        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public void openWorkoutPage(javafx.event.ActionEvent actionEvent) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("WorkoutPage.fxml"));
//        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public void openGroupsPage(javafx.event.ActionEvent actionEvent) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("GroupsPage.fxml"));
//        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public void openUserPage(javafx.event.ActionEvent actionEvent) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("UserPage.fxml"));
//        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

    public void MenuSwitch(javafx.event.ActionEvent actionEvent) throws IOException{
        String text = ((Button)actionEvent.getSource()).getText();
        switch(text){
            case "Home":
                root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
                break;
            case "Diet":
                root = FXMLLoader.load(getClass().getResource("DietPage.fxml"));
                break;
            case "Workout":
                root = FXMLLoader.load(getClass().getResource("WorkoutPage.fxml"));
                break;
            case "Groups":
                root = FXMLLoader.load(getClass().getResource("GroupsPage.fxml"));
                break;
            case "User Information":
                root = FXMLLoader.load(getClass().getResource("UserPage.fxml"));
                break;
            case "Edit group":
                root = FXMLLoader.load(getClass().getResource("EditGroupPage.fxml"));
                break;
            case "Create group":
                root = FXMLLoader.load(getClass().getResource("CreateGroupPage.fxml"));
                break;
            case "Add Diet Item":
                root = FXMLLoader.load(getClass().getResource("AddDietItemPage.fxml"));
                break;

        }
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
