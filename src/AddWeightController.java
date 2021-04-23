import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class AddWeightController extends BaseController {
    @FXML
    Label userLabel;
    @FXML
    TextField Weightgtf;
    @FXML
    TextField Nametf;
    @FXML
    DatePicker enddate;
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
        userLabel.setText("Hello "+User.INSTANCE.getUsername());


    }

    public void toggles(javafx.event.ActionEvent actionEvent){
        if(lbtb.isSelected()==true && kgtb.isSelected()==false){
            lbtb.setStyle("-fx-text-fill:#aa80ff;-fx-background-color: transparent");
            kgtb.setStyle("-fx-text-fill:#cab0ff;-fx-background-color: transparent");
        }else if(kgtb.isSelected()==true && lbtb.isSelected()==false){
            kgtb.setStyle("-fx-text-fill:#aa80ff; -fx-background-color: transparent");
            lbtb.setStyle("-fx-text-fill:#cab0ff;-fx-background-color: transparent");

        }
    }



    public void AddWeightGoal(javafx.event.ActionEvent actionEvent) throws IOException {
        LocalDate now = LocalDate.now();
        //TODO Complete query with group exending test
        String SQL_QUERY="INSERT into Goal (goalname,enddate,userid,startdate,goaltype) values (?,?,?,?,?)";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
        //    if(Check()==true) {
                if (Nametf.getText() == "" && kgtb.isSelected()) {
                    pst.setString(1, Weightgtf.getText() + "Kg");
                }else  if (Nametf.getText() == "" && lbtb.isSelected()) {
                    pst.setString(1, Weightgtf.getText() + "Lbs");
                } else if (Weightgtf.getText() == "") {
                    pst.setString(1, Nametf.getText());
                } else {
                    if( kgtb.isSelected()) {
                        pst.setString(1, (Nametf.getText() + " " + Weightgtf.getText() + "Kg"));
                    }else if(lbtb.isSelected()){
                        pst.setString(1, (Nametf.getText() + " " + Weightgtf.getText() + "Lbs"));
                    }
                }
                pst.setString(2, String.valueOf(enddate.getValue()));
                pst.setInt(3, User.INSTANCE.getUserid());
                pst.setString(4, String.valueOf(now));
                pst.setString(5, "Simple");
            pst.executeUpdate();
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
            BaseController.Instance.Switch(actionEvent, "FXML/HomePage.fxml");

    }
    public void CustomGoal(javafx.event.ActionEvent actionEvent) throws IOException {
        BaseController.Instance.Switch(actionEvent, "FXML/AddWorkoutGoal.fxml");

    }


    public Boolean Check(){
        return (Nametf.getText() != "" || Weightgtf.getText() != "") && enddate.getValue().toString() != "";
    }



}
