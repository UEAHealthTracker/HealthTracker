
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GoalCell extends ListCell<Goal> {
    FXMLLoader mLLoader;
    @FXML
    AnchorPane ap;
    @FXML
    Label StatusLbl;
    @FXML
    Label NameLbl;
    @FXML
    Label DateLbl;
    @FXML
    Label GroupsLbl;
    @FXML
    ProgressBar Progress;
    @FXML
    ProgressBar Progress1;
    @FXML Button cbtn;
    boolean complete=false;
//    public static final GoalCell Instance= new GoalCell();
    public float percent=0;

    @Override
    protected void updateItem(Goal item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setContentDisplay(null);
        }
        else {
            if ( mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("FXML/ListCellHM.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            LocalDate sd=LocalDate.parse(item.getGoalstartdate());
            LocalDate now = LocalDate.now();
            LocalDate ed = LocalDate.parse(item.getGoaldate());
            StatusLbl.setText(item.getGoalstatus());
            NameLbl.setText(item.getGoalname());
            DateLbl.setText(item.getGoaldate());
            GroupsLbl.setText("Groups: "+item.getGoalgroups());
            NameLbl.setStyle("-fx-text-fill: Black;-fx-font-weight: bold");

            if(item.getGoalstatus()=="Complete"){
                cbtn.setVisible(false);
                StatusLbl.setStyle("-fx-text-fill: #8D0000;-fx-font-weight: bold");
                Progress.setProgress(1);
                Progress.setId("completed");

            }else if(item.getGoalstatus()=="Active"){
                    StatusLbl.setStyle("-fx-text-fill: green;-fx-font-weight: bold");
                    percent = 100 * ChronoUnit.DAYS.between(sd, now) / ChronoUnit.DAYS.between(ed, sd);
                    Progress1.setProgress(percent / 100);
                    percent = 100 * ChronoUnit.DAYS.between(now, sd) / ChronoUnit.DAYS.between(ed, sd);
                    Progress.setProgress(percent / 100);
                    cbtn.setOnAction(e -> {
                        cbtn.setVisible(false);
                        StatusLbl.setStyle("-fx-text-fill: #8D0000;-fx-font-weight: bold");
                        Progress.setProgress(1);
                        Progress.setId("completed");
                        complete=true;

                        String SQL_QUERY="UPDATE Goal set enddate=? where goalname=?";
                        try{
                            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
                            pst.setString(1, String.valueOf(now));
                            pst.setString(2, NameLbl.getText());
                            pst.executeUpdate();

                            DBsession.INSTANCE.OpenConnection().close();
                        }catch(Exception exept){System.out.println(exept);}

                    });
            }
           // Progress.setProgress(0.5);

            setText(null);
            setGraphic(ap);
        }

    }





}



