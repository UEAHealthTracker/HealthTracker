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
        LocalDate sd=LocalDate.parse(item.getGoalstartdate());
        LocalDate now = LocalDate.now();
        LocalDate ed = LocalDate.parse(item.getGoaldate());

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
            StatusLbl.setText(item.getGoalstatus());
            NameLbl.setText(item.getGoalname());
            DateLbl.setText(item.getGoaldate());
            GroupsLbl.setText(item.getGoalgroups());
            //=(B2 - TODAY()) / (B2 - A2)
            if(item.getGoalstatus()=="Complete"){
                Progress.setProgress(1);

            }else if(item.getGoalstatus()=="Active"){
                float percent=ChronoUnit.DAYS.between(ed,now)/ChronoUnit.DAYS.between(ed,sd);
                Progress.setProgress(percent);
            }

            setText(null);
            setGraphic(ap);
        }
    }



}



