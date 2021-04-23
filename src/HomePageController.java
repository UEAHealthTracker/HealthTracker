import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ResourceBundle;

public class HomePageController extends BaseController{
    private ObservableList<Goal> data;
    @FXML ListView<Goal> listView = new ListView<>();
    public boolean hm=false;
    @FXML Label userLabel;
    @FXML  TableView <Goal>  goalview;
    @FXML TableColumn<Goal, String> goalid;
    @FXML TableColumn<Goal, String> goalname;
    @FXML TableColumn<Goal, Date> goaldate;
    @FXML TableColumn<Goal,String> goalstatus;
    @FXML TableColumn<Goal, Integer> goalgroups;
    @FXML TextField editgoalname;
    @FXML DatePicker editgoaldate;
    @FXML ComboBox editgoalgroup;

    public void initialize() {
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        Check();
        populateGoalsTable();





    }
    //add data to goal table
    public void populateGoalsTable(){
        data = FXCollections.observableArrayList();
        String SQL_QUERY= "select goalname,startdate,enddate,Goal.goalid as goalid,COUNT(groupgoal.groupgoalid) as total from Goal JOIN Users ON Users.userid=Goal.userid left JOIN groupgoal on Goal.goalid = groupgoal.goalid where Users.userid=? GROUP BY Goal.goalid;";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, User.INSTANCE.getUsername().toString());
            ResultSet rs = pst.executeQuery();
            String status=null;
            while(rs.next()) {
               LocalDate sd=LocalDate.parse(rs.getString("startdate"));
                LocalDate ed=LocalDate.parse(rs.getString("enddate"));
//               long days = ed.getTime() - sd.getTime();
                long days = ChronoUnit.DAYS.between(sd, ed);
                if(days>0) {
                    String d=Long.toString(days);
                    status="Incomplete";
                    data.add(new Goal(rs.getString("goalname"),d,status ,Integer.parseInt(rs.getString("goalid"))));
                }else{
                    String d=Long.toString(days);
                    status="Complete";
                    data.add(new Goal(rs.getString("goalname"),d,status ,Integer.parseInt(rs.getString("goalid"))));
                }
            }
//            goalid.setCellValueFactory(new PropertyValueFactory<>("goalid"));
//            goalname.setCellValueFactory(new PropertyValueFactory<>("goalname"));
//            goaldate.setCellValueFactory(new PropertyValueFactory<>("goaldate"));
//            goalstatus.setCellValueFactory(new PropertyValueFactory<>("goalstatus"));
//            goalgroups.setCellValueFactory(new PropertyValueFactory<>("goalgroups"));
//            goalview.setItems(data);

            listView.setItems(data);
            listView.setCellFactory(param -> new GoalCell());



            DBsession.INSTANCE.OpenConnection().close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }



    public void Check(){

        String SQL_query="select groups.groupname as gn from groups JOIN groupsmember on groups.groupid=groupsmember.groupid JOIN Users on Users.userid=groupsmember.userid where Users.userid=?";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_query);
            pst.setInt(1, User.INSTANCE.getUserid());
            ResultSet rs=pst.executeQuery();
            while(rs.next()) {
                items++;

            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
    }



    public void openSelectGoalTypePage(javafx.event.ActionEvent actionEvent) throws IOException {
        BaseController.Instance.Switch(actionEvent,"SelectGoalType.fxml");
    }

    public void openAddGoalPage(javafx.event.ActionEvent actionEvent) throws IOException {
        BaseController.Instance.Switch(actionEvent,"AddWeightGoal.fxml");
    }
    @FXML
    public void openEditGoalPage(javafx.event.ActionEvent actionEvent) throws IOException {
        BaseController.Instance.Switch(actionEvent,"EditGoal.fxml");


    }

    public void onEdit(javafx.event.ActionEvent actionEvent) throws IOException {

            if (listView.getSelectionModel().getSelectedItem() != null) {
                Goal selectedGoal = listView.getSelectionModel().getSelectedItem();
                Goal.Instance.setGoalid(selectedGoal.getGoalid());
                openEditGoalPage(actionEvent);

            }
    }
    //allow user to select a table item/row and delete it using the delete button
    public void onDelete(javafx.event.ActionEvent actionEvent) throws IOException{
        if (listView.getSelectionModel().getSelectedItem() != null) {
            Goal selectedGoal = listView.getSelectionModel().getSelectedItem();
            Goal.Instance.setGoalid(selectedGoal.getGoalid());
            String SQL_query="DELETE FROM Goal WHERE goalid=?;";
            try{
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_query);
                pst.setInt(1, Goal.Instance.getGoalid());
                pst.executeUpdate();
                DBsession.INSTANCE.OpenConnection().close();
            }catch(Exception e){System.out.println(e);}

            BaseController.Instance.Switch(actionEvent,"HomePage.fxml");
        }

    }


}
