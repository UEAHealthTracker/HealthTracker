import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BaseController {

    //Store a user in every controller that can be accesses on initialize using runLater()
    User user;

    //Default root, stage, and scene for JavaFX
    Parent root;
    Stage stage;
    Scene scene;

    //Label to display the user's name in the top left user by most controllers
    @FXML
    Label userLabel;

    //Default constructor
    public BaseController(){

    }

    //Method to assign the user when the controllers are passed around
    void setUser(User user){
        this.user = user;
    }

    //Method to extract the name from the user and display it on the label
    void setUserLabel(){
        userLabel.setText(user.getUsername());
    }

    //Switch to allow the user to change pages/controllers
    public void MenuSwitch(javafx.event.ActionEvent actionEvent) throws IOException{
        String text = ((Button)actionEvent.getSource()).getText();
        String filename = switch (text) {
            case "Home" -> "HomePage.fxml";
            case "Diet" -> "DietPage.fxml";
            case "Workout" -> "WorkoutPage.fxml";
            case "Groups" -> "GroupsPage.fxml";
            case "User Information" -> "UserPage.fxml";
            case "Edit group" -> "EditGroupPage.fxml";
            case "Create group" -> "CreateGroupPage.fxml";
            case "Add Diet Item" -> "AddDietItemPage.fxml";
            case "EditWorkoutPage" -> "EditWorkoutPage.fxml";
            case "AddWorkoutPage" -> "AddWorkoutPage.fxml";
            case "EditUserPage" -> "EditUserPage.fxml";
            default -> "";
        };
        loadPage(actionEvent, filename);

    }

    //Method to load pages using the base controller
    private void loadPage(javafx.event.ActionEvent actionEvent, String filename) throws IOException {

        //Create new FXMLLoader object to create the page
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filename));

        //Load the desired page/controller
        Parent root = (Parent) fxmlLoader.load();

        //Get the new controller for the desired page
        BaseController baseController = fxmlLoader.getController();

        //Save the user before moving to the next page
        try {
            //Create a new db connection
            Connection connection = DatabaseConnect.connect();

            //SQL query to add user information to db
            String SQL_INSERT="UPDATE users SET userObject = (?)";
            PreparedStatement pst = connection.prepareStatement(SQL_INSERT);

            pst.setString(1, User.toDatabaseString(user));


            pst.executeUpdate();

            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        //Pass the current user object to the new controller
        baseController.setUser(user);

        //Set the label of the new page to the user's name
        baseController.setUserLabel();

        //Render the page
        stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
