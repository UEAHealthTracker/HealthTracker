import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class WorkoutGoalController extends BaseController {

    @FXML
    Label userLabel;
    @FXML
    DatePicker enddate;
    @FXML
    TextField GoalNameTF;
    public void initialize() {

        userLabel.setText("Hello "+User.INSTANCE.getUsername());


    }

    public void AddWorkoutGoal(javafx.event.ActionEvent actionEvent) throws IOException {
        LocalDate now = LocalDate.now();
        //TODO Complete query with group exending test
        String SQL_QUERY="INSERT into Goal (goalname,enddate,userid,startdate,goaltype) values (?,?,?,?,?)";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            //    if(Check()==true) {
            pst.setString(1, GoalNameTF.getText());
            pst.setString(2, String.valueOf(enddate.getValue()));
            pst.setInt(3, User.INSTANCE.getUserid());
            pst.setString(4, String.valueOf(now));
            pst.setString(5, "Complex");
            ;
            pst.executeUpdate();
//            }else{ thread.start();
//                label.setText("");
//            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
//        if (Check() == true) {
        BaseController.Instance.Switch(actionEvent,"HomePage.fxml");
        //  }
    }

}
