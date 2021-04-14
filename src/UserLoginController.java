import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//TODO add a logo/hero image and change font

public class UserLoginController extends BaseController{

    private static Button loginButton, createAccountButton;

    private Parent root;
    private Stage stage;
    private Scene scene;


    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //text fields for user input- email, height and weight are only used on sign up page
    @FXML  TextField usernameTextField;
    @FXML  PasswordField passwordTextField;
    //passwordStatusLabel shows if password is incorrect
    @FXML  TextField usernameTF;
    @FXML  PasswordField passwordTF;
    @FXML  Label passwordStatusLabel;
    @FXML  TextField emailTF;
    @FXML  TextField heightTF;
    @FXML  TextField weightTF;
    @FXML Button loginbtn;
    @FXML Button signupbtn;
    @FXML Button sign;

    public void initialize() {

        loginButton = loginbtn;
        //createacc=signup;
        loginButton.setStyle("-fx-background-color:#FA526C");

    }
    public void openSignUpPage(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("CreateAccountPage.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openLoginPage(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //Run the first time login
    public void login(javafx.event.ActionEvent actionEvent) throws IOException {

        //Get the entered input details for the user
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        //Create a temporary user object for checking their details
        User checkUser = new User(username, password);

        //Query to get information from db
        String SQL_QUERY="SELECT username, password, userObject FROM users where username=? and password=?";

        try{

            //Create a new db connection
            Connection connection = DatabaseConnect.connect();
            PreparedStatement pst = connection.prepareStatement(SQL_QUERY);

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs=pst.executeQuery();

            //Loop through db results
            while(rs.next()) {

                //Set username and password variables
                username=rs.getString("username");
                password=rs.getString("password");

                //Check the username/email matches a user in the db
                if (username.equals(checkUser.getUsername()) && password.equals(checkUser.getPassword())) {

                    //If the user exists, retrieve their object from the db
                    User newUser = (User) User.fromDatabaseString(rs.getString("userObject"));

                    //Load the home page for the selected user
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomePage.fxml"));

                    Parent root = (Parent) fxmlLoader.load();

                    HomePageController controller = fxmlLoader.getController();

                    //TODO remove testing data method
                    //Use the method in the user class to add any test data
                    //This will be run after every login
                    User.addTestData(newUser);

                    //Pass the user object to the new controller and set the label
                    controller.setUser(newUser);

                    controller.setUserLabel();

                    stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } else {

                    //Case for if the user does not exist in the db
                    //TODO maybe redirect to sign up page
                    loginButton.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                    loginButton.setText("Wrong Username/Password");
                }

                //Close the db connection
                connection.close();
            }

        }catch(Exception e) {
            System.out.println(e);
        }
    }

    //Method for a new user to create an account
    public void CreateAccount(ActionEvent event){

        //Variable initilizations to store new user info
        String username, password, email;
        double height, weight;

        //Get the user's desired details
        username = usernameTF.getText();
        password = passwordTF.getText();
        email = emailTF.getText();
        height = Double.parseDouble(heightTF.getText());
        weight = Double.parseDouble(weightTF.getText());

        //Store the details in a new user object
        User newUser = new User(username, password, email, height, weight);

        //Chech the email is valid for sign-up
        if (email.matches(EMAIL_PATTERN)) {

            try {
                //Create a new db connection
                Connection connection = DatabaseConnect.connect();

                //SQL query to add user information to db
                String SQL_INSERT="INSERT INTO users(username, password, userObject) VALUES (?,?,?)";


                PreparedStatement pst = connection.prepareStatement(SQL_INSERT);

                pst.setString(1, username);
                pst.setString(2, password);
                pst.setString(3, User.toDatabaseString(newUser));

                //Check the the username/password is unique
                if (CheckCredentials() > 0) {
                    loginbtn.setStyle("-fx-background-color:transparent;-fx-text-fill: red");
                    loginbtn.setText("Username/Password already exists");

                    connection.close();
                } else {
                    pst.executeUpdate();

                    connection.close();
                    newUser.setUsername(username);
                    newUser.setPassword(password);

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomePage.fxml"));

                    Parent root = (Parent) fxmlLoader.load();

                    HomePageController controller = fxmlLoader.getController();

                    //Pass the new user to the home page with their new object
                    controller.setUser(newUser);

                    controller.setUserLabel();

                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }else{
            createAccountButton.setStyle("-fx-background-color:transparent;-fx-text-fill: red");
            createAccountButton.setText("Wrong Email address");

        }
    }


    public Integer CheckCredentials(){
        int counter=0;
        String username;
        try{
            Connection connection = DatabaseConnect.connect();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM users");
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                username=rs.getString("username");

                if (username.equals(usernameTF.getText())) {
                    counter++;
                }
            }
        }catch(Exception e){ System.out.println(e);}
        return counter;
    }
}
