import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;

public class UserPageController extends BaseController{
    @FXML Label name;
    @FXML Label email;
    @FXML Label height;
    @FXML Label weight;
    @FXML Label bmi;
    @FXML Label bmilabel;

    /**
     * Initialize all the fiels in the UserPage with current information taken from the User class
     */
    public void initialize() {

            userLabel.setText("Hello " + User.INSTANCE.getUsername());
            DecimalFormat df = new DecimalFormat("#.00");
            double var = (User.INSTANCE.getWeight() / (User.INSTANCE.getHeight() * User.INSTANCE.getHeight())) * 10000;
            name.setText("Name: " + User.INSTANCE.getUsername());
            email.setText("Email: " + User.INSTANCE.getEmail());
            height.setText("Height: " + User.INSTANCE.getHeight());
            weight.setText("Weight: " + User.INSTANCE.getWeight());
            bmi.setText("BMI: " + df.format(var));
            bmilabel.setUnderline(true);
            if (var < 18.5) {
                bmilabel.setText("UNDERWEIGHT");
            } else if (var > 18.5 && var < 24.9) {
                bmilabel.setText("NORMAL WEIGHT");
            } else if (var > 25.0 && var < 29.9) {
                bmilabel.setText("OVERWEIGHT");
            } else {
                bmilabel.setText("OBESE");
            }

    }
}
