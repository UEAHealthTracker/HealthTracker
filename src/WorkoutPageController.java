
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkoutPageController extends BaseController {

    public TableView workoutTable;
    private static Button addWorkoutbtn;
    private static final String SQL_Insert ="INSERT INTO workout (workoutid, sets, reps, calories, weekday) VALUES ('4','3','20','150 calories','Wednesday')";
    @FXML ComboBox WorkoutTypeSelector;
    //add data to workout table
    public void populateWorkoutTable(){
      // Statement statement = conn.createStatement();
        try {
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Insert);
           // statement.executeUpdate(SQL_Insert);
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Insert);
            pst.setString(1, usernameTextField.getText());
            pst.setString(2, passwordTextField.getText());
            ResultSet rs=pst.executeQuery();
            while(rs.next()) {
                userdb=rs.getString("username");
                passdb=rs.getString("password");

                if (userdb.equals(User.INSTANCE.getUsername())&&passdb.equals(User.INSTANCE.getPassword()) ) {
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

        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }

    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){

    }


}

