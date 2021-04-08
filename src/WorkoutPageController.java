
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

    AEROBICS(6,"AEROBICS",1),
    BASKETBALL(8,"BASKETBALL",2),
    BOXING(12,"BOXING",3),
    CRICKET(5,"CRICKET",4),
    CIRCUIT_TRAINING(8, "CIRCUIT_TRAINING",5),
    CYCLING(7,"CYCLING",6),
    DANCING(5,"DANCING",7),
    FOOTBALL(8,"FOOTBALL",8),
    GYMNASTICS(6,"GYMNASTICS",9),
    HIKING(7,"HIKING",10),
    HOCKEY(8,"HOCKEY",11),
    HORSE_RIDING(5,"HORSE_RIDING",12),
    MOUNTAIN_BIKING(9,"MOUNTAIN_BIKING",13),
    MARTIAL_ARTS(10,"MARTIAL_ARTS",14),
    PILATES(3,"PILATES",15),
    RUNNING(8,"RUNNING",16),
    ROCK_CLIMBING(8,"ROCK_CLIMBING",17),
    ROWING(6,"ROWING",18),
    RUGBY(8,"RUGBY",19),
    SKATING(7,"SKATING",20),
    SKIING(7,"SKIING",21),
    SKIPPING(12,"SKIPPING",22),
    SWIMMING(7,"SWIMMING",23),
    TENNIS(7,"TENNIS",24),
    WALKING(3,"WALKING",24),
    WEIGHT_LIFTING(5,"WEIGHT_LIFTING",25),
    YOGA(3,"YOGA",26);

    //MET = metabolic equivalent for task- it is based on the intensity and used to calculate calories burnt
    public int MET;
    public String ID;
    public int id;

    //default constructor for enum
    WorkoutType(int MET,String ID, int id){
        this.MET = MET;
        this.ID=ID;
        this.id=id;
    }
}

public class WorkoutPageController extends BaseController{
    public final static  WorkoutPageController Instance= new WorkoutPageController();
    public TableView workoutTable;

    private static Button addWorkoutbtn;
   // private static final String SQL_Insert ="INSERT INTO workout (workoutid, sets, reps, calories, weekday) VALUES ('4','3','20','150 calories','Wednesday')";
    @FXML ComboBox WorkoutTypeSelector;
    @FXML TextField durationTF;
    @FXML ComboBox id;
    boolean open=false;
   public void init(){
        if(open==false) {
            for (WorkoutType type : WorkoutType.values()) {
                WorkoutTypeSelector.getItems().add(type.ID);
            }
            open=true;
        }

    }
    public void fillUpdateID(){
                if(open==false){
                    for(WorkoutType type :WorkoutType.values()){
                        id.getItems().add(type.id);
                    }

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

            DBsession.INSTANCE.OpenConnection().close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        DateSet();
    }
    public void DateSet(){
        LocalDate date=LocalDate.now();
//        String SQL_Insert="INSERT INTO day( date, workoutid, calories) Select workoutid,username from Users ON day.dayid=Users.dayid Join workout ON workout.workoutid=day.dayid Where username=? and workoutid=?";   ";
       // String SQL_Insert="INSERT INTO day(date,mealid,workoutid,userid) values";
        //String SQL_Insert="INSERT INTO day(date,workoutid,userid) values(?,(Select workoutid from workout where workouttype=? AND duration=?),?)";
        String SQL_Select="Select workoutid from workout where workouttype=? AND duration=?";

        try {
            PreparedStatement sel = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Select);
            sel.setString(1,WorkoutTypeSelector.getSelectionModel().getSelectedItem().toString());
            sel.setInt(2, Integer.parseInt(durationTF.getText()));
            ResultSet wid= sel.executeQuery();
            while(wid.next()) {
             Workout.Instance.setWorkoutid(Integer.parseInt(wid.getString("workoutid")));
            }

            DBsession.INSTANCE.OpenConnection().close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        DateSet2();
    }
    public void DateSet2(){
        LocalDate date=LocalDate.now();
//        String SQL_Insert="INSERT INTO day( date, workoutid, calories) Select workoutid,username from Users ON day.dayid=Users.dayid Join workout ON workout.workoutid=day.dayid Where username=? and workoutid=?";   ";
        // String SQL_Insert="INSERT INTO day(date,mealid,workoutid,userid) values";
        //String SQL_Insert="INSERT INTO day(date,workoutid,userid) values(?,(Select workoutid from workout where workouttype=? AND duration=?),?)";
        String SQL_Insert="INSERT INTO day(date,workoutid,userid) values(?,?,?)";
        try {
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Insert);
            pst.setString(1,date.toString());
            pst.setInt(2, Workout.Instance.getWorkoutid());
            pst.setInt(3, User.INSTANCE.getUserid());
            pst.executeUpdate();

            DBsession.INSTANCE.OpenConnection().close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //Update workout
    public void updateWorkout(){
        LocalDate date=LocalDate.now();
        String SQL_Update="Select workoutid FROM workout JOIN day on day.workoutid=workout.workoutid JOIN day.userid=user.userid and username=?";

        try {
            PreparedStatement sel = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Update);
            sel.setInt(1, Integer.parseInt(id.getSelectionModel().getSelectedItem().toString()));
            sel.setString(2,WorkoutTypeSelector.getSelectionModel().getSelectedItem().toString());
            sel.setInt(3, Integer.parseInt(durationTF.getText()));
            ResultSet wid = sel.executeQuery();

            while(wid.next()) {
               Workout.Instance.setWorkoutid(Integer.parseInt(wid.getString("workoutid")));
              // id.
            }

            DBsession.INSTANCE.OpenConnection().close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        DateSet2();
    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){

    }


}

