import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddWeightController extends BaseController implements Initializable {
    @FXML
    Label userLabel;

    @FXML
    TextField Weightgtf;

    @FXML
    TextField Nametf;

    @FXML
    DatePicker endDate;

    @FXML
    Label statuslbl;

    @FXML
    Label label;

    @FXML
    ToggleButton kgtb;

    @FXML
    ToggleButton lbtb;

    @FXML
    ToggleGroup group = new ToggleGroup();


    public void AddWeightGoal(javafx.event.ActionEvent actionEvent) throws IOException {
        LocalDate now = LocalDate.now();

        String name = null;
        LocalDate date;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            this.setUserLabel();
            this.setUser(user);
            kgtb.setToggleGroup(group);
            lbtb.setToggleGroup(group);
            kgtb.setSelected(true);
            label = statuslbl;
            userLabel.setText("Hello " + user.getUsername());
            label.setText("");
            label.setStyle("-fx-text-fill: white");
        });
    }

}
