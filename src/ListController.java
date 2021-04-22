import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ListController extends ListCell<Goal> {
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
    public ListController() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task_cell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void updateItem(Goal item, boolean empty) {
        super.updateItem(item, empty);
        LocalDate sd=LocalDate.parse(item.getGoalstartdate());
        LocalDate now = LocalDate.now();
        LocalDate ed = LocalDate.parse(item.getGoaldate());

        if(empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            StatusLbl.setText(item.getGoalstatus());
            NameLbl.setText(item.getGoalname());
            DateLbl.setText(item.getGoaldate());
            GroupsLbl.setText(item.getGoalgroups());
            //=(B2 - TODAY()) / (B2 - A2)
            if(item.getGoalstatus()=="Comlete"){
                Progress.setProgress(1);

            }else if(item.getGoalstatus()=="Active"){
                float percent=ChronoUnit.DAYS.between(ed,now)/ChronoUnit.DAYS.between(ed,sd);
                Progress.setProgress(percent);
            }

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}



