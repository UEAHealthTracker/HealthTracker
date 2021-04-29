import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditWorkoutController extends BaseController{

    @FXML
    ComboBox WorkoutTypeSelector;
    @FXML
    TextField durationTF;
    @FXML TextField duration2;
    @FXML TextField workoutTypeTF;
    boolean workoutidbool;

    /*public void fillID(){
        String SQL_Select="Select workout.workoutid FROM workout JOIN day ON day.workoutid=workout.workoutid JOIN Users ON day.userid=Users.userid and Users.userid=?";
        try {
            PreparedStatement sel = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Select);
            sel.setInt(1, Integer.parseInt(String.valueOf( User.INSTANCE.getUserid())));
            ResultSet wid = sel.executeQuery();
            while(wid.next()){
                workoutidbool =   ID.getItems().add(wid.getInt("workoutid"));
            }

            DBsession.INSTANCE.OpenConnection().close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        selectWorkoutType();
    }*/
    public void initialize(){
        userLabel.setText("Hello "+User.INSTANCE.getUsername());

        String SQL_Select="Select workouttype,duration FROM workout JOIN day ON day.workoutid=workout.workoutid JOIN Users ON day.userid=Users.userid and workout.workoutid =?" ;
        //Select workouttype,duration FROM workout JOIN day ON day.workoutid=workout.workoutid JOIN Users ON day.userid=Users.userid and workout.workoutid=1
        //Select workouttype,duration FROM workout, Users WHERE User.userid=1 and workout.workoutid=1
        //Select workouttype,duration from workout where workoutid=?
        // Select workouttype,duration FROM workout JOIN day ON day.workoutid=workout.workoutid JOIN Users ON day.userid=Users.userid and Users.userid=?
        // Select workouttype,duration FROM workout JOIN day ON day.workoutid=workout.workoutid JOIN Users ON day.userid=Users.userid and Users.userid=? and workout.workoutid=?
        try {
            PreparedStatement sel = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Select);
            // sel.setInt(1,Integer.parseInt(String.valueOf(User.INSTANCE.getUserid())));
            sel.setInt(1,Integer.parseInt(String.valueOf(Workout.Instance.getWorkoutid())));
            ResultSet wid = sel.executeQuery();
            String workoutType ="";
            while(wid.next()){
                workoutType = wid.getString("workouttype");
                Integer duration = wid.getInt("duration");
                workoutTypeTF.setText(workoutType);
                duration2.setText(String.valueOf(duration));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateWorkout(ActionEvent event) throws IOException {
        int cal =0;

        String SQL_Update=" UPDATE workout SET duration =?, workouttype=?, calories =? WHERE workoutid=?";
        try {
            for (WorkoutType type : WorkoutType.values()) {
                if(workoutTypeTF.getText().equals(type.ID)){
                    cal= type.MET;
                    }
            }
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Update);
            pst.setInt(1, Integer.parseInt(duration2.getText()));
            pst.setString(2,workoutTypeTF.getText());
            pst.setInt(3, cal * Integer.parseInt(duration2.getText()));
            pst.setInt(4,Workout.Instance.getWorkoutid());
           int msg = pst.executeUpdate();
            DBsession.INSTANCE.OpenConnection().close();
            if(msg==1){
                updateMessage("Workout Successfully Updated!","WORKOUT UPDATED");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        switchAfterUpdate(event);

    }

    public void switchAfterUpdate(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("FXML/WorkoutPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void updateMessage(String message, String title){
        JOptionPane.showMessageDialog(null, message, title,JOptionPane.INFORMATION_MESSAGE);
    }

}
