import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;

public class AddWeightController extends BaseController {
    @FXML
    Label userLabel;
    @FXML
    TextField Weightgtf;
    @FXML
    TextField Nametf;
    @FXML
    DatePicker endDate;
    @FXML Label statuslbl;
    Label label;
    @FXML
    ToggleButton kgtb;
    @FXML ToggleButton lbtb;
    ToggleGroup group = new ToggleGroup();
    public void initialize() {
        kgtb.setToggleGroup(group);
        lbtb.setToggleGroup(group);
        kgtb.setSelected(true);
        label=statuslbl;
        userLabel.setText("Hello "+user.getUsername());
        label.setText("");
        label.setStyle("-fx-text-fill: white");
    }


    public void AddWeightGoal(javafx.event.ActionEvent actionEvent) throws IOException {
        LocalDate now = LocalDate.now();
        //TODO Complete query with group exending test

        String weight;
        String name = null;
        LocalDate date;
        String status;

        if (Nametf.getText().equals("") && kgtb.isSelected()) {
            name = Weightgtf.getText() + "Kg";
        }
        else  if (Nametf.getText().equals("") && lbtb.isSelected()) {
            name = Weightgtf.getText() + "Lbs";
        }
        else if (Weightgtf.getText().equals("")) {
            name = Nametf.getText();
        }
        else {
            if( kgtb.isSelected()) {
                name = Nametf.getText() + " " + Weightgtf.getText() + "Kg";
            }else if(lbtb.isSelected()){
                name = Nametf.getText() + " " + Weightgtf.getText() + "Lbs";
            }
        }

        date = endDate.getValue();

        Goal newGoal = new Goal(name, date, now, "Simple");

        user.addGoal(newGoal);
    }

    public Boolean Check(){
        if(Nametf.getText().equals("") && Weightgtf.getText().equals("") || endDate.getValue().toString().equals("")){
            return false;
        }
        return true;
    }

//    Thread thread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            Runnable updater = new Runnable() {
//                @Override
//                public void run() {
//                    label.setStyle("-fx-text-fill: red");
//                    label.setText("Incorrect Values");
//
//                }
//            };
//            while (true) {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException ex) {
//                }
//                // UI update is run on the Application thread
//                Platform.runLater(updater);
//            }
//        }
//
//    });

}
