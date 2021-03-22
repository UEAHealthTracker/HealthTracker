
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkoutPageController extends BaseController {

    public TableView workoutTable;
    private static Button addWorkoutbtn;
    private static final String SQL_Insert ="INSERT INTO workout (workoutid, sets, reps, calories, weekday) VALUES ('4','3','20','150 calories','Wednesday')";


    //add data to workout table
    public void populateWorkoutTable(){
      // Statement statement = conn.createStatement();
        try {
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_Insert);
           // statement.executeUpdate(SQL_Insert);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){

    }


}

