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

import java.sql.SQLException;
import java.util.Timer;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;





public class UserLoginController {
    private static Button logbtn,createacc;
    private static TextField pass,user;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private static final String SQL_INSERT="INSERT INTO Users(userid,username, password, email, height, weight) VALUES (?,?,?,?,?,?)";
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

        user=usernameTextField;
        pass=passwordTextField;
        logbtn=loginbtn;
        //createacc=signup;
        logbtn.setStyle("-fx-background-color:#FA526C");

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


public void login(javafx.event.ActionEvent actionEvent) throws IOException {
    passwordTextField.setStyle("-fx-text-fill:white");
    String userdb=null;
    String passdb=null;
    User.INSTANCE.setUsername(usernameTextField.getText());
    User.INSTANCE.setPassword(passwordTextField.getText());
    String SQL_QUERY="select userid,username,password,realname,weight,height,age,email from Users where username=? and password=?";

    try{
        PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
        pst.setString(1, usernameTextField.getText());
        pst.setString(2, passwordTextField.getText());
        ResultSet rs=pst.executeQuery();
            while(rs.next()) {
                userdb=rs.getString("username");
                passdb=rs.getString("password");

                if (userdb.equals(User.INSTANCE.getUsername())&&passdb.equals(User.INSTANCE.getPassword()) ) {
                    User.INSTANCE.setUserid(rs.getInt("userid"));
                    User.INSTANCE.setRealName(rs.getString("realname"));
                    User.INSTANCE.setEmail(rs.getString("email"));
                    User.INSTANCE.setHeight(rs.getDouble("height"));
                    User.INSTANCE.setWeight(rs.getDouble("weight"));
                    User.INSTANCE.setAge(rs.getInt("age"));
                    //User.INSTANCE.setHeight(Double.parseDouble(rs.getString("height")));
                    //User.INSTANCE.setWeight(Integer.parseInt(rs.getString("weight")));
                    //User.INSTANCE.setAge(Integer.parseInt(rs.getString("age")));
                    root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
                    stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } else {
                    String s=null;
                    logbtn.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                    logbtn.setText("Wrong Username/Password");
                    thread.start();
                }
            }
        DBsession.INSTANCE.OpenConnection().close();
    }catch(Exception e){ System.out.println(e);}
}

    //https://riptutorial.com/javafx/example/7291/updating-the-ui-using-platform-runlater
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    logbtn.setStyle("-fx-background-color:#FA526C;-fx-text-fill: white");
                    logbtn.setText("Login");
                }
            };
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                }
                // UI update is run on the Application thread
                Platform.runLater(updater);
            }
        }

    });

    public void CreateAccount(ActionEvent event) throws SQLException {
        String usnm,pass,email;
        int ht;
        int wt;
        int userId=0;
        usnm=usernameTF.getText();
        pass=passwordTF.getText();
        email=emailTF.getText();
        ht=Integer.parseInt(heightTF.getText());
        wt=Integer.parseInt(weightTF.getText());
        //Get the last userid:
        ResultSet userIdSet=DBsession.INSTANCE.Stmt().executeQuery("select userid from Users ");
        while(userIdSet.next()){
            userId= userIdSet.getInt("userid");
            //System.out.println(userId);
        }
        userId+=1;
        //System.out.println(userId);
        if (email.matches(EMAIL_PATTERN)) {
            try {
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_INSERT);
                pst.setInt(1,userId);
                pst.setString(2, usnm);
                pst.setString(3, pass);
                pst.setString(4, email);
                pst.setInt(5,ht);
                pst.setInt(6,wt);
                if (CheckCredentials() > 0) {
                    loginbtn.setStyle("-fx-background-color:transparent;-fx-text-fill: red");
                    loginbtn.setText("Username/Password already exists");
                    thread.start();
                    DBsession.INSTANCE.OpenConnection().close();
                } else {
                    thread.start();
                    pst.executeUpdate();
                    User.INSTANCE.setUsername(usernameTF.getText());
                    User.INSTANCE.setPassword(passwordTF.getText());
                    root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                }

                DBsession.INSTANCE.OpenConnection().close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }else{
            createacc.setStyle("-fx-background-color:transparent;-fx-text-fill: red");
            createacc.setText("Wrong Email address");
            thread.start();

        }
    }


    public int CheckCredentials(){
        int counter=0;
        String userdb,emaildb;
        try{
            ResultSet rs=DBsession.INSTANCE.Stmt().executeQuery("select username,email from Users ");
            while(rs.next()) {
                userdb=rs.getString("username");
                emaildb=rs.getString("email");
                if (userdb.equals(usernameTF.getText())||emaildb.equals(emailTF.getText())) {
                    counter++;
                }else{}
            }
        }catch(Exception e){ System.out.println(e);}
        return counter;
    }
}
