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
import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditGroupPageController extends BaseController {
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
    TextField addMemberMail;

    //This is the combox box to remove member
    @FXML
    ComboBox removeMemberMail;
    @FXML
    Label GroupMessage;

    static Group selectedGroup = null;

    GroupsPageController groupsDetail = new GroupsPageController();
    //String groupName= groupsDetail.getGroupForEdit();

    //ArrayList allMembers = groupsDetail.getGroupMember();

    public EditGroupPageController() throws IOException {
    }

    //ArrayList membersName= getGroupMember();
    public void initialize() throws IOException {
        userLabel.setText("Hello " + User.INSTANCE.getUsername());
        String initialiseQuery = "SELECT groups.groupname, Users.username FROM groups INNER JOIN groupsmember ON groupsmember.groupid=groups.groupid INNER JOIN Users ON Users.userid = groupsmember.userid  WHERE groups.groupname=?";
        try {
            PreparedStatement sel = DBsession.INSTANCE.OpenConnection().prepareStatement(initialiseQuery);
            System.out.println(Group.Instance.getGroupName());
            // sel.setInt(1,Integer.parseInt(String.valueOf(User.INSTANCE.getUserid())));
            sel.setString(1, (Group.Instance.getGroupName()));
            ResultSet results = sel.executeQuery();
            String groupName ="";
            while(results.next()){
                groupName = results.getString("groupname");
                EditGroupName.setText(groupName);
                String username = results.getString("username");
                removeMemberMail.getItems().add(username);
                //System.out.println(username);
                //System.out.println(groupName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }




    public void updateMessage(String message, String title){
        JOptionPane.showMessageDialog(null, message, title,JOptionPane.INFORMATION_MESSAGE);
    }


    public void EditAddMember(ActionEvent actionEvent) throws SQLException, IOException, MessagingException {
        String editGroupName = EditGroupName.getText();
        String addMember = addMemberMail.getText();
        boolean inGroup=false;
        int editGroupId, editUserId;
        String editPassword, editAdmin;

        try {
            if (editGroupName.isEmpty() || addMember.isEmpty()) {
                System.out.println("Please fill in group name and add member details.");
                GroupMessage.setOpacity(1);
                GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
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
                                        JOptionPane.showMessageDialog(null, "Email invitation has been sent to: " + addMember, "Successful member invite!", JOptionPane.INFORMATION_MESSAGE);
                                        //updateMessage(" Email Inviation has been sent to "+ addMember, "invite sent");
                                        //Insert into group invites after mail has been sent:
                                    }
                                }
                            }else {
                                GroupMessage.setOpacity(1);
                                GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                                GroupMessage.setText("Not in group called: " + editGroupName);

                            }
                        }
                    } else {
                        GroupMessage.setOpacity(1);
                        GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                        GroupMessage.setText("Group named : " + editGroupName + " does not exist");
                    }
                }else{
                    GroupMessage.setOpacity(1);
                    GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                    GroupMessage.setText("User named : " + addMember + " does not exist");
                    //System.out.println(addMember + " not exists");
                }

            }
        }catch(SQLException e){
            e.printStackTrace();

        }
    }




    public void removeMember(ActionEvent actionEvent) throws SQLException {
        String editGroupName = EditGroupName.getText();

        String resultAdmin;
        boolean isInInGroup=false;
        //Check if the user is admin of the group
        try {
            if (editGroupName.isEmpty() || removeMemberMail.getValue()==null) {
                System.out.println("Please fill in group name and add member details.");
                GroupMessage.setOpacity(1);
                GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                GroupMessage.setText("Please fill in group name and  add member details");
            } else {
                String memberUserName = (String) removeMemberMail.getValue();
                if (GroupsPageController.usernameExist(memberUserName)) {
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
                                //If user is admin:

                                System.out.println("You are admin");
                                //Remove member
                                String removeQuery = "SELECT groups.groupid, Users.userid, Users.email FROM groups INNER JOIN groupsmember ON groupsmember.groupid=groups.groupid INNER JOIN Users ON Users.userid = groupsmember.userid  WHERE groups.groupname=? AND Users.username=?";
                                PreparedStatement removeStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(removeQuery);
                                removeStatement.setString(1, editGroupName);
                                removeStatement.setString(2, memberUserName);
                                ResultSet queryResult = removeStatement.executeQuery();
                                while (queryResult.next()) {
                                    String membersMail= queryResult.getString("email");
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
                                    JOptionPane.showMessageDialog(null, memberUserName + " has been deleted from the group", "Successful delete member", JOptionPane.INFORMATION_MESSAGE);
                                    root = FXMLLoader.load(getClass().getResource("FXML/GroupsPage.fxml"));
                                    stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                    //Delete invite to stop user from re-joining the group:
                                    String removeInvite = "DELETE FROM group_invites WHERE group_id=? AND group_member=?";
                                    PreparedStatement deleteInvite = DBsession.INSTANCE.OpenConnection().prepareStatement(removeInvite);
                                    deleteInvite.setInt(1, groupId);
                                    deleteInvite.setString(2, membersMail);
                                    deleteInvite.executeUpdate();
                                    System.out.println("Invite has been deleted. User cannot join the group");
                                }
                            } else {
                                //If you are not admin but are trying to remove yourself from the group.
                                GroupMessage.setOpacity(1);
                                GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                                GroupMessage.setText("Can't remove member because you are not admin");
                            }

                        }
                    }else{
                        GroupMessage.setOpacity(1);
                        GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                        GroupMessage.setText("Group typed does not exist");
                    }
                }else{
                    GroupMessage.setOpacity(1);
                    GroupMessage.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-text-fill: #ff0000");
                    GroupMessage.setText("User typed does not exist");

                }
            }
        }catch(SQLException | IOException e){
            e.printStackTrace();

        }
    }

    public void pageInfo(){
        Alert deleteAlert =  new Alert(Alert.AlertType.INFORMATION," To Invite Member: Fill group name and member email and click invite member button\n"  +
                "To Delete Member: Type group name, select member from list and click remove member  \n" + "Click add button for more functionalities including change group name, password, and/or admin \n" +
                 "   ",ButtonType.OK);
        deleteAlert.show();
    }

    public int moreEditOptions() throws SQLException {
        int response = 10;
        String SQL_QUERY = "SELECT  groupadmin FROM groups WHERE groupname=?";
        PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_QUERY);
        pst.setString(1, EditGroupName.getText());
        ResultSet result = pst.executeQuery();
        while (result.next()) {
            String groupAdmin = result.getString("groupadmin");
            System.out.println("Admin is: " + groupAdmin);
            System.out.println("Username is: " + User.INSTANCE.getUsername());
            if (groupAdmin.contentEquals(User.INSTANCE.getUsername())) {
                //Check if user is admin of group.
                String[] finalResponse = {"Group name", "Group password", "Group Admin"};
                response = JOptionPane.showOptionDialog(null, "Choose what to edit. Change", "Additional edit options", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, finalResponse, finalResponse[0]);
                System.out.println(response);

            }else{
                JOptionPane.showMessageDialog(null, "Uh-oh! You aren't admin and thus can't change group name, password, or admin", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return response;
    }

    public void updateGroupName(String newGroupName) throws  SQLException {
        boolean updateComplete = false;

            if (GroupsPageController.groupExist(newGroupName) == false) {
                String update = "UPDATE groups SET groupname =? WHERE groupname=?";
                PreparedStatement updateStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(update);
                updateStatement.setString(1, newGroupName);
                updateStatement.setString(2, EditGroupName.getText());
                updateStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Group name has been changed to " + newGroupName, "Successful name change", JOptionPane.INFORMATION_MESSAGE);
                DBsession.INSTANCE.OpenConnection().close();
            } else {
                JOptionPane.showMessageDialog(null, "Uh-oh!, name is already taken. Try again.", "Error", JOptionPane.ERROR_MESSAGE);

            }


    }

    public void updateGroupPassword(String newGroupPassword) throws  SQLException{
        String update = "UPDATE groups SET groupPassword =? WHERE groupname=?";
        PreparedStatement updateStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(update);
        updateStatement.setString(1, newGroupPassword);
        updateStatement.setString(2, EditGroupName.getText());
        updateStatement.executeUpdate();
        DBsession.INSTANCE.OpenConnection().close();
        JOptionPane.showMessageDialog(null, "Group password has been changed to " + newGroupPassword, "Successful password change", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateGroupAdmin(String newAdmin) throws SQLException {
        String update = "UPDATE groups SET groupadmin =? WHERE groupname=?";
        PreparedStatement updateStatement = DBsession.INSTANCE.OpenConnection().prepareStatement(update);
        updateStatement.setString(1, newAdmin);
        updateStatement.setString(2, EditGroupName.getText());
        updateStatement.executeUpdate();
        DBsession.INSTANCE.OpenConnection().close();
        JOptionPane.showMessageDialog(null, "Group admin has been changed to " + newAdmin, "Successful admin change", JOptionPane.INFORMATION_MESSAGE);
    }


    public void furtherEdits() throws SQLException {
        int editResponse= moreEditOptions();
        //System.out.println(editResponse);
        switch (editResponse){
            case 0:
                String newGroupName = JOptionPane.showInputDialog("Type new group name");
                updateGroupName(newGroupName);
                break;
            case 1:
                String newGroupPassword = JOptionPane.showInputDialog("Type new group password");
                updateGroupPassword(newGroupPassword);
                break;
            case 2:
                //Object options = new String[10];
                List<String> optionList = new ArrayList<String>();

                //String[] options = EditGroupName.getText().getGroupMembers().split(",");
                for(int i=0; i<removeMemberMail.getItems().size(); i++) {
                     optionList.add((String) removeMemberMail.getItems().get(i));
                }
                String options[] = optionList.toArray( new String[optionList.size()]);
                //System.out.println(options[0]);
                //System.out.println(options[1]);

                ImageIcon icon = new ImageIcon("src/img/smallgroupadd.png");
               String newAdmin = (String)JOptionPane.showInputDialog(null, "Select New Admin",
                        "Choosing New Admin", JOptionPane.QUESTION_MESSAGE, icon, options, options[0]);
                //System.out.println("Potential admin: " + newAdmin.trim());
                updateGroupAdmin(newAdmin.trim());
                break;

        }
    }



}
