import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class EditGroupController extends BaseController {
    private ObservableList<Group> data;
    @FXML
    TableView<Group> groupView;

    @FXML
    TableColumn<Group, String> groupname;
    @FXML
    TableColumn<Group, String> groupMembers;
    @FXML
    Label userLabel;
    @FXML
    TextField EditGroupName;

    @FXML
    ComboBox addMemberMail;

    @FXML
    TextField removeMemberMail;
    @FXML
    TextField GroupMessage;

    static Group selectedGroup = null;
    ArrayList membersName= getGroupMember();
    public void initialize() throws IOException {

        userLabel.setText("Hello " + User.INSTANCE.getUsername());
        EditGroupName.setText(getGroupForEdit());
        for(int i=0; i<membersName.size(); i++){
            System.out.println(membersName.get(i));
            addMemberMail.setVisibleRowCount(membersName.size());
            addMemberMail.setValue(membersName.get(i));
        }




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

    public String getGroupForEdit() throws IOException {
        String getGroupName=null;
        if (groupView.getSelectionModel().getSelectedItem() != null) {
            selectedGroup = groupView.getSelectionModel().getSelectedItem();
            getGroupName = selectedGroup.getGroupName();
        }
        return getGroupName;
    }
    public ArrayList getGroupMember(){
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
    public void mainEditGroup(ActionEvent actionEvent) throws IOException {
        System.out.println("Heyy!");
        /*
        root = FXMLLoader.load(getClass().getResource("FXML/EditGroupPage.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

         */
    }


    public void EditAddMember(ActionEvent actionEvent) throws SQLException, IOException, MessagingException {
        String editGroupName = EditGroupName.getText();
        boolean inGroup=false;
        String addMember = addMemberMail.getValue().toString();
        int editGroupId, editUserId;
        String editPassword, editAdmin;

        try {
            if (editGroupName.isEmpty() || addMember.isEmpty()) {
                System.out.println("Please fill in group name and add member details.");
                GroupMessage.setOpacity(1);
                GroupMessage.setText("Please fill in group name and  add member details");
            } else {

                if (GroupsPageController.userExist(addMember)){
                    System.out.println(addMember + " exists");
                    System.out.println("User and group Exist");
                    if (GroupsPageController.groupExist(editGroupName)) {
                        //Get group id and user id:
                        String groupIdQuery = "SELECT groups.groupid, groups.groupPassword, groups.groupadmin FROM groups INNER JOIN groupsmember ON groupsmember.groupid=groups.groupid WHERE groups.groupname=? AND  groupsmember.userid=?";
                        PreparedStatement groupIdStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(groupIdQuery);
                        groupIdStatement.setString(1, editGroupName);
                        groupIdStatement.setInt(2, User.INSTANCE.getUserid());
                        ResultSet result = groupIdStatement.executeQuery();
                        while (result.next()) {
                            inGroup = true;
                            //System.out.println("Something");
                            editGroupId = result.getInt("groupid");
                            editPassword = result.getString("groupPassword");
                            editAdmin = result.getString("groupadmin");

                            System.out.println(editPassword);
                            System.out.println(editGroupId);

                            if (inGroup) {
                                String userIdQuery = "SELECT userid FROM Users WHERE email=? ";
                                PreparedStatement userId = DBsession.INSTANCE.OpenConnection().prepareStatement(userIdQuery);
                                userId.setString(1, addMember);
                                ResultSet result2 = userId.executeQuery();
                                while (result2.next()) {
                                    editUserId = result2.getInt("userid");
                                    System.out.println(editUserId);
                                    if (SendMail.sendMail(addMember, editGroupName, editPassword)) {
                                        String insertQuery = "INSERT INTO group_invites(group_id, group_name,group_admin, group_member) VALUES(?,?,?, ?)";
                                        PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(insertQuery);
                                        pst.setInt(1, editGroupId);
                                        pst.setString(2, editGroupName);
                                        pst.setString(3, editAdmin);
                                        pst.setString(4, addMember);
                                        pst.executeUpdate();
                                        GroupMessage.setOpacity(1);
                                        GroupMessage.setText("Email invitation has been sent to: " + addMember);
                                        //Insert into group invites after mail has been sent:
                                    }
                                }
                            }else {
                                GroupMessage.setOpacity(1);
                                GroupMessage.setText("Not in group called: " + editGroupName);

                            }
                        }
                    } else {
                        GroupMessage.setOpacity(1);
                        GroupMessage.setText("Group named : " + editGroupName + " does not exist");
                    }
                }else{
                    GroupMessage.setOpacity(1);
                    GroupMessage.setText("User named : " + addMember + " does not exist");
                    System.out.println(addMember + " exists");
                }

            }
        }catch(SQLException e){
            e.printStackTrace();

        }
    }




    public void removeMember(ActionEvent actionEvent) throws SQLException {
        String editGroupName = EditGroupName.getText();
        String memberEmail = removeMemberMail.getText();
        String resultAdmin;
        boolean isInInGroup=false;
        //Check if the user is admin of the group
        try {
            if (editGroupName.isEmpty() || memberEmail.isEmpty()) {
                System.out.println("Please fill in group name and add member details.");
                GroupMessage.setOpacity(1);
                GroupMessage.setText("Please fill in group name and  add member details");
            } else {
                if (GroupsPageController.userExist(memberEmail)) {
                    if (GroupsPageController.groupExist(editGroupName)) {
                        String Adminquery = "SELECT groupadmin FROM groups WHERE groupname =?";
                        PreparedStatement checkAdmin = DBsession.INSTANCE.OpenConnection().prepareStatement(Adminquery);
                        checkAdmin.setString(1, editGroupName);
                        ResultSet result = checkAdmin.executeQuery();

                        while(result.next()) {
                            resultAdmin = result.getString("groupadmin");
                            System.out.println(resultAdmin);
                            System.out.println("Query working");
                            if (resultAdmin.contentEquals(User.INSTANCE.getUsername())) {

                                System.out.println("You are admin");
                                //Remove member
                                String removeQuery = "SELECT groups.groupid, Users.userid FROM groups INNER JOIN groupsmember ON groupsmember.groupid=groups.groupid INNER JOIN Users ON Users.userid = groupsmember.userid  WHERE groups.groupname=? AND Users.email=?";
                                PreparedStatement removeStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(removeQuery);
                                removeStatement.setString(1, editGroupName);
                                removeStatement.setString(2, memberEmail);
                                ResultSet queryResult = removeStatement.executeQuery();
                                while (queryResult.next()) {
                                    int groupId = queryResult.getInt("groupid");
                                    System.out.println(groupId);
                                    int userId = queryResult.getInt("userid");
                                    System.out.println(userId);
                                    //Remove member:
                                    String removeMemberQuery = "DELETE FROM groupsmember WHERE groupid=? AND userid=?";
                                    PreparedStatement deleteStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(removeMemberQuery);
                                    deleteStatement.setInt(1, groupId);
                                    deleteStatement.setInt(2, userId);
                                    deleteStatement.executeUpdate();
                                    GroupMessage.setOpacity(1);
                                    GroupMessage.setText(memberEmail + " has been deleted from the group");
                                    root = FXMLLoader.load(getClass().getResource("FXML/GroupsPage.fxml"));
                                    stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                    //Delete invite to stop user from re-joining the group:
                                    String removeInvite = "DELETE FROM group_invites WHERE group_id=? AND group_member=?";
                                    PreparedStatement deleteInvite = DBsession.INSTANCE.OpenConnection().prepareStatement(removeInvite);
                                    deleteInvite.setInt(1, groupId);
                                    deleteInvite.setString(2, memberEmail);
                                    deleteInvite.executeUpdate();
                                    System.out.println("Invite has been deleted. User cannot join the group");
                                }
                            } else {
                                GroupMessage.setOpacity(1);
                                GroupMessage.setText("Can't remove member because you are not admin");
                            }

                        }
                    }else{
                        GroupMessage.setOpacity(1);
                        GroupMessage.setText("Group typed does not exist");
                    }
                }else{
                    GroupMessage.setOpacity(1);
                    GroupMessage.setText("User typed does not exist");

                }
            }
        }catch(SQLException | IOException e){
            e.printStackTrace();

        }
    }

}
