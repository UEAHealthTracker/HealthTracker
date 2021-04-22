import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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



    @Override
    protected void updateItem(Goal item, boolean empty) {
        super.updateItem(item, empty);


        if(empty || item == null) {
            setText(null);
            setContentDisplay(null);
        }
        else {
            if ( mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("ListCellHM.fxml"));
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
                StatusLbl.setStyle("-fx-text-fill: #8D0000;-fx-font-weight: bold");
                Progress.setProgress(1);
                Progress.setId("completed");

            }else if(item.getGoalstatus()=="Active"){
                StatusLbl.setStyle("-fx-text-fill: green;-fx-font-weight: bold");
                float percent=100*ChronoUnit.DAYS.between(now,sd)/ChronoUnit.DAYS.between(ed,sd);
                Progress.setProgress(percent/100);
            }
           // Progress.setProgress(0.5);

            setText(null);
            setGraphic(ap);
        }
    }



}



