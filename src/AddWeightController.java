import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

//import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Random;

public class AddWeightController extends BaseController {

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
    @FXML
    TextField codetf;
    @FXML Label msglbl;

    /**
     * Initializing toggles and userlabel
     */
    public void initialize() {
        kgtb.setToggleGroup(group);
        lbtb.setToggleGroup(group);
        kgtb.setSelected(true);
        userLabel.setText("Hello "+User.INSTANCE.getUsername());
    }

    /**
     *Toggle function which makes a basic flipping animation
     */
    public void toggles(javafx.event.ActionEvent actionEvent){
        if(lbtb.isSelected()==true && kgtb.isSelected()==false){
            lbtb.setStyle("-fx-text-fill:#aa80ff;-fx-background-color: transparent");
            kgtb.setStyle("-fx-text-fill:#cab0ff;-fx-background-color: transparent");
        }else if(kgtb.isSelected()==true && lbtb.isSelected()==false){
            kgtb.setStyle("-fx-text-fill:#aa80ff; -fx-background-color: transparent");
            lbtb.setStyle("-fx-text-fill:#cab0ff;-fx-background-color: transparent");

        }
    }


    /**
     * Function to add new goal record inside the database
     * @param actionEvent
     * @throws IOException
     */
    public void AddWeightGoal(javafx.event.ActionEvent actionEvent) throws IOException {
        LocalDate now = LocalDate.now();
        //TODO Complete query with group exending test
        String SQL_QUERY="INSERT into Goal (goalname,enddate,userid,startdate,goaltype,code) values (?,?,?,?,?,?)";
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
                pst.setString(6,getSaltString());
            pst.executeUpdate();
            if(String.valueOf(enddate.getValue())==""||Nametf.getText()==""|| Weightgtf.getText()==""){
                msglbl.setVisible(true);
            }

            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
            BaseController.Instance.Switch(actionEvent, "FXML/HomePage.fxml");

    }

    /**
     * Window switch function
     * @param actionEvent
     * @throws IOException
     */
    public void CustomGoal(javafx.event.ActionEvent actionEvent) throws IOException {
        BaseController.Instance.Switch(actionEvent, "FXML/AddWorkoutGoal.fxml");
    }


    /**
     * Alphanumeric generator function which returns a string credits to the owner
     */
    //https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    /**
     * Function given the code of a specific goal in a group a goal can be added as local goal by the user
     * @param actionEvent
     * @throws IOException
     */
    public void AddWorkoutByCode(javafx.event.ActionEvent actionEvent) throws IOException {
        FindGoal(codetf.getText());
        String SQL_QUERY="INSERT into Goal (goalname,enddate,userid,startdate,goaltype,code) values (?,?,?,?,?,?)";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);

            pst.setString(1, Goal.Instance.getGoalname());
            pst.setString(2, Goal.Instance.getGoaldate());
            pst.setInt(3, User.INSTANCE.getUserid());
            pst.setString(4, Goal.Instance.getGoalstartdate());
            pst.setString(5, "Simple");
            pst.setString(6,Goal.Instance.getCode());
            pst.executeUpdate();
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
//        if (Check() == true) {
        Instance.Switch(actionEvent, "FXML/HomePage.fxml");
        //  }
    }

    /**
     * Search function to find a specific goal given the shared code in a email
     * @param goalcode
     */
    public void FindGoal(String goalcode){
        int i=0;
        String SQL_QUERY="select goalname,code,enddate,startdate,goaltype from Goal left join groupgoal on groupgoal.goalid=Goal.goalid left JOIN groups on groups.groupid=groupgoal.groupid where Goal.code=?";
        try{
            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
            pst.setString(1, goalcode);
            ResultSet rs=pst.executeQuery();
            while(rs.next()) {
            Goal.Instance.setCode(rs.getString("code"));
            Goal.Instance.setGoalname(rs.getString("goalname"));
            Goal.Instance.setGoaldate(rs.getString("enddate"));
            Goal.Instance.setGoalstartdate(rs.getString("startdate"));
            }
            DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){System.out.println(e);}
    }

}
