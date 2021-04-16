import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.UnsupportedEncodingException;
import java.sql.SQLOutput;
import java.util.Date;


//import javax.mail.internet.MimeMessage;


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
//import javax.mail.Session;


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
    private ObservableList<Group> data;
    @FXML
    TextField groupName;
    @FXML
    TextField completeMessage;
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



    @FXML
    TableView<Group> groupView;
    String nameOfGroup,groupAdmin, groupMail, passwordGroup;
    @FXML
    TableColumn<Group, String> groupname;
    @FXML
    TableColumn<Group, String> groupMembers;
    @FXML
    Label userLabel;

    //Group existingGroup = new Group();
    int items=0;

    //Methods that initialise the scene builder textfields to the static elements
    public void initialize() throws SQLException {
        groupNameText = groupName;
        groupMemberText = groupMembersEmail;
        createGroupbutton = createGroupbtn;
        imageViewstatic = imageView;
        //userLabel.setText("Hello "+User.INSTANCE.getUsername());
        populateGroupTables();


    }





    public void CreateGroup(ActionEvent actionEvent) throws SQLException, SQLException, IOException {
        //Test to see if textfields and
        //Set Group name and get Group object
         groupAdmin=User.INSTANCE.getUsername();
        nameOfGroup = groupName.getText();
        groupMail = groupMembersEmail.getText();
        passwordGroup = groupPassword.getText();
        Group newGroup = new Group(nameOfGroup,groupAdmin, passwordGroup );
        newGroup.setGroupAdmin(groupAdmin);
        newGroup.setGroupName(nameOfGroup);
        newGroup.setGroupPassword(passwordGroup);
        //Insert group details and create group:
        if (nameOfGroup.isEmpty() || groupMail.isEmpty() == true) {
            System.out.println("Please ensure that both group name and the group  mail is filled ");
            imageView.setOpacity(0);
            createGroupbtn.setOpacity(1);
            createGroupbtn.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
            createGroupbtn.setText("Please fill all the details");

        } else {
            //Since all the input fileds are filed next is:
            //Check if user to be invited exists:

            if (groupExist() == false) {
                if (userExist()) {
                    try {
                        String username, password;
                        String adminUserId;
                        int memberUserId;
                        adminUserId = Integer.toString(User.INSTANCE.getUserid());
                        System.out.println(adminUserId);
                        //Create Group and Invite Members
                        //If email sending is successful
                        if (SendMail.sendMail(groupMail, nameOfGroup, passwordGroup) == true) {
                            //Add the details to the database:
                            String insertQuery = "INSERT INTO groups(groupname, groupadmin, groupPassword) VALUES(?,?, ?)";
                            PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(insertQuery);
                            pst.setString(1, nameOfGroup);
                            pst.setString(2, groupAdmin);
                            pst.setString(3, passwordGroup);
                            pst.executeUpdate();
                            System.out.println("Group has been created. The name of the Group is " + newGroup.getGroupName());
                            System.out.println("Group admin is " +groupAdmin);
                            System.out.println("The group password is " + newGroup.getGroupPassword());

                            //Insert the admin as a member in the groupsmember group:
                            String getGroupId= "SELECT groupid FROM groups WHERE groupname =?";
                            PreparedStatement groupIdStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(getGroupId);
                            groupIdStatement.setString(1,nameOfGroup);
                            ResultSet idSet = groupIdStatement.executeQuery();
                            while(idSet.next()) {
                                int groupId = Integer.parseInt(idSet.getString("groupid"));
                                String insertAdminAsMember = "INSERT INTO groupsmember(groupid, userid) VALUES(?,?)";
                                PreparedStatement insertMemberStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(insertAdminAsMember);
                                insertMemberStatement.setInt(1, groupId);
                                insertMemberStatement.setInt(2, User.INSTANCE.getUserid());
                                insertMemberStatement.executeUpdate();

                            }
                            root = FXMLLoader.load(getClass().getResource("GroupsPage.fxml"));
                            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();

                        } else {
                            System.out.println("Mail has not been sent)");
                        }
                    } catch (Exception e) {
                        System.out.println(e);

                    }
                } else {
                    imageView.setOpacity(0);
                    createGroupbtn.setOpacity(1);
                    createGroupbtn.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                    createGroupbtn.setText("User does not exist");
                    System.out.println("User does not exist");

                }
            } else {

                imageView.setOpacity(0);
                createGroupbtn.setOpacity(1);
                createGroupbtn.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                createGroupbtn.setText("Group already exist");
                System.out.println("Group already exist");

            }

        }
        DBsession.INSTANCE.OpenConnection().close();
        }




        /*
        public void AddGroupMembers (ActionEvent actionEvent) throws SQLException {
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

         */

        public void testQuery() throws SQLException {
            //Getting all the groups the user is part of.
            //String queryTest = "SELECT Users.username, groups.groupname FROM Users INNER JOIN groupsmember ON Users.userid = groupsmember.userid INNER JOIN groups ON groupsmember.groupid=groups.groupid WHERE groupsmember.userid=?";
            String queryTest = "SELECT groups.groupname, groups.groupid FROM groups INNER JOIN groupsmember ON groupsmember.groupid=groups.groupid INNER JOIN Users ON Users.userid = groupsmember.userid  WHERE groupsmember.userid=?";
            try{
                PreparedStatement pst= DBsession.INSTANCE.OpenConnection().prepareStatement(queryTest);
                pst.setInt(1,User.INSTANCE.getUserid());
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    System.out.println(rs.getString(1));
                    int groupIds= Integer.parseInt(rs.getString("groupid"));
                    //System.out.println(groupIds);
                    //Get all the members of all the groups, the user is in:
                    String memberQuery= "SELECT Users.username FROM Users INNER JOIN groupsmember ON groupsmember.userid= Users.userid WHERE groupsmember.groupid=?";
                    PreparedStatement pst2= DBsession.INSTANCE.OpenConnection().prepareStatement(memberQuery);
                    pst2.setInt(1, groupIds);
                    ResultSet rs2 = pst2.executeQuery();
                    while(rs2.next()){
                        System.out.println(rs2.getString("username"));

                    }


                }

            }catch(SQLException e){
                e.printStackTrace();

            }
            DBsession.INSTANCE.OpenConnection().close();

        }

        public void populateGroupTablesCreate() throws SQLException {
            String nullName=null, nullAdmin=null, nullPassword = null;
            data = FXCollections.observableArrayList();
            // TableColumn<Group, String> groupname;
            //Select all the groups that the user is admin to:
            String groupAdminInsertQuery = "SELECT groupname, groupadmin FROM groups WHERE groupadmin =? ";
            try {
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(groupAdminInsertQuery);
                pst.setString(1, User.INSTANCE.getUsername());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    data.add(new Group(rs.getString("groupname"), rs.getString("groupadmin"), passwordGroup));
                    groupname.setCellValueFactory(new PropertyValueFactory<>("groupName"));
                    groupMembers.setCellValueFactory(new PropertyValueFactory<>("groupAdmin"));
                    groupView.setItems(data);
                }

            }catch (Exception e){

                }
                DBsession.INSTANCE.OpenConnection().close();
                }


    public void populateGroupTables() throws SQLException {

        data = FXCollections.observableArrayList();
        try{
            //Getting all the groups the user is part of.
            String queryTest = "SELECT groups.groupname, groups.groupid FROM groups INNER JOIN groupsmember ON groupsmember.groupid=groups.groupid INNER JOIN Users ON Users.userid = groupsmember.userid  WHERE groupsmember.userid=?";
            try{
                PreparedStatement pst= DBsession.INSTANCE.OpenConnection().prepareStatement(queryTest);
                pst.setInt(1,User.INSTANCE.getUserid());
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    //System.out.println(rs.getString(1));
                    int groupIds= Integer.parseInt(rs.getString("groupid"));
                    //System.out.println(groupIds);
                    //Get all the members of all the groups, the user is in:
                    String memberQuery= "SELECT Users.username FROM Users INNER JOIN groupsmember ON groupsmember.userid= Users.userid WHERE groupsmember.groupid=?";
                    PreparedStatement pst2= DBsession.INSTANCE.OpenConnection().prepareStatement(memberQuery);
                    pst2.setInt(1, groupIds);
                    ResultSet rs2 = pst2.executeQuery();
                    ArrayList members = new ArrayList();
                    while(rs2.next()){
                        members.add(rs2.getString("username"));



                    }
                    data.add(new Group(rs.getString("groupname"), rs.getString("groupname")));
                    groupname.setCellValueFactory(new PropertyValueFactory<>("groupName"));
                    groupMembers.setCellValueFactory(new PropertyValueFactory<>("groupName"));
                    //groupMembers.setCellValueFactory(new PropertyValueFactory<>("memberName"));
                     //groupMembers.setCellFactory(new PropertyValueFactory<>());

                    for(int i=0; i< members.size(); i++){
                        System.out.println(members.get(i));
                    }


                    groupView.setItems(data);


                }

            }catch(SQLException e){
                e.printStackTrace();

            }
        }catch (Exception e){

        }
        DBsession.INSTANCE.OpenConnection().close();
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


    public boolean userExist() {

        String emailDb;
        boolean userExist = false;
        try {
            ResultSet rs = DBsession.INSTANCE.Stmt().executeQuery("select email from Users ");
            while(rs.next()){
                emailDb = rs.getString("email");
                if(emailDb.contentEquals(groupMail)){
                    userExist=true;

                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return userExist;
    }

    public boolean groupExist(){
        String databaseGroups;
        boolean groupExist=false;
        try {
            ResultSet rs2 = DBsession.INSTANCE.Stmt().executeQuery("select groupname from groups");
            while(rs2.next()) {
                databaseGroups = rs2.getString("groupname");
                if (databaseGroups.contentEquals(nameOfGroup)) {
                    groupExist = true;

                }
            }
        }catch (SQLException e){

        }
        //System.out.println(groupExist);
        return groupExist;

    }




}


/*
        public void EditGroupCommands() {
        nameOfGroup = groupName.getText();
        String groupEmail = groupMembersEmail.getText();

        if(){

        }

         */


















