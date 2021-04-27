import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;

public class EditUsePageController extends BaseController{

    @FXML
    TextField nametf;
    @FXML TextField emailtf;
    @FXML TextField heighttf;
    @FXML TextField weighttf;
    public void initialize() {
        userLabel.setText("Hello " + User.INSTANCE.getUsername());
        nametf.setText(User.INSTANCE.getRealName());
        emailtf.setText(User.INSTANCE.getEmail());
        heighttf.setText(String.valueOf((int)User.INSTANCE.getHeight()));
        weighttf.setText(String.valueOf((int)User.INSTANCE.getWeight()));
    }

    public void Update(javafx.event.ActionEvent actionEvent) throws IOException {
        String SQL_QUERY="UPDATE Users set realname=?, email=?,height=? ,weight=? where userid=?";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, nametf.getText());
            pst.setString(2, emailtf.getText());
            pst.setInt(3, Integer.parseInt(heighttf.getText()));
            pst.setInt(4, Integer.parseInt(weighttf.getText()));
            pst.setInt(5, User.INSTANCE.getUserid());
            pst.executeUpdate();

            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
        User.INSTANCE.setRealName(nametf.getText());
        User.INSTANCE.setEmail(emailtf.getText());
        User.INSTANCE.setHeight(Double.parseDouble(heighttf.getText()));
        User.INSTANCE.setWeight(Double.parseDouble(weighttf.getText()));
        BaseController.Instance.Switch(actionEvent, "FXML/HomePage.fxml");

    }
}
