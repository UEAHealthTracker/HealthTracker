import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Random;

public class WorkoutGoalController extends BaseController {

    @FXML
    DatePicker enddate;
    @FXML
    TextField GoalNameTF;
    @FXML Label msglbl;

    /**
     * Initialize user label
     */
    public void initialize() {
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
    }

    /**
     * Add custom goal to the database with a code generated
     * @param actionEvent
     * @throws IOException
     */
    public void AddWorkoutGoal(javafx.event.ActionEvent actionEvent) throws IOException {
        LocalDate now = LocalDate.now();
        String SQL_QUERY="INSERT into Goal (goalname,enddate,userid,startdate,goaltype,code) values (?,?,?,?,?,?)";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, GoalNameTF.getText());
            pst.setString(2, String.valueOf(enddate.getValue()));
            pst.setInt(3, User.INSTANCE.getUserid());
            pst.setString(4, String.valueOf(now));
            pst.setString(5, "Complex");
            pst.setString(6,getSaltString());
            pst.executeUpdate();
            if(GoalNameTF.getText()==""|| String.valueOf(enddate.getValue())==""){
                msglbl.setVisible(true);
            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
        Instance.Switch(actionEvent, "FXML/HomePage.fxml");
    }

    /**
     * Alphanumeric generator function which returns a string credits to the owner
     */
    //https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

}
