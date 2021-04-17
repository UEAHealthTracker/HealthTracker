import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GroupsPageController extends BaseController {

    public Button createGroupbtn;
    public TextField editNameTextField;
    public TextField editGroupEmailAddTextField;
    public TextField editGroupEmailRemoveTextField;
    public TextField groupNameToJoinTextField;
    public TextField groupToJoinPassword;
    @FXML
    TextField groupNameTextField;

    @FXML
    TextField groupMembersEmail;

    @FXML
    TextField groupPassword;

    @FXML
    Button inviteMember;

    @FXML
    Button createGroupButton;

    @FXML
    ImageView imageView;

    String nameOfGroup, groupMail;

    @FXML
    private TableView<Group> tbData;

    @FXML
    public TableColumn<Group, String> groupName;

    @FXML
    public TableColumn<Group, String> groupMembers;

    public void initialize(){
        Platform.runLater(() -> {

            try {
                populateGroupTable();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });
    }

    //Method to populate the group table when the page is rendered
    public void populateGroupTable() throws SQLException {

        //Get the attributes for the table from default getters and setters
        groupName.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        groupMembers.setCellValueFactory(new PropertyValueFactory<>("groupMembers"));

        //Populate the table data
        tbData.setItems(getGroups());
    }

    //Create an observable list that can be rendered by javafx with the data inside
    private ObservableList<Group> getGroups() throws SQLException {
        ObservableList<Group> groups = FXCollections.observableArrayList();
        System.out.println(user);
        groups.addAll(user.getGroups());
        return groups;
    }

    public void createGroup(ActionEvent actionEvent) throws IOException {

        String nameOfGroup = groupNameTextField.getText();
        String groupMail = groupMembersEmail.getText();
        String groupPass = groupPassword.getText();
        Group newGroup = new Group(nameOfGroup, user, groupPass);

        if(nameOfGroup.isEmpty() || groupMail.isEmpty()){
            System.out.println("Please fill in all details");
        }
        else{
            Integer groupReference = Group.createGroup(newGroup);
            user.addGroup(groupReference);
            //SendMail.sendMail(groupMail, nameOfGroup);
        }

        loadPage(actionEvent, "GroupsPage.fxml");

    }


    public void editGroup(ActionEvent actionEvent) throws IOException, SQLException {
        String groupName = editNameTextField.getText();
        String addEmail = editGroupEmailAddTextField.getText();
        String removeEmail = editGroupEmailRemoveTextField.getText();

        for(int i = 0; i < user.getGroupsIds().size(); i++){

            Group group = user.getGroupFromId(user.getGroupsIds().get(i));

            if(group.getGroupName().equals(groupName) && group.getGroupAdmin() == user){

                if(addEmail != null){
                    String password = User.toDatabaseString(user);
                    //TODO email to user with the password
                    //user.getGroups().get(i).sendNewMemberEmail(addEmail);
                }

                if(removeEmail != null){
                    group.removeUser(removeEmail);
                }



            }
        }

        loadPage(actionEvent, "GroupsPage.fxml");
    }

    public void joinGroup(ActionEvent actionEvent) throws IOException, ClassNotFoundException, SQLException {
        String groupName = groupNameToJoinTextField.getText();
        String groupPassword = groupToJoinPassword.getText();

        Group groupToJoin = null;

        String SQL_QUERY = "SELECT groupObject FROM groups";

        try{

            //Create a new db connection
            Connection connection = DatabaseConnect.connect();
            PreparedStatement pst = connection.prepareStatement(SQL_QUERY);

            ResultSet rs=pst.executeQuery();

            //Loop through db results
            while(rs.next()) {

                //Set username and password variables
                String groupString = rs.getString("groupObject");

                Group group = (Group) Group.fromDatabaseString(groupString);

                if(group.getGroupName().equals(groupName) && group.getGroupPassword().equals(groupPassword)){
                    groupToJoin = group;
                }

            }

                //Close the db connection
            connection.close();
            } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Integer groupReference = Group.getGroupByObject(groupToJoin);



        user.addGroup(groupReference);
        groupToJoin.addGroupMember(user);

        try {
            //Create a new db connection
            Connection connection = DatabaseConnect.connect();

            //SQL query to add user information to db
            String SQL_INSERT="UPDATE groups SET groupObject = (?) WHERE groupid = (?)";
            PreparedStatement pst = connection.prepareStatement(SQL_INSERT);

            pst.setString(1, Group.toDatabaseString(groupToJoin));
            pst.setString(2, groupReference.toString());


            pst.executeUpdate();

            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        loadPage(actionEvent, "GroupsPage.fxml");

    }
}
