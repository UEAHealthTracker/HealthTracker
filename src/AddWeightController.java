import javafx.application.Platform;
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
        label=statuslbl;
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
        label.setText("");
        label.setStyle("-fx-text-fill: white");
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
            ;
                pst.executeUpdate();
//            }else{ thread.start();
//                label.setText("");
//            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
//        if (Check() == true) {
            BaseController.Instance.Switch(actionEvent,"HomePage.fxml");
      //  }
    }

    public Boolean Check(){
        if(Nametf.getText()=="" && Weightgtf.getText()=="" || enddate.getValue().toString()==""){
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
