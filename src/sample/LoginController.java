/*
package sample;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;

public class LoginController {
    public final static LoginController INSTANCE = new LoginController();
    private static Button logbtn;
    @FXML TextField usernametf,passwordtf;
    @FXML Button closebtn;
    @FXML Button loginbtn;
    @FXML Button regbtn;
    private String Useracc,Passacc;
    @FXML
    BorderPane root;
    public User user=new User();
    WorkoutController wkController=new WorkoutController();


    public void initialize() {
        logbtn=loginbtn;
        logbtn.setStyle("-fx-background-color:#FA526C");
    }

    public void Login(ActionEvent actionEvent) {
        passwordtf.setStyle("-fx-text-fill:white");
        String userdb,passdb;

        try{
            ResultSet rs=DBsession.INSTANCE.Stmt().executeQuery("select username,password,realname from Users ");
            while(rs.next()) {
                userdb=rs.getString("username");
                passdb=rs.getString("password");
                User.INSTANCE.setUsername(usernametf.getText());
                User.INSTANCE.setPassword(passwordtf.getText());

                if (userdb.equals(User.INSTANCE.getUsername())&&passdb.equals(User.INSTANCE.getPassword()) ) {
                    User.INSTANCE.setRealName(rs.getString("realname"));
                    closewindow(actionEvent);
                    userController.userwindow();
                } else {
                    logbtn.setStyle("-fx-background-color:transparent;-fx-text-fill: red");
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

    @FXML
    public void closewindow(ActionEvent event) {
        Stage stage = (Stage) closebtn.getScene().getWindow();
        stage.close();
    }
    public void cw() {
        Stage stage = (Stage) closebtn.getScene().getWindow();
        stage.close();
    }

    public void OpenRegistrationwindow(ActionEvent event) throws IOException {
        BorderPane pane= FXMLLoader.load(getClass().getResource("Registration.fxml"));
        root.getChildren().setAll(pane);
    }


}
*/
