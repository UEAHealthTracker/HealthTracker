import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserPageController extends BaseController{
    @FXML Label name;
    @FXML Label email;
    @FXML Label height;
    @FXML Label weight;
    @FXML Label bmi;
    @FXML Label bmilabel;

    /**
     * Initialize all the fiels in the UserPage with current information taken from the User class
     */
    public void initialize() {

            userLabel.setText("Hello " + User.INSTANCE.getUsername());
            DecimalFormat df = new DecimalFormat("#.00");
            double var = (User.INSTANCE.getWeight() / (User.INSTANCE.getHeight() * User.INSTANCE.getHeight())) * 10000;
            name.setText("Name: " + User.INSTANCE.getUsername());
            email.setText("Email: " + User.INSTANCE.getEmail());
            height.setText("Height: " + User.INSTANCE.getHeight());
            weight.setText("Weight: " + User.INSTANCE.getWeight());
            bmi.setText("BMI: " + df.format(var));
            bmilabel.setUnderline(true);
            if (var < 18.5) {
                bmilabel.setText("UNDERWEIGHT");
            } else if (var > 18.5 && var < 24.9) {
                bmilabel.setText("NORMAL WEIGHT");
            } else if (var > 25.0 && var < 29.9) {
                bmilabel.setText("OVERWEIGHT");
            } else {
                bmilabel.setText("OBESE");
            }

    }

    public void activityLog(ActionEvent actionEvent){
        String SQL_QUERY = "SELECT date, workouttype, duration FROM day, workout WHERE day.date=? AND day.workoutid = workout.workoutid";

        ArrayList<String> workouts = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();

        try {
            //String groupQuery="select groups.groupname from groups INNER JOIN groupsmember ON groups.groupid=groupsmember.groupid where groupsmember.userid=?";
            PreparedStatement pst2= DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            LocalDate now = LocalDate.now();
            pst2.setString(1, now.toString());
            ResultSet rs2 = pst2.executeQuery();
            while(rs2.next()) {
                String date = rs2.getString("date");
                String workouttype = rs2.getString("workouttype");
                String duration = rs2.getString("duration");
                System.out.println(date + workouttype);
                workouts.add(workouttype);
                durations.add(duration);
            }
            DBsession.INSTANCE.OpenConnection().close();

        }catch (SQLException e){
            e.printStackTrace();
        }
        String SQL_QUERY2 = "SELECT date, itemname FROM day, dietitem, mealitem WHERE day.date=? AND day.mealid = mealitem.mealid AND mealitem.itemid = dietitem.itemid";
        try {
            //String groupQuery="select groups.groupname from groups INNER JOIN groupsmember ON groups.groupid=groupsmember.groupid where groupsmember.userid=?";
            PreparedStatement pst2= DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY2);
            LocalDate now = LocalDate.now();
            pst2.setString(1, now.toString());
            ResultSet rs2 = pst2.executeQuery();
            while(rs2.next()) {
                String date = rs2.getString("date");
                String itemname = rs2.getString("itemname");
                System.out.println(date + itemname);
            }
            DBsession.INSTANCE.OpenConnection().close();

        }catch (SQLException e){
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Activity Log");
        alert.setHeaderText("Activities for the day : " + LocalDate.now());
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < workouts.size(); i++){
            stringBuilder.append(workouts.get(i) + " for ");
            stringBuilder.append(durations.get(i) + " minutes");
            stringBuilder.append("\n");
            stringBuilder.append("\n");
        }
        alert.setContentText(stringBuilder.toString());

        alert.showAndWait();
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        int logoutOpt = JOptionPane.showConfirmDialog(null,"Are you sure you want to Log out?");
        if(logoutOpt==JOptionPane.YES_OPTION){
            BaseController.Instance.Switch(actionEvent,"FXML/LoginPage.fxml");
        }
    }
}
