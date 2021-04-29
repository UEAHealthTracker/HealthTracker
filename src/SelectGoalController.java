import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SelectGoalController extends BaseController {
    @FXML ComboBox gtypecb;
    @FXML ComboBox gnamecb;

    public void initialize() {

        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        gtypecb.getItems().add("Simple");
        gtypecb.getItems().add("Complex");


        String SQL_query="SELECT goalname from Goal JOIN Users ON Users.userid=Goal.userid where Users.userid=?";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_query);
            pst.setInt(1, User.INSTANCE.getUserid());
            ResultSet rs=pst.executeQuery();
            while(rs.next()) {
                gnamecb.getItems().add(rs.getString("goalname"));
            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
    }

    //allow user to select a table item/row and delete it using the delete button
    public void onUpdate(javafx.event.ActionEvent actionEvent) throws IOException {

           // Goal.Instance.setGoalid(selectedGoal.getGoalid());
            String SQL_query="UPDATE Goal SET goaltype = ? WHERE goalname=?;";
            try{
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_query);
                pst.setString(1, gtypecb.getSelectionModel().getSelectedItem().toString());
                pst.setString(1, gnamecb.getSelectionModel().getSelectedItem().toString());
                pst.executeUpdate();
                DBsession.INSTANCE.OpenConnection().close();
            }catch(Exception e){System.out.println(e);}

            Instance.filename= "FXML/HomePage.fxml";
    }

}
