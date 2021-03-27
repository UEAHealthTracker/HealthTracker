
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;

enum WorkoutType {


    //exercise types- MET value for each type based on information at https://golf.procon.org/met-values-for-800-activities/

    AEROBICS(6,"AEROBICS"),
    BASKETBALL(8,"BASKETBALL"),
    BOXING(12,"BOXING"),
    CRICKET(5,"CRICKET"),
    CIRCUIT_TRAINING(8, "CIRCUIT_TRAINING"),
    CYCLING(7,"CYCLING"),
    DANCING(5,"DANCING"),
    FOOTBALL(8,"FOOTBALL"),
    GYMNASTICS(6,"GYMNASTICS"),
    HIKING(7,"HIKING"),
    HOCKEY(8,"HOCKEY"),
    HORSE_RIDING(5,"HORSE_RIDING"),
    MOUNTAIN_BIKING(9,"MOUNTAIN_BIKING"),
    MARTIAL_ARTS(10,"MARTIAL_ARTS"),
    PILATES(3,"PILATES"),
    RUNNING(8,"RUNNING"),
    ROCK_CLIMBING(8,"ROCK_CLIMBING"),
    ROWING(6,"ROWING"),
    RUGBY(8,"RUGBY"),
    SKATING(7,"SKATING"),
    SKIING(7,"SKIING"),
    SKIPPING(12,"SKIPPING"),
    SWIMMING(7,"SWIMMING"),
    TENNIS(7,"TENNIS"),
    WALKING(3,"WALKING"),
    WEIGHT_LIFTING(5,"WEIGHT_LIFTING"),
    YOGA(3,"YOGA");

    //MET = metabolic equivalent for task- it is based on the intensity and used to calculate calories burnt
    public int MET;
    public String ID;

    //default constructor for enum
    WorkoutType(int MET,String ID){
        this.MET = MET;
        this.ID=ID;
    }
}

public class WorkoutPageController extends BaseController{
    public final static  WorkoutPageController Instance= new WorkoutPageController();
    public TableView workoutTable;

    private static Button addWorkoutbtn;
   // private static final String SQL_Insert ="INSERT INTO workout (workoutid, sets, reps, calories, weekday) VALUES ('4','3','20','150 calories','Wednesday')";
    @FXML ComboBox WorkoutTypeSelector;
    @FXML TextField durationTF;
    boolean open=false;
    public void init(){
        if(open==false) {
            for (WorkoutType type : WorkoutType.values()) {
                WorkoutTypeSelector.getItems().add(type.ID);
            }
            open=true;
        }

    }

    //add data to workout table
    public void populateWorkoutTable(){
        int cal=0;
      String SQL_Insert="INSERT INTO workout( duration, workoutType, calories) VALUES (?,?,?)";
            try {
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Insert);
              pst.setInt(1, Integer.parseInt(durationTF.getText()));
            pst.setString(2,WorkoutTypeSelector.getSelectionModel().getSelectedItem().toString());
                for (WorkoutType type : WorkoutType.values()) {
                    if(WorkoutTypeSelector.getSelectionModel().getSelectedItem().equals(type.ID)){
                        cal= type.MET;
                    }
                }
            pst.setInt(3,cal* Integer.parseInt(durationTF.getText()));
            pst.executeUpdate();
            DateSet();
            DBsession.INSTANCE.OpenConnection().close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public void DateSet(){
        LocalDate date=LocalDate.now();
//        String SQL_Insert="INSERT INTO day( date, workoutid, calories) Select workoutid,username from Users ON day.dayid=Users.dayid Join workout ON workout.workoutid=day.dayid Where username=? and workoutid=?";   ";
       // String SQL_Insert="INSERT INTO day(date,mealid,workoutid,userid) values";
        //String SQL_Insert="INSERT INTO day(date,workoutid,userid) values(?,(Select workoutid from workout where workouttype=? AND duration=?),?)";
        String SQL_Select="Select workoutid from workout where workouttype=? AND duration=?";
        String SQL_Insert="INSERT INTO day(date,workoutid,userid) values(?,?,?)";
        try {
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Insert);
            PreparedStatement sel = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Select);
            sel.setString(1,WorkoutTypeSelector.getSelectionModel().getSelectedItem().toString());
            sel.setInt(2, Integer.parseInt(durationTF.getText()));
            ResultSet wid= sel.executeQuery();
            while(wid.next()) {
             Workout.Instance.setWorkoutid(Integer.parseInt(wid.getString("workoutid")));
            }
            pst.setString(1,date.toString());
            pst.setInt(2, Workout.Instance.getWorkoutid());
            pst.setString(3, User.INSTANCE.getUsername());
             pst.executeUpdate();

            DBsession.INSTANCE.OpenConnection().close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){

    }


}

