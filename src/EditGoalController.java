import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class EditGoalController extends BaseController{
    @FXML
    TextField editgoalname;
    @FXML
    DatePicker editgoaldate;
    @FXML
    ComboBox editgoalgroup;
    @FXML
    Label userLabel;

    public void initialize() {

        userLabel.setText("Hello " + User.INSTANCE.getUsername());


        String SQL_QUERY="select goalname,enddate,groupgoal.groupgoalid from Goal JOIN  groupgoal on Goal.goalid=groupgoal.goalid where Goal.goalid=?";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setInt(1, Goal.Instance.getGoalid());
            ResultSet rs=pst.executeQuery();
            while(rs.next()) {
        //        System.out.println(rs.getString("goalname"));
          //      System.out.println(rs.getString("enddate"));

               editgoalname.setText(rs.getString("goalname"));
                editgoaldate.setValue(LocalDate.parse(rs.getString("enddate")));
               // editgoalgroup.getSelectionModel();
            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){ System.out.println(e);}
    }
}
