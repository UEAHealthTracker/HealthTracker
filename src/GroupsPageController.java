import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


//import javax.mail.internet.MimeMessage;


import javax.mail.MessagingException;
import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.*;
//import javax.mail.Session;


public class GroupsPageController extends BaseController {
    //Variables needed for controller

    //Email Pattern that emails must adhere to
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static TextField groupNameText, groupMemberText;
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
    TextField groupPassword;
    @FXML
    TextField joinGroupName;

    @FXML
    Label GroupMessage;

    @FXML
    Label messageLabel;

    @FXML
    TextField joinGroupPassword;



    @FXML AnchorPane groupPage;



    String nameOfGroup,groupAdmin, groupMail, passwordGroup;
    @FXML
    TableView<Group> groupView;

    @FXML
    TableColumn<Group, String> groupname;
    @FXML
    TableColumn<Group, String> groupMembers;
    @FXML
    Label userLabel;
    static Group selectedGroup = null;

    //Group existingGroup = new Group();
    int items=0;

    public GroupsPageController() throws IOException {
    }

    //Methods that initialise the scene builder textfields to the static elements
    public void initialize() {
        //System.out.println(text);
        groupNameText = groupName;
        groupMemberText = groupMembersEmail;
        createGroupbutton = createGroupbtn;
        userLabel.setText("Hello " + User.INSTANCE.getUsername());
        //System.out.println(groupPage.toString());
        //System.out.println(stage.getScene().getWindow().toString());
        /*
         if(groupPage.toString()!=null) {
                populateGroupTables();
            }else{
                System.out.println("Not on group Page");

            }
         */
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
        if (nameOfGroup.isEmpty() || groupMail.isEmpty()|| passwordGroup.isEmpty()) {
            System.out.println("Please ensure that both group name, mail and password is filled ");
            GroupMessage.setOpacity(1);
            GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
            GroupMessage.setText("Please fill all the details");
            GroupMessage.setAlignment(Pos.CENTER);

        } else {
            //Since all the input fileds are filed next is:
            //Check if user to be invited exists:
            if (groupExist(nameOfGroup) == false) {
                if (userExist(groupMail)) {
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
                            //Add the  invite details into the database.
                            AddInvite(groupAdmin, groupMail, nameOfGroup);
                            root = FXMLLoader.load(getClass().getResource("FXML/GroupsPage.fxml"));
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
                    GroupMessage.setOpacity(1);
                    GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                    GroupMessage.setText("User does not exist");
                    GroupMessage.setAlignment(Pos.CENTER);
                    System.out.println("User does not exist");
                }
            } else {
                GroupMessage.setOpacity(1);
                GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                GroupMessage.setText("Group already exist");
                GroupMessage.setAlignment(Pos.CENTER);
                System.out.println("Group already exist");
            }
        }
        DBsession.INSTANCE.OpenConnection().close();
        }

//Add invite after sending the mail:
    public void AddInvite(String groupAdmin, String receipient, String groupName) throws SQLException {
        //Insert
        try {
            //Insert the admin as a member in the groupsmember group:
            String getGroupId = "SELECT groupid FROM groups WHERE groupname =?";
            PreparedStatement groupIdStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(getGroupId);
            groupIdStatement.setString(1, groupName);
            ResultSet idSet = groupIdStatement.executeQuery();
            while(idSet.next()){
                int groupId = idSet.getInt("groupid");
                //Insert the invite
                String addInviteQuery= "INSERT INTO group_invites(group_id, group_name, group_admin, group_member)VALUES(?,?,?,?)";
                PreparedStatement inviteStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(addInviteQuery);
                inviteStatement.setInt(1,groupId);
                inviteStatement.setString(2, groupName);
                inviteStatement.setString(3, groupAdmin);
                inviteStatement.setString(4, receipient);
                inviteStatement.executeUpdate();
                System.out.println("Invite sent  to " + receipient+" by " + groupAdmin);
                System.out.println("Invite stored into the database.");



            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }




    public void JoinGroup(ActionEvent actionEvent) throws SQLException, IOException {
        if(!alreadyInGroup()) {
            if(joinGroupsCredentials()){
                System.out.println("You are invited and can join group. The group and password do match");
                if(checkInvite()){
                    String getGroupId = "SELECT groupid FROM groups WHERE groupname =?";
                    PreparedStatement groupIdStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(getGroupId);
                    groupIdStatement.setString(1, joinGroupName.getText());
                    ResultSet idSet = groupIdStatement.executeQuery();
                    while(idSet.next()){
                        int groupId = idSet.getInt("groupid");
                        //Since the user is not in the group and the join group credentials are correct and the user was invited, now the user can join the group:
                        String joinQuery= "INSERT INTO groupsmember(groupid, userid) VALUES(?,?)";
                        PreparedStatement joinStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(joinQuery);
                        joinStatement.setInt(1, groupId);
                        joinStatement.setInt(2, User.INSTANCE.getUserid());
                        joinStatement.executeUpdate();
                        System.out.println("Successfully joined the group "+ joinGroupName.getText());

                        //Move to the groups page to show they have joined the group:
                        root = FXMLLoader.load(getClass().getResource("FXML/GroupsPage.fxml"));
                        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();


                    }






                }else{
                    GroupMessage.setOpacity(1);
                    GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                    GroupMessage.setText("Not invited thus can't join");
                    GroupMessage.setAlignment(Pos.CENTER);

                }

            }else{
                GroupMessage.setOpacity(1);
                GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                GroupMessage.setText("Try Again. Name and Password Don't match.");
                GroupMessage.setAlignment(Pos.CENTER);
                System.out.println("The group and Password don't match");

            }
            System.out.println("You are not already in the group");


        }else{
            GroupMessage.setOpacity(1);
            GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
            GroupMessage.setText("You are already in the group");
            GroupMessage.setAlignment(Pos.CENTER);
            System.out.println("You are already in group");


        }

    }

    //Check if user was invited to the group they are attempting to join
    public boolean checkInvite() throws SQLException {
        boolean wasUserInvited= false;
        String groupName = joinGroupName.getText();
        System.out.println(User.INSTANCE.getEmail());
        String SQLQuery= "SELECT group_invites.group_name, group_invites.group_admin, group_invites.group_member FROM group_invites INNER JOIN  groups ON group_invites. group_id=groups.groupid WHERE group_invites.group_name =? AND group_invites.group_member=? AND groups.groupadmin= group_invites.group_admin";
        PreparedStatement checkInvite = DBsession.INSTANCE.OpenConnection().prepareStatement(SQLQuery);
        checkInvite.setString(1,groupName);
        checkInvite.setString(2,User.INSTANCE.getEmail());
        ResultSet result = checkInvite.executeQuery();
        while(result.next()){
            //Final validation. Just for extras.
            if(groupName.contentEquals(result.getString("group_name")) && User.INSTANCE.getEmail().contentEquals(result.getString("group_member"))){
                System.out.println(result.getString(1));
                System.out.println(result.getString(2));
                System.out.println(result.getString(3));
                wasUserInvited= true;
                System.out.println("You are invited to the group and thus can join the group!");
            }
        }




        return wasUserInvited;

    }



        public void testQuery() throws SQLException {
        GroupInvites allInvites = new GroupInvites();
            System.out.println( allInvites.getGroupInvites().toString());



        }



    public void populateGroupTables() {

        data = FXCollections.observableArrayList();
        try{
            //Getting all the groups the user is part of.
            String queryTest = "SELECT groups.groupname, groups.groupid, groups.groupadmin, groups.groupPassword FROM groups INNER JOIN groupsmember ON groupsmember.groupid=groups.groupid INNER JOIN Users ON Users.userid = groupsmember.userid  WHERE groupsmember.userid=?";
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
                    data.add(new Group(rs.getString("groupname"), rs.getString("groupadmin"), rs.getString("groupPassword"), members));
                    groupname.setCellValueFactory(new PropertyValueFactory<>("groupName"));
                    groupMembers.setCellValueFactory(new PropertyValueFactory<>("groupMembers"));
                    //groupMembers.setCellValueFactory(new PropertyValueFactory<>("memberName"));
                     //groupMembers.setCellFactory(new PropertyValueFactory<>());



                    groupView.setItems(data);

                    DBsession.INSTANCE.OpenConnection().close();
                }

            }catch(SQLException e){
                e.printStackTrace();

            }
        }catch (Exception e){
            //e.printStackTrace();
        }
    }


    public void deleteGroup(ActionEvent actionEvent) throws SQLException {
        //check if user has selcted a meal
        if (groupView.getSelectionModel().getSelectedItem() != null) {
            selectedGroup = groupView.getSelectionModel().getSelectedItem();

            //try removing meal data from database
            try{
                String SQL_QUERY="SELECT groupid, groupadmin FROM groups WHERE groupname=?";
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
                pst.setString(1,selectedGroup.getGroupName());
                ResultSet result = pst.executeQuery();
                while(result.next()){
                    String groupAdmin = result.getString("groupadmin");
                    if(groupAdmin.contentEquals(User.INSTANCE.getUsername())){
                        int groupId = result.getInt("groupid");
                        //System.out.println(groupId);
                        //Delete group
                        String delete2="DELETE  FROM groupsmember WHERE groupid=?";
                        PreparedStatement delete2statement = DBsession.INSTANCE.OpenConnection().prepareStatement(delete2);
                        delete2statement.setInt(1, groupId);
                        delete2statement.executeUpdate();

                        String deleteInvite = "DELETE from group_invites WHERE group_id=?";
                        PreparedStatement inviteDelete = DBsession.INSTANCE.OpenConnection().prepareStatement(deleteInvite);
                        inviteDelete.setInt(1,groupId);
                        inviteDelete.executeUpdate();


                        String deleteGoal="DELETE  FROM groupgoal WHERE groupid=?";
                        PreparedStatement goalDelete = DBsession.INSTANCE.OpenConnection().prepareStatement(deleteGoal);
                        goalDelete.setInt(1, groupId);
                        goalDelete.executeUpdate();

                        String deleteQuery="DELETE  FROM groups WHERE groups.groupid=?";
                        PreparedStatement deleteStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(deleteQuery);
                        deleteStatement.setInt(1, groupId);
                        deleteStatement.executeUpdate();

                        DBsession.INSTANCE.OpenConnection().close();
                        //Delete it from the group.
                        groupView.getItems().removeAll(selectedGroup);



                    }else{
                         //Once you are not admin, you just delete yourself from the group:
                        try {
                            String removeQuery = "SELECT groups.groupid  FROM groups INNER JOIN groupsmember ON groupsmember.groupid=groups.groupid INNER JOIN Users ON Users.userid = groupsmember.userid  WHERE groups.groupname=? AND Users.username=?";
                            PreparedStatement removeStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(removeQuery);
                            removeStatement.setString(1, selectedGroup.getGroupName());
                            removeStatement.setString(2, User.INSTANCE.getUsername());
                            ResultSet queryResult = removeStatement.executeQuery();
                            while (queryResult.next()) {
                                int groupId = queryResult.getInt("groupid");
                                System.out.println(groupId);

                                //Remove member:
                                String removeMemberQuery = "DELETE FROM groupsmember WHERE groupid=? AND userid=?";
                                PreparedStatement deleteStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(removeMemberQuery);
                                deleteStatement.setInt(1, groupId);
                                deleteStatement.setInt(2, User.INSTANCE.getUserid());
                                deleteStatement.executeUpdate();
                                messageLabel.setOpacity(1);

                                messageLabel.setText(User.INSTANCE.getUsername() + " has left the group");
                                //Delete invite to stop user from re-joining the group:
                                String removeInvite = "DELETE FROM group_invites WHERE group_id=? AND group_member=?";
                                PreparedStatement deleteInvite = DBsession.INSTANCE.OpenConnection().prepareStatement(removeInvite);
                                deleteInvite.setInt(1, groupId);
                                deleteInvite.setString(2, User.INSTANCE.getEmail());
                                deleteInvite.executeUpdate();
                                System.out.println("Invite has been deleted. User cannot join the group");




                            }
                        }catch (SQLException e){
                            e.printStackTrace();

                        }






                    }

                    //User.INSTANCE.removeMeal(selectedGroup);
                }

                /*
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
                pst.setInt(1, selectedMeal.getMealid());
                */


                //pst.executeUpdate();


                //delete meal from user's daily activity

            }
            catch(SQLException e){
                e.printStackTrace();
                messageLabel.setOpacity(1);
                messageLabel.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                messageLabel.setText("Group has not been deleted.");
                System.out.println("error deleting meal");
            }
        }
        //update table
      populateGroupTables();

    }

public  String getGroupForEdit() throws IOException {
    String getGroupName=null;
    if (groupView.getSelectionModel().getSelectedItem() != null) {
        selectedGroup = groupView.getSelectionModel().getSelectedItem();
        getGroupName = selectedGroup.getGroupName();
    }
    return getGroupName;
}



 public  ArrayList getGroupMember(){
        ArrayList groupMembers = new ArrayList<>();
        String memberName= null;
    if (groupView.getSelectionModel().getSelectedItem() != null) {
        selectedGroup = groupView.getSelectionModel().getSelectedItem();
        memberName= selectedGroup.getGroupMembers();
        //StringTokenizer tokenizer = new StringTokenizer(memberName, ",");
        groupMembers = new ArrayList<>(Arrays.asList(memberName.split(",")));
        //int size = selectedGroup.getGroupMembers();
        //System.out.println(size);

        //System.out.println(memberName);
        /*
        for(int i=0; i< groupMembers.size(); i++){
            System.out.println(groupMembers.get(i));
        }

         */
    }
    return groupMembers;
}




    public void onEditGroup(javafx.event.ActionEvent actionEvent) throws IOException {
        if (groupView.getSelectionModel().getSelectedItem() != null) {
           selectedGroup = groupView.getSelectionModel().getSelectedItem();
            Group.Instance.setGroupName(selectedGroup.getGroupName());
            BaseController.Instance.Switch(actionEvent,"FXML/EditGroupPage.fxml");

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


    public static boolean userExist(String mail) {

        String emailDb;
        boolean userExist = false;
        try {
            ResultSet rs = DBsession.INSTANCE.Stmt().executeQuery("select email from Users ");
            while(rs.next()){
                emailDb = rs.getString("email");
                if(emailDb.contentEquals(mail)){
                    userExist=true;

                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return userExist;
    }

    public static boolean usernameExist(String username) {

        String usernameDb;
        boolean userExist = false;
        try {
            ResultSet rs = DBsession.INSTANCE.Stmt().executeQuery("select username from Users ");
            while(rs.next()){
                usernameDb = rs.getString("username");
                if(usernameDb.contentEquals(username)){
                    userExist=true;

                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return userExist;
    }
    public static boolean groupExist(String groupName){
        String databaseGroups;
        boolean groupExist=false;
        try {
            ResultSet rs2 = DBsession.INSTANCE.Stmt().executeQuery("select groupname from groups");
            while(rs2.next()) {
                databaseGroups = rs2.getString("groupname");
                if (databaseGroups.contentEquals(groupName)) {
                    groupExist = true;

                }
            }
        }catch (SQLException e){

        }
        //System.out.println(groupExist);
        return groupExist;

    }

    //Method to check if the group exists for the join group method.
    public boolean joinGroupCorrect() throws SQLException{
        String groupToJoin = joinGroupName.getText();
        String databaseGroups;
        boolean groupExist=false;
        try {
            ResultSet rs2 = DBsession.INSTANCE.Stmt().executeQuery("select groupname from groups");
            while(rs2.next()) {
                databaseGroups = rs2.getString("groupname");
                if (databaseGroups.contentEquals(groupToJoin)) {
                    groupExist = true;
                    //System.out.println(databaseGroups);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
        //System.out.println("Method Works");
        //System.out.println(groupExist);
        return groupExist;
    }

    //Method to check if the group and password match for user to be able to join group.
    public boolean joinGroupsCredentials() throws SQLException{
        String groupToJoin = joinGroupName.getText();
        String groupPassword = joinGroupPassword.getText();
        String databaseGroup, databasePasswords;
        boolean joinGroupCredential=false;
        try {
            String groupQuery="select groupname, groupPassword from groups where groupname=?";
            PreparedStatement pst= DBsession.INSTANCE.OpenConnection().prepareStatement(groupQuery);
            pst.setString(1, groupToJoin);
            ResultSet rs2 = pst.executeQuery();
            while(rs2.next()) {
                databaseGroup = rs2.getString("groupname");
                databasePasswords= rs2.getString("groupPassword");
                if (databaseGroup.contentEquals(groupToJoin)&& databasePasswords.contentEquals(groupPassword)) {
                    joinGroupCredential = true;
                    System.out.println("You can join group called:" + databaseGroup);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return joinGroupCredential;
    }


    //Method to check if the user has already  joined the group method.
    public boolean alreadyInGroup() throws SQLException{
        String groupToJoin = joinGroupName.getText();
        String databaseGroup;
        boolean userIsAlreadyInGroup=false;
        try {
            String groupQuery="select groups.groupname from groups INNER JOIN groupsmember ON groups.groupid=groupsmember.groupid where groupsmember.userid=?";
            PreparedStatement pst2= DBsession.INSTANCE.OpenConnection().prepareStatement(groupQuery);
            pst2.setInt(1, User.INSTANCE.getUserid());
            ResultSet rs2 = pst2.executeQuery();
            while(rs2.next()) {
                databaseGroup = rs2.getString("groupname");
                if (databaseGroup.contentEquals(groupToJoin)) {
                    userIsAlreadyInGroup = true;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return userIsAlreadyInGroup;
    }


    public void logout(ActionEvent actionEvent) throws IOException {
        int logoutOpt = JOptionPane.showConfirmDialog(null,"Are you sure you want to Log out?");
        if(logoutOpt==JOptionPane.YES_OPTION){
            BaseController.Instance.Switch(actionEvent,"FXML/LoginPage.fxml");
        }
    }
}


/*
        public void EditGroupCommands() {
        nameOfGroup = groupName.getText();
        String groupEmail = groupMembersEmail.getText();

        if(){

        }

         */


















