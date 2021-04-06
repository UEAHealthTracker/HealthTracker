import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.DecimalFormat;

public class UserPageController extends BaseController{

    @FXML
    Label name;
    @FXML Label email;
    @FXML Label height;
    @FXML Label weight;
    @FXML Label bmi;
    @FXML Label bmilabel;


    @FXML private void initialize() {

        DecimalFormat df = new DecimalFormat("#.00");

        Platform.runLater(() -> {

            name.setText("Name: " + user.getUsername());
            email.setText("Email: " + user.getEmail());
            height.setText("Height: " + user.getHeight());
            weight.setText("Weight: " + user.getWeight());
            bmi.setText("BMI: " + df.format(user.getBMI()));

            if(user.getBMI()<18.5){
                bmilabel.setText("UNDERWEIGHT");
            }else if(user.getBMI()>18.5 && user.getBMI()<24.9){
                bmilabel.setText("NORMAL WEIGHT");
            }else if(user.getBMI()>25.0 && user.getBMI()<29.9){
                bmilabel.setText("OVERWEIGHT");
            }else{
                bmilabel.setText("OBESE");
            }
        });

    }
}
