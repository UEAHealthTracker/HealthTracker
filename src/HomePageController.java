import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class HomePageController extends BaseController {
    ArrayList<String> email=new ArrayList<>();
    ArrayList<String> groupnames=new ArrayList<>();
    private ObservableList<Goal> data;
    @FXML ListView<Goal> listView = new ListView<>();
    public boolean hm=false;
    @FXML  TableView <Goal>  goalview;
    @FXML TableColumn<Goal, String> goalid;
    @FXML TableColumn<Goal, String> goalname;
    @FXML TableColumn<Goal, Date> goaldate;
    @FXML TableColumn<Goal,String> goalstatus;
    @FXML TableColumn<Goal, Integer> goalgroups;
    int items=0;


    public void initialize() throws MessagingException {
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        Check();
        populateGoalsTable();

//SELECT DISTINCT groups.groupname as gn from groups JOIN groupsmember on groupsmember.groupid=groups.groupid
// JOIN Users on Users.userid=groupsmember.userid
// JOIN Goal ON Goal.userid=Users.userid
// JOIN groupgoal on groupgoal.groupid=groups.groupid where Goal.goalname="Complete5thWorkout"


        //Do not remove this comment!!!!!
//        for(String groupname:groupnames){
//            for(String email:email){
//                for(Goal goal:data) {
//                    if(goal.getGoalstatus()=="Complete") {
//                    SendMail.sendGoalCompletationMail(email,groupname,goal.getGoalname(),User.INSTANCE.username);
//                    }
//                }
//            }
//        }



    }
    //add data to goal table
    public void populateGoalsTable(){
        data = FXCollections.observableArrayList();


        String SQL_QUERY = "select goalname,startdate,enddate,Goal.code as code,Goal.goalid as goalid,COUNT(groupgoal.groupgoalid) as total from Goal JOIN Users ON Users.userid=Goal.userid left JOIN groupgoal on Goal.goalid = groupgoal.goalid where Users.userid=? GROUP BY Goal.goalid";
        try {
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setInt(1, User.INSTANCE.getUserid());
            ResultSet rs = pst.executeQuery();
            String status = null;
            while (rs.next()) {
                LocalDate sd = LocalDate.parse(rs.getString("startdate"));
                LocalDate ed = LocalDate.parse(rs.getString("enddate"));
                LocalDate now = LocalDate.now();

                long days = ChronoUnit.DAYS.between(now, ed);
                if (days > 0) {
                    status = "Active";
                    data.add(new Goal(Integer.parseInt(rs.getString("goalid")), rs.getString("goalname"), ed.toString(), status, rs.getString("total")+"/"+items,sd.toString(),rs.getString("code")));
                } else {
                    status = "Complete";
                    getgroupnames(rs.getString("goalname"));
                    data.add(new Goal(Integer.parseInt(rs.getString("goalid")), rs.getString("goalname"), ed.toString(), status, rs.getString("total")+"/"+items,sd.toString(),rs.getString("code")));
                    for(String item:groupnames){
                        getgroupmembers(item,User.INSTANCE.getUserid());
                    }

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
        BaseController.Instance.Switch(actionEvent,"FXML/SelectGoalType.fxml");
    }

    public void openAddGoalPage(javafx.event.ActionEvent actionEvent) throws IOException {
        BaseController.Instance.Switch(actionEvent,"FXML/AddWeightGoal.fxml");
    }
    @FXML
    public void openEditGoalPage(javafx.event.ActionEvent actionEvent) throws IOException {
        BaseController.Instance.Switch(actionEvent,"FXML/EditGoal.fxml");


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

            BaseController.Instance.Switch(actionEvent,"FXML/HomePage.fxml");
        }

    }

    ArrayList getgroupmembers(String groupname, Integer userid){

        String SQL_QUERY="SELECT DISTINCT Users.email as email,Users.userid as userid FROM Users JOIN groupsmember on Users.userid=groupsmember.userid JOIN groups ON groups.groupid=groupsmember.groupid WHERE groups.groupid=(SELECT groupid FROM groups where groupname=?)";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, groupname);
            ResultSet rs=pst.executeQuery();
            while(rs.next()) {
                if(Integer.parseInt(rs.getString("userid"))==userid) {

                }else {

                    email.add(rs.getString("email"));
                }
            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
        return email;
    }
    ArrayList getgroupnames(String goalname){

        String SQL_QUERY="SELECT DISTINCT groups.groupname as gn from groups JOIN groupsmember on groupsmember.groupid=groups.groupid JOIN groupgoal on groupgoal.groupid=groups.groupid JOIN Goal on Goal.goalid=groupgoal.goalid where Goal.goalname=?";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, goalname);
            ResultSet rs=pst.executeQuery();
            while(rs.next()) {
                groupnames.add(rs.getString("gn"));
            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
        return groupnames;
    }

}
