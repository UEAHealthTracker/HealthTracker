import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class HomePageController extends BaseController {
    ArrayList<String> email=new ArrayList<>();
    ArrayList<String> groupnames=new ArrayList<>();
    private ObservableList<Goal> data;
    private ObservableList<Goal> completeGoals;
    @FXML ListView<Goal> listView = new ListView<>();
    public boolean hm=false;
    @FXML  TableView <Goal>  goalview;
    @FXML TableColumn<Goal, String> goalid;
    @FXML TableColumn<Goal, String> goalname;
    @FXML TableColumn<Goal, Date> goaldate;
    @FXML TableColumn<Goal,String> goalstatus;
    @FXML TableColumn<Goal, Integer> goalgroups;
    @FXML Label msglbl;
    int items=0;
    int goalsmet=0;
    @FXML Button share;
    @FXML  ImageView imshare;



    public void initialize() throws MessagingException {
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        Check();
        populateGoalsTable();
        if(goalsmet>0) {
            msglbl.setVisible(true);
            share.setVisible(true);
            imshare.setVisible(true);
            msglbl.setText("You have:" + goalsmet + " completed goal/'s");
        }else{
            msglbl.setVisible(false);
            share.setVisible(false);
            imshare.setVisible(false);
        }




    }


    //add data to goal table
    public void populateGoalsTable(){
        data = FXCollections.observableArrayList();
        completeGoals = FXCollections.observableArrayList();


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
                    goalsmet++;
                    status = "Complete";
                    getgroupnames(rs.getString("goalname"));
                    data.add(new Goal(Integer.parseInt(rs.getString("goalid")), rs.getString("goalname"), ed.toString(), status, rs.getString("total")+"/"+items,sd.toString(),rs.getString("code")));
                    completeGoals.add(new Goal(Integer.parseInt(rs.getString("goalid")), rs.getString("goalname"), ed.toString(), status, rs.getString("total")+"/"+items,sd.toString(),rs.getString("code")));
                    for(String item:groupnames){
                        getgroupmembers(item,User.INSTANCE.getUserid());
                    }

                }
            }

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



    public void ShareGoal() throws MessagingException {
        msglbl.setVisible(false);
        share.setVisible(false);
        imshare.setVisible(false);
        Iterator iterator=groupnames.iterator();
        Iterator emailiterator=email.iterator();
        while(iterator.hasNext()&&emailiterator.hasNext() ) {
            while (emailiterator.hasNext()) {
                for (Goal goal : completeGoals) {
                    if(emailiterator.hasNext()==true) {
                        SendMail.sendGoalCompletationMail((String) emailiterator.next(), (String) iterator.next(), goal.getGoalname(), User.INSTANCE.username);

                    }else{break;}
                }
            }
        }

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
//                    if(!email.contains(rs.getString("email")))
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
                if(!groupnames.contains(rs.getString("gn")))
                groupnames.add(rs.getString("gn"));
            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
        return groupnames;
    }

}
