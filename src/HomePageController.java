import com.sun.media.jfxmedia.events.PlayerEvent;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import javax.xml.soap.Text;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class HomePageController extends BaseController {
    private ObservableList<Goal> data;

    public boolean hm=false;
    @FXML Label userLabel;
    @FXML  TableView <Goal>  goalview;
    @FXML TableColumn<Goal, String> goalid;
    @FXML TableColumn<Goal, String> goalname;
    @FXML TableColumn<Goal, Date> goaldate;
    @FXML TableColumn<Goal,String> goalstatus;
    @FXML TableColumn<Goal, Integer> goalgroups;

    TextField en;

    public void initialize() {

        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        populateGoalsTable();


    }
    //add data to goal table
    public void populateGoalsTable(){
        SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat edate = new SimpleDateFormat("yyyy-MM-dd");
        data = FXCollections.observableArrayList();
        String SQL_QUERY = "select goalname,startdate,enddate,Goal.goalid,groupgoalid from Goal JOIN Users ON Users.userid=Goal.userid JOIN groupgoal on Goal.goalid = groupgoal.goalid where Users.userid=?;";
        try {
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, User.INSTANCE.getUserid().toString());
            ResultSet rs = pst.executeQuery();
            String status = null;
            while (rs.next()) {
                LocalDate sd = LocalDate.parse(rs.getString("startdate"));
                LocalDate ed = LocalDate.parse(rs.getString("enddate"));
                // Goal.Instance.setGoalid(Integer.parseInt(rs.getString("goalid")));
//               long days = ed.getTime() - sd.getTime();
//                long days = ChronoUnit.DAYS.between(sd, ed);
//                if(days>0) {
//                    String d=Long.toString(days);
//                    status="Incomplete";
//                    data.add(new Goal(rs.getString("goalname"),d,status ,Integer.parseInt(rs.getString("goalid"))));
//                }else{
//                    String d=Long.toString(days);
//                    status="Complete";
//                    data.add(new Goal(rs.getString("goalname"),d,status ,Integer.parseInt(rs.getString("goalid"))));
//                }
                LocalDate now = LocalDate.now();
                long days = ChronoUnit.DAYS.between(now, ed);
                if (days > 0) {
                    status = "Active";
                    data.add(new Goal(Integer.parseInt(rs.getString("goalid")), rs.getString("goalname"), ed.toString(), status, Integer.parseInt(rs.getString("groupgoalid"))));
                } else {
                    status = "Expired";
                    data.add(new Goal(Integer.parseInt(rs.getString("goalid")), rs.getString("goalname"), ed.toString(), status, Integer.parseInt(rs.getString("groupgoalid"))));
                }
            }
            goalid.setCellValueFactory(new PropertyValueFactory<>("goalid"));
            goalname.setCellValueFactory(new PropertyValueFactory<>("goalname"));
            goaldate.setCellValueFactory(new PropertyValueFactory<>("goaldate"));
            goalstatus.setCellValueFactory(new PropertyValueFactory<>("goalstatus"));
            goalgroups.setCellValueFactory(new PropertyValueFactory<>("goalgroups"));
            goalview.setItems(data);
            DBsession.INSTANCE.OpenConnection().close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){

    }

    public void openSelectGoalTypePage(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SelectGoalType.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openAddGoalPage(javafx.event.ActionEvent actionEvent) throws IOException {
        BaseController.Instance.Switch(actionEvent,"AddWeightGoal.fxml");
    }
    @FXML
    public void openEditGoalPage(javafx.event.ActionEvent actionEvent) throws IOException {
        BaseController.Instance.Switch(actionEvent,"EditGoal.fxml");


    }

    public void onEdit(javafx.event.ActionEvent actionEvent) throws IOException {

            if (goalview.getSelectionModel().getSelectedItem() != null) {
                Goal selectedGoal = goalview.getSelectionModel().getSelectedItem();
                Goal.Instance.setGoalid(selectedGoal.getGoalid());
                openEditGoalPage(actionEvent);

            }
    }

}
