import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.UnsupportedEncodingException;
import java.util.Date;


import javax.mail.internet.MimeMessage;


import java.util.Properties;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Session;


public class GroupsPageController extends BaseController {
    //Variables needed for controller
    private Parent root;
    private Stage stage;
    private Scene scene;
    //Email Pattern that emails must adhere to
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static TextField groupNameText, groupMemberText;
    private static ImageView imageViewstatic;
    private static Button createGroupbutton;
    //Textfields that correspond to Scene Builder elements
    @FXML
    TextField groupName;
    @FXML
    TextField groupMembersEmail;
    @FXML
    Button inviteMember;
    @FXML
    Button createGroupbtn;
    @FXML
    ImageView imageView;
    @FXML
    TextField groupPassword;
    String nameOfGroup, groupMail, passwordGroup;


    //Methods that initialise the scene builder textfields to the static elements
    public void initialize() {
        groupNameText = groupName;
        groupMemberText = groupMembersEmail;
        createGroupbutton = createGroupbtn;
        imageViewstatic = imageView;

    }


    public void CreateGroup(ActionEvent actionEvent) throws SQLException, SQLException, IOException {
        //Test to see if textfields and
        //Set Group name and get Group object
        nameOfGroup = groupName.getText();
        groupMail = groupMembersEmail.getText();
        passwordGroup= groupPassword.getText();
        String groupAdmin = User.INSTANCE.getUsername();
        Group newGroup = new Group();
        newGroup.setGroupName(nameOfGroup);
        newGroup.setGroupAdmin(groupAdmin);
        newGroup.setGroupPassword(passwordGroup);
        //Insert group details and create group:
        if (nameOfGroup.isEmpty() || groupMail.isEmpty() == true) {
            System.out.println("Please ensure that both group name and the group  mail is filled ");
            imageView.setOpacity(0);
            createGroupbtn.setOpacity(1);
            createGroupbtn.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
            createGroupbtn.setText("Please fill all the details");
            //Reload the page seconds after displaying the error message
            /*
            reloadTime reloadTiming=null;
            reloadTiming.timer.schedule(new reloadTime(), );

            class reloadPage extends TimerTask{
                public void run(){
                    try {
                        root = FXMLLoader.load(getClass().getResource("CreateGroupPage.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
             */
        } else {
            try {
                String username, password;
                String adminUserId;
                int memberUserId;
                adminUserId = Integer.toString(User.INSTANCE.getUserid());
                System.out.println(adminUserId);
                //Create Group and Invite Members
                //If email sending is successful
               if(SendMail.sendMail(groupMail, nameOfGroup, passwordGroup)==true && CheckCredentials() > 0){
                   //Add the details to the database:
                   String insertQuery = "INSERT INTO groups(groupname, groupadmin, groupPassword) VALUES(?,?, ?)";
                   PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(insertQuery);
                   pst.setString(1, nameOfGroup);
                   pst.setString(2, groupAdmin);
                   pst.setString(3, passwordGroup);
                   pst.executeUpdate();
                   System.out.println("Group has been created. The name of the Group is " + newGroup.getGroupName());
                   System.out.println("Group admin is "+ groupAdmin);
                   System.out.println("The group password is " + newGroup.getGroupPassword());
               }else{
                   System.out.println("User does not exists");
               }
            } catch (Exception e) {
                System.out.println(e);

            }

        }

        DBsession.INSTANCE.OpenConnection().close();
    }

    public void AddGroupMembers(ActionEvent actionEvent) throws SQLException {
        String email;
        String adminUserId;
        email = groupMembersEmail.getText();
        adminUserId = Integer.toString(User.INSTANCE.getUserid());
        //System.out.println("AddGroup Member userId:" +adminUserId);
        //If user exists
        if (email.matches(EMAIL_PATTERN)) {
            if (CheckCredentials() > 0) {
                System.out.println("User and group exists test");
                //Getting the emails of the members
                String memberQuery = "select userid from Users where email=?";
                PreparedStatement firstStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(memberQuery);
                firstStatement.setString(1, email);
                ResultSet rs = firstStatement.executeQuery();
                while (rs.next()) {
                    System.out.println("The member's user id is: " + rs.getInt("userid"));
                }
            } else {
                System.out.println("Group and/or user does not exist");
                createGroupbtn.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                createGroupbtn.setText("User does not exists");
            }
            DBsession.INSTANCE.OpenConnection().close();
        } else {
            System.out.println("Email is not written correctly. Please type it well");
            createGroupbtn.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
            createGroupbtn.setText("Please type the email correctly");
        }
    }

    //Checking credntials for adding members in the group, after group has been created.
    public int CheckCredentials() {
        int counter = 0;
        String emailDb;
        try {
            ResultSet rs = DBsession.INSTANCE.Stmt().executeQuery("select email from Users");
            ResultSet rs2 = DBsession.INSTANCE.Stmt().executeQuery("select groupname from groups");
            while (rs.next() && rs2.next()) {
                emailDb = rs.getString("email");
                nameOfGroup = rs2.getString("groupname");
                if (emailDb.equals(groupMembersEmail.getText()) && nameOfGroup.equals(groupName.getText())) {
                    counter++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return counter;
    }

    //Check if user exists to be added to the group
    public int checkUser() {
        int counter = 0;
        String emailDb;
        try {
            ResultSet rs = DBsession.INSTANCE.Stmt().executeQuery("select email from Users ");
            while (rs.next()) {
                emailDb = rs.getString("email");
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    //Check if group already exists
    public int checkGroup() {
        int counter = 0;
        try {
            ResultSet rs = DBsession.INSTANCE.Stmt().executeQuery("select groupname from groups");
            while (rs.next()) {
                nameOfGroup = rs.getString("groupname");
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }



/*
        public void EditGroupCommands() {
        nameOfGroup = groupName.getText();
        String groupEmail = groupMembersEmail.getText();

        if(){

        }

         */


    }















