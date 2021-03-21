import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class HomePageController extends BaseController {
    private ObservableList<Goal> data;

    private TableView goalsTable;
    @FXML Label userLabel;
    @FXML  TableView <Goal>  goalview;
    @FXML TableColumn<Goal, String> goalname;
    @FXML TableColumn<Goal, Date> goaldate;
    @FXML TableColumn<Goal,String> goalstatus;
    @FXML TableColumn<Goal, Integer> goalgroups;
    @FXML TextField editgoalname;
    @FXML DatePicker editgoaldate;
    @FXML ComboBox editgoalgroup;
    public void initialize() {
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat edate = new SimpleDateFormat("yyyy-MM-dd");
        data = FXCollections.observableArrayList();
        String SQL_QUERY="select goalname,startdate,enddate,Users.goalid from Users JOIN Goal ON Users.goalid=Goal.goalid and username=?;";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, User.INSTANCE.getUsername().toString());
            ResultSet rs=pst.executeQuery();
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
            goalname.setCellValueFactory(new PropertyValueFactory<>("goalname"));
            goaldate.setCellValueFactory(new PropertyValueFactory<>("goaldate"));
            goalstatus.setCellValueFactory(new PropertyValueFactory<>("goalstatus"));
            goalgroups.setCellValueFactory(new PropertyValueFactory<>("goalgroups"));
            goalview.setItems(data);
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){ System.out.println(e);}
    }
    //add data to goal table
    public void populateGoalsTable(){

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
        //if user selects weight goal then open AddWeightGoal page
        //else if user selects workout goal then open AddWorkoutGoal page
        root = FXMLLoader.load(getClass().getResource("AddWeightGoal.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openEditGoalPage(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("EditGoal.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onEdit(javafx.event.ActionEvent actionEvent) throws IOException {
        if (goalview.getSelectionModel().getSelectedItem() != null) {
            Goal selectedGoal = goalview.getSelectionModel().getSelectedItem();
            editgoalname.setText(selectedGoal.getGoalname());
            editgoaldate.setValue(LocalDate.parse(selectedGoal.goaldate));
            openAddGoalPage(actionEvent);
        }
    }
}
