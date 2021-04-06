import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserPageController extends BaseController{
    @FXML
    Label name;
    @FXML Label email;
    @FXML Label height;
    @FXML Label weight;
    @FXML Label bmi;

    public void initialize() {
        name.setText("Name: "+User.INSTANCE.getRealName());
        email.setText("Email: "+User.INSTANCE.getEmail());
        height.setText("Height: "+User.INSTANCE.getRealName());
        weight.setText("Weight: "+User.INSTANCE.getWeight());
        bmi.setText("BMI: "+User.INSTANCE.getBMI());

    }
}
