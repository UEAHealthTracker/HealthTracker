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


        //String SQL_QUERY="select goalname,enddate,groupgoal.groupgoalid from Goal JOIN  groupgoal on Goal.goalid=groupgoal.goalid where Goal.goalid=?";
        //String SQL_QUERY="select goalname,enddate,groupgoal.groupgoalid, groupsmember.groupid ,groups.groupname from Goal JOIN groupgoal on Goal.goalid=groupgoal.goalid Join Users on Users.userid=Goal.userid  LEFT JOIN groupsmember on groupsmember.userid=Users.userid LEFT JOIN groups on groups.groupid=groupsmember.groupmemberid where Goal.goalid=?";
       String SQL_QUERY="select goalname,enddate from Goal JOIN where Goal.goalid=?";
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
