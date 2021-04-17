import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class WorkoutPageController extends BaseController implements Initializable {

    @FXML
    private static Button addWorkoutbtn;

    @FXML
    public TableColumn<Workout, String> workoutType;

    @FXML
    public TableColumn<Workout, Integer> workoutDuration;

    @FXML
    public TableColumn<Workout, Integer> caloriesBurned;

    @FXML
    private TableView<Workout> workoutTableView;

    @FXML
    ComboBox<String> WorkoutTypeSelector;

    @FXML
    TextField durationTF;

    private boolean open=false;

    public void init(){
        if(!open) {
            for (WorkoutType type : WorkoutType.values()) {
                WorkoutTypeSelector.getItems().add(type.ID);
            }
            open=true;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {

            populateWorkoutTable();

        });

    }

    public void addWorkout(ActionEvent actionEvent) throws IOException {
        int cal=0;
        WorkoutType newWorkoutType = null;

        System.out.println(Arrays.toString(WorkoutType.values()));
        System.out.println(WorkoutTypeSelector.getSelectionModel().getSelectedItem());

        for (WorkoutType type : WorkoutType.values()) {
            if(WorkoutTypeSelector.getSelectionModel().getSelectedItem().equals(type.ID)){
                newWorkoutType = type;
                System.out.println(type.ID);
                cal= type.MET;
            }
        }

        Workout newWorkout = new Workout(newWorkoutType,Integer.parseInt(durationTF.getText()), cal);

        user.addWorkout(newWorkout);

        loadPage(actionEvent, "WorkoutPage.fxml");
    }

    //add data to workout table
    public void populateWorkoutTable(){

        System.out.println(user.getWorkouts());

        System.out.println(this.workoutType);
        System.out.println(this.caloriesBurned);
        System.out.println(this.workoutDuration);

        //Get the attributes for the table from default getters and setters
        workoutType.setCellValueFactory(new PropertyValueFactory<>("workoutType"));
        workoutDuration.setCellValueFactory(new PropertyValueFactory<>("workoutDuration"));
        caloriesBurned.setCellValueFactory(new PropertyValueFactory<>("caloriesBurned"));

        //Populate the table data
        workoutTableView.setItems(getWorkouts());

    }

    //Create an observable list that can be rendered by javafx with the data inside
    private ObservableList<Workout> getWorkouts(){
        ObservableList<Workout> workouts = FXCollections.observableArrayList();
        workouts.addAll(user.getWorkouts());
        return workouts;
    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){

    }


}

