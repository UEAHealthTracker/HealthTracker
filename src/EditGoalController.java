import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Iterator;

public class EditGoalController extends BaseController{
    @FXML
    TextField editgoalname;
    @FXML
    DatePicker editgoaldate;
    @FXML
    ComboBox editgoalgroup;

    int items=0;

    public void initialize() {
        userLabel.setText("Hello " + User.INSTANCE.getUsername());

        String SQL_query="select groups.groupname as gn from groups JOIN groupsmember on groups.groupid=groupsmember.groupid JOIN Users on Users.userid=groupsmember.userid where Users.userid=?";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_query);
            pst.setInt(1, User.INSTANCE.getUserid());
            ResultSet rs=pst.executeQuery();
            while(rs.next()) {
                editgoalgroup.getItems().add(rs.getString("gn"));
                items++;

            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
        Check();
    }
    public void Check(){
        int i=0;
        String SQL_QUERY="select goalname,enddate,groups.groupname as gn from Goal left join groupgoal on groupgoal.goalid=Goal.goalid left JOIN groups on groups.groupid=groupgoal.groupid where Goal.goalid=?";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setInt(1, Goal.Instance.getGoalid());
            ResultSet rs=pst.executeQuery();
            while(rs.next()) {
                editgoalname.setText(rs.getString("goalname"));
                editgoaldate.setValue(LocalDate.parse(rs.getString("enddate")));
                ObservableList<String> items = editgoalgroup.getItems();
                Iterator<String> iterator = items.iterator();
                while(iterator.hasNext()){
                    String next = iterator.next();
                    if(next.equals(rs.getString("gn"))){
                        iterator.remove();
                    }
                }

            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
    }


    public void Update(javafx.event.ActionEvent actionEvent) throws IOException {
        //TODO Complete query with group exending test
        String SQL_QUERY="UPDATE Goal set goalname=?, enddate=? where goalid=?";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, editgoalname.getText());
            pst.setString(2, String.valueOf(editgoaldate.getValue()));
            pst.setInt(3, Goal.Instance.getGoalid());
            pst.executeUpdate();
            if(!editgoalgroup.getSelectionModel().isEmpty()){
                String SQL_query="Insert into groupgoal (goalid,groupid) values(?,(Select groupid from groups where groupname=?))";
                PreparedStatement ps = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_query);
                ps.setInt(1, Goal.Instance.getGoalid());
                ps.setString(2,editgoalgroup.getSelectionModel().getSelectedItem().toString());
                ps.executeUpdate();
            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
        Instance.Switch(actionEvent, "FXML/HomePage.fxml");

    }
}