import javafx.collections.transformation.TransformationList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddWorkoutController  extends BaseController{

    @FXML
    ComboBox WorkoutTypeSelector;
    @FXML
    TextField durationTF;
    @FXML TextField duration2;
    @FXML ComboBox WorkoutTypeSelector2;
    @FXML ComboBox ID;
    @FXML
    Label workoutTypelable;
    private Parent root;
    private Stage stage;
    private Scene scene;
    boolean open=false;
    public void init(){
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        if(open==false) {
            for (WorkoutType type : WorkoutType.values()) {
                WorkoutTypeSelector.getItems().add(type.ID);
            }
            open=true;
        }
    }

    public void addMessage(String message, String title){
        JOptionPane.showMessageDialog(null, message, title,JOptionPane.INFORMATION_MESSAGE);
    }

    public void populateWorkoutTable(ActionEvent event) throws IOException {
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
        switchToAddworkout(event);
    }

    public void switchToAddworkout(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("FXML/WorkoutPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
}


