import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

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

    @FXML TableView workoutTableView;
    @FXML TableColumn<Workout,Integer> workoutid ;
    @FXML TableColumn<Workout,Integer> caloriesBurned ;
    @FXML TableColumn<Workout,Integer> durationMinutes;
    @FXML TableColumn<Workout,String> workoutTypestr ;


    ArrayList<Integer> calories= new ArrayList<>();
    @FXML CategoryAxis xAxis;
    //xAxis.setLabel("Devices");
    @FXML NumberAxis yAxis;
   // yAxis.setLabel("Visits");

    @FXML  BarChart <String,Number>  barChart;


    ObservableList<Workout> workouts = FXCollections.observableArrayList();
    ArrayList<String> fiveDays = new ArrayList<>();
    public void initialize(){
        userLabel.setText("Hello "+User.INSTANCE.getUsername());

        populateTable();


      //  barChart("2021-05-07");
        for(String day : days()){
            barChart(day);
        }
        XYChart.Series <String,Number> dataSeries1 = new XYChart.Series();
        Iterator  numbers = calories.iterator();
        Iterator days = fiveDays.iterator();
        while(numbers.hasNext() && days.hasNext()){

            dataSeries1.getData().add(new XYChart.Data(days.next(), numbers.next()));
                  // System.out.println(numbers);

        }
        barChart.getData().add(dataSeries1);
        //System.out.println("this is a test ");

    }

    public void populateTable(){
        workouts = FXCollections.observableArrayList();
        System.out.println("Here");
        String SQL_SELECT=" Select workout.workoutid as WorkoutID, workout.calories as Calories, workout.duration as Duration, workout.workouttype as WorkoutType FROM workout JOIN day ON day.workoutid=workout.workoutid JOIN Users ON day.userid=Users.userid and Users.userid=?";
        try {
            PreparedStatement seltb = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_SELECT);
            seltb.setInt(1,User.INSTANCE.getUserid());
            ResultSet wid = seltb.executeQuery();
            System.out.println(wid);
            while(wid.next()){
                System.out.println(wid);
                workouts.add(new Workout(wid.getInt("WorkoutID"), wid.getInt("Calories"), wid.getInt("Duration"), wid.getString("WorkoutType")));
            }
            workoutid.setCellValueFactory(new PropertyValueFactory<>("workoutid"));
            caloriesBurned.setCellValueFactory(new PropertyValueFactory<>("caloriesBurned"));
            durationMinutes.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
            workoutTypestr.setCellValueFactory(new PropertyValueFactory<>("workoutTypestr"));
            workoutTableView.setItems(workouts);
            DBsession.INSTANCE.OpenConnection().close();
        } catch (SQLException throwables ) {
            throwables.printStackTrace();
        }
    }

    public void onEdit(javafx.event.ActionEvent actionEvent) throws IOException {
        if (workoutTableView.getSelectionModel().getSelectedItem() != null) {
            Workout selectedWorkout = (Workout) workoutTableView.getSelectionModel().getSelectedItem();
            Workout.Instance.setWorkoutid(selectedWorkout.getWorkoutid());
            BaseController.Instance.Switch(actionEvent,"FXML/EditWorkoutPage.fxml");
        }
    }

    public void onDelete(javafx.event.ActionEvent actionEvent) throws IOException{
        if (workoutTableView.getSelectionModel().getSelectedItem() != null) {
            Workout selectedItem = (Workout) workoutTableView.getSelectionModel().getSelectedItem();
            Workout.Instance.setWorkoutid(selectedItem.getWorkoutid());
            String SQL_query="delete from workout Where workoutid=? " ;
            try{
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_query);
                pst.setInt(1, Workout.Instance.getWorkoutid());
                pst.executeUpdate();
                DBsession.INSTANCE.OpenConnection().close();
            }catch(Exception e){System.out.println(e);}
            onDelete2(actionEvent);
            BaseController.Instance.Switch(actionEvent,"FXML/HomePage.fxml");

        }

    }

    public void onDelete2(javafx.event.ActionEvent actionEvent) throws IOException{
            String SQL_query="delete from day Where workoutid=?" ;
            try{
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_query);
                pst.setInt(1, Workout.Instance.getWorkoutid());
                pst.executeUpdate();
                DBsession.INSTANCE.OpenConnection().close();
            }catch(Exception e){System.out.println(e);}
        }

    //add data to workout table
    public void barChart(String date){
        String SQL_Select="SELECT SUM(calories) AS cal, date from workout join day on day.workoutid=workout.workoutid join Users on Users.userid=day.userid where Users.userid =? and date=? ";


        try {
            PreparedStatement sel = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Select);
            sel.setInt(1,User.INSTANCE.getUserid());
            sel.setString(2,date);

            ResultSet wid = sel.executeQuery();

            while(wid.next()){
                calories.add(wid.getInt("cal"));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }




    }
    public ArrayList<String> days(){
        LocalDate now = LocalDate.now();

        for(int i = 0; i < 5; i++){
            LocalDate dayBefore = now.minusDays(i);
            fiveDays.add(dayBefore.toString());

        }
        return fiveDays;
    }



    public void logOut(javafx.event.ActionEvent actionEvent) throws IOException {
        int logoutOpt = JOptionPane.showConfirmDialog(null,"Are you sure you want to Log out?");
        if(logoutOpt==JOptionPane.YES_OPTION){
            BaseController.Instance.Switch(actionEvent,"FXML/LoginPage.fxml");
        }
    }


}

