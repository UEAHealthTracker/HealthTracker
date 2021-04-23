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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserLoginController extends BaseController {
    public static final UserLoginController Instance= new UserLoginController();
    private static Button logbtn,createacc;
    private static TextField pass,user;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private static final String SQL_INSERT="INSERT INTO Users( username, password, email, height, weight) VALUES (?,?,?,?,?)";
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
                    User.INSTANCE.setUserid(Integer.parseInt(rs.getString("userid")));
                    User.INSTANCE.setRealName(rs.getString("realname"));
                    User.INSTANCE.setEmail(rs.getString("email"));
                    User.INSTANCE.setHeight(Double.parseDouble(rs.getString("height")));
                    User.INSTANCE.setWeight(Double.parseDouble(rs.getString("weight")));
                    BaseController.Instance.Switch(actionEvent,"FXML/HomePage.fxml");

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

    public void CreateAccount(ActionEvent event){
        String usnm,pass,email;
        Integer ht;
        Integer wt;
        usnm=usernameTF.getText();
        pass=passwordTF.getText();
        email=emailTF.getText();
        ht=Integer.parseInt(heightTF.getText());
        wt=Integer.parseInt(weightTF.getText());

        if (email.matches(EMAIL_PATTERN)) {
            try {
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_INSERT);
                pst.setString(1, usnm);
                pst.setString(2, pass);
                pst.setString(3, email);
                pst.setInt(4,ht);
                pst.setInt(5,wt);
                if (CheckCredentials() > 0) {
                    loginbtn.setStyle("-fx-background-color:transparent;-fx-text-fill: red");
                    loginbtn.setText("Username/Password already exists");
                    thread.start();
                    DBsession.INSTANCE.OpenConnection().close();
                } else {
                    thread.start();
                    User.INSTANCE.setUsername(usernameTF.getText());
                    User.INSTANCE.setPassword(passwordTF.getText());
                    User.INSTANCE.setHeight(Double.parseDouble(heightTF.getText()));
                    User.INSTANCE.setWeight(Double.parseDouble(weightTF.getText()));
                    pst.executeUpdate();
                }
                userid();
                BaseController.Instance.Switch(event,"HomePage.fxml");

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

    public void userid(){

        String SQL="select userid,realname,age,email from Users where username=? and password=?";
        try{
            PreparedStatement pst1 = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL);
            pst1.setString(1, User.INSTANCE.getUsername());
            pst1.setString(2, User.INSTANCE.getPassword());
            ResultSet rs=pst1.executeQuery();
            while(rs.next()) {
                User.INSTANCE.setUserid(Integer.parseInt(rs.getString("userid")));
                User.INSTANCE.setRealName(rs.getString("realname"));
                User.INSTANCE.setAge(Integer.parseInt(rs.getString("age")));
                User.INSTANCE.setEmail(rs.getString("email"));
            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){ System.out.println(e);}

    }

    public Integer CheckCredentials(){
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
