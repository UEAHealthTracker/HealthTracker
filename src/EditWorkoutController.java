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

    public void updateWorkout(){
        String SQL_Update=" UPDATE workout SET duration =?, workouttype=? WHERE workoutid=?";
        try {
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Update);
            pst.setInt(1, Integer.parseInt(duration2.getText()));
            pst.setString(2,workoutTypeTF.getText());
            pst.setInt(3,Workout.Instance.getWorkoutid());
            pst.executeUpdate();
            DBsession.INSTANCE.OpenConnection().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
