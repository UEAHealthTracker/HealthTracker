import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditWorkoutController extends BaseController{

    @FXML
    ComboBox WorkoutTypeSelector;
    @FXML
    TextField durationTF;
    @FXML TextField duration2;
    @FXML ComboBox WorkoutTypeSelector2;
    @FXML ComboBox ID;
    @FXML
    Label workoutTypelable;
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
                /* WorkoutTypeSelector.getItems().add(wid.getString("workouttype")); String duration2 = durationTF.getText(wid.getInt("duration"),);*///String duration = durationTF.getText();// String workoutType = wid.getString("workouttype");
                workoutType = wid.getString("workouttype");
                Integer duration = wid.getInt("duration");
                workoutTypelable.setText(workoutType);
                 //WorkoutTypeSelector2.setValue(workoutType);
                duration2.setText(String.valueOf(duration));
                // WorkoutTypeSelector.setText(workoutType); //  String.valueOf(duration);  //durationTF.getText(duration);*/    //durationTF.getText(); Workout.Instance.setWorkoutid(Integer.parseInt(wid.getString("workoutid")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
