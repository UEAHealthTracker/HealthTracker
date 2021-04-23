import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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

public class WorkoutPageController extends BaseController {
    public final static  WorkoutPageController Instance= new WorkoutPageController();


    private static Button addWorkoutbtn;
   // private static final String SQL_Insert ="INSERT INTO workout (workoutid, sets, reps, calories, weekday) VALUES ('4','3','20','150 calories','Wednesday')";
    @FXML ComboBox WorkoutTypeSelector;
    @FXML TextField durationTF;
    @FXML TextField duration2;
    @FXML ComboBox WorkoutTypeSelector2;
    @FXML ComboBox ID;
    @FXML Label workoutTypelable;

    @FXML TableView workoutTableView;
    @FXML TableColumn<Workout,Integer> workoutid ;
    @FXML TableColumn<Workout,Integer> caloriesBurned ;
    @FXML TableColumn<Workout,Integer> durationMinutes;
    @FXML TableColumn<Workout,String> workoutTypestr ;

    ObservableList<Workout> workouts = FXCollections.observableArrayList();

    public void initialize(){
        workouts = FXCollections.observableArrayList();
        String SQL_SELECT="Select workout.workoutid as WorkoutID, workout.calories as Calories, workout.duration as Duration, workout.workouttype as WorkoutType " +
                "FROM workout JOIN day ON day.workoutid=workout.workoutid JOIN Users ON day.userid=Users.userid and Users.userid=?";
        try {
            PreparedStatement seltb = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_SELECT);
            seltb.setInt(1,User.INSTANCE.getUserid());
            ResultSet wid = seltb.executeQuery();
            while(wid.next()){
               /* workouts.add(new Workout(Integer.parseInt(wid.getString("WorkoutID"),
                        Integer.parseInt(wid.getString("Calories"),
                                Integer.parseInt(wid.getString("Duration"),
                                        wid.getString("WorkoutType"))))));
*/
                workouts.add(new Workout(Integer.parseInt(wid.getString("WorkoutID")), Integer.parseInt( wid.getString("Calories")),Integer.parseInt( wid.getString("Duration")), wid.getString("WorkoutType")));

                /*workouts.add(new Workout(String.valueOf(Integer.parseInt(wid.getString("WorkoutID"),Integer.parseInt(wid.getString("Calories"),
                       Integer.parseInt(wid.getString("Duration"),
                               Integer.parseInt(String.valueOf(Integer.parseInt(wid.getString("WorkoutType"))))))))));*/
            }
            workoutid.setCellValueFactory(new PropertyValueFactory<>("workoutid"));
            caloriesBurned.setCellValueFactory(new PropertyValueFactory<>("caloriesBurned"));
            durationMinutes.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
            workoutTypestr.setCellValueFactory(new PropertyValueFactory<>("workoutTypestr"));
            workoutTableView.setItems(workouts);
            DBsession.INSTANCE.OpenConnection().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    /*public void initialize(){
        String SQL_Select = " Select workout.workoutid as WorkoutID, workout.calories as Calories, workout.duration as Duration, workout.workouttype as WorkoutType FROM workout JOIN day ON day.workoutid=workout.workoutid JOIN Users ON day.userid=Users.userid and Users.userid=1";
       // String SQL_Select="Select workout.workoutid as WorkoutID, workout.calories as Calories, workout.duration as Duration, workout.workouttype as WorkoutType FROM workout JOIN day ON day.workoutid=workout.workoutid JOIN Users ON day.userid=Users.userid and Users.userid=?";
        //Select workout.workoutid as WorkoutID, workout.calories as Calories, workout.duration as Duration, workout.workouttype as WorkoutType FROM workout
        // JOIN day ON day.workoutid=workout.workoutid JOIN Users ON day.userid=Users.userid and Users.userid=1
        try {
            PreparedStatement selTB = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Select);
            selTB.setInt(1, Integer.parseInt(String.valueOf( User.INSTANCE.getUserid())));
            ResultSet wid = selTB.executeQuery();
            while(wid.next()){
                workouts.add(new Workout(Integer.parseInt(wid.getString("WorkoutID"),Integer.parseInt(wid.getString("Calories"),
                        Integer.parseInt(wid.getString("Duration"),Integer.parseInt(wid.getString("WorkoutType")))))));
            }
            workoutidTb.setCellValueFactory(new PropertyValueFactory<>("workoutidtb"));
            caloriesTb.setCellValueFactory(new PropertyValueFactory<>("caloriestb"));
            durationTb.setCellValueFactory(new PropertyValueFactory<>("durationtb"));
            workoutTypeTb.setCellValueFactory(new PropertyValueFactory<>("workoutTypetb"));
            workoutTable.setItems(workouts);
            DBsession.INSTANCE.OpenConnection().close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }*/
    /* workouts.add(new Workout (Integer.parseInt(wid.getString("workoutid"))));
                   workouts.add(new Workout (Integer.parseInt(wid.getString("calories"))));
                   workouts.add(new Workout (Integer.parseInt(wid.getString("duration"))));
                  ;*/
    boolean workoutidbool;
    boolean open=false;

    public void addMessage(String message, String title){
        JOptionPane.showMessageDialog(null, message, title,JOptionPane.INFORMATION_MESSAGE);
    }

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
          int msg =   pst.executeUpdate();


            DBsession.INSTANCE.OpenConnection().close();
            if(msg==1){
                addMessage("Workout Successfully Added!","WORKOUT ADDED");
            }
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

    public void fillID(){
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
    }
    public void selectWorkoutType(){
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
                // WorkoutTypeSelector2.setValue(workoutType);
                duration2.setText(String.valueOf(duration));
               // WorkoutTypeSelector.setText(workoutType); //  String.valueOf(duration);  //durationTF.getText(duration);*/    //durationTF.getText(); Workout.Instance.setWorkoutid(Integer.parseInt(wid.getString("workoutid")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){

    }


}

