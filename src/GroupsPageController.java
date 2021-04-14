import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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

            populateGroupTable();

        });
    }

    //Method to populate the group table when the page is rendered
    public void populateGroupTable(){

        //Get the attributes for the table from default getters and setters
        groupName.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        groupMembers.setCellValueFactory(new PropertyValueFactory<>("groupMembers"));

        //Populate the table data
        tbData.setItems(getGroups());
    }

    //Create an observable list that can be rendered by javafx with the data inside
    private ObservableList<Group> getGroups(){
        ObservableList<Group> groups = FXCollections.observableArrayList();
        System.out.println(user);
        System.out.println(user.getGroups().get(0).getGroupName());
        groups.addAll(user.getGroups());
        return groups;
    }

    public void createGroup() throws IOException {

        String nameOfGroup = groupNameTextField.getText();
        String groupMail = groupMembersEmail.getText();
        User groupAdmin = user;
        String groupPass = User.toDatabaseString(user);
        Group newGroup = new Group(nameOfGroup, groupAdmin, groupPass);

        if(nameOfGroup.isEmpty() || groupMail.isEmpty()){
            System.out.println("Please fill in all details");
        }
        else{
            user.addGroup(newGroup);
            //SendMail.sendMail(groupMail, nameOfGroup);
        }

    }


    public void editGroup(ActionEvent actionEvent) throws IOException {
        String groupName = editNameTextField.getText();
        String addEmail = editGroupEmailAddTextField.getText();
        String removeEmail = editGroupEmailRemoveTextField.getText();

        for(int i = 0; i < user.getGroups().size(); i++){
            if(user.getGroups().get(i).getGroupName().equals(groupName) && user.getGroups().get(i).getGroupAdmin() == user){

                if(addEmail != null){
                    String password = User.toDatabaseString(user);
                    //TODO email to user with the password
                    //user.getGroups().get(i).sendNewMemberEmail(addEmail);
                }

                if(removeEmail != null){
                    user.getGroups().get(i).removeUser(removeEmail);
                }



            }
        }

        if(addEmail != null){

        }
    }

    public void joinGroup(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        String groupName = groupNameToJoinTextField.getText();
        String groupPassword = groupToJoinPassword.getText();

        User prospectiveAdmin = (User) User.fromDatabaseString(groupPassword);
        String adminUsername = prospectiveAdmin.getUsername();

        String SQL_QUERY = "SELECT userObject FROM users WHERE username = ?";

        String userObject = null;

        try{

            //Create a new db connection
            Connection connection = DatabaseConnect.connect();
            PreparedStatement pst = connection.prepareStatement(SQL_QUERY);

            pst.setString(1, adminUsername);

            ResultSet rs=pst.executeQuery();

            //Loop through db results
            while(rs.next()) {

                //Set username and password variables
                userObject=rs.getString("userObject");

                    //If the user exists, retrieve their object from the db
                    User currentAdminObject = (User) User.fromDatabaseString(userObject);

                    Goal groupGoalToAdd = null;
                ArrayList<User> groupMembersToAdd = new ArrayList<>();


                    for(int i = 0; i < currentAdminObject.getGroups().size(); i++){
                        if(groupName.equals(currentAdminObject.getGroups().get(i).getGroupName())){
                            currentAdminObject.getGroups().get(i).addGroupMember(user);
                            groupGoalToAdd = currentAdminObject.getGroups().get(i).getGroupGoal();
                            groupMembersToAdd = currentAdminObject.getGroups().get(i).getGroupMembersList();
                        }
                    }

                Group groupToAdd = new Group(groupName, currentAdminObject, groupMembersToAdd, groupGoalToAdd);

                    user.addGroup(groupToAdd);

                try {
                    //Create a new db connection
                    Connection connection2 = DatabaseConnect.connect();

                    //SQL query to add user information to db
                    String SQL_INSERT="UPDATE users SET userObject = (?)";
                    PreparedStatement pst2 = connection.prepareStatement(SQL_INSERT);

                    pst2.setString(1, User.toDatabaseString(currentAdminObject));


                    pst2.executeUpdate();

                    connection2.close();

                } catch (Exception e) {
                    System.out.println(e);
                }


            }

                //Close the db connection
                connection.close();
            } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for(int i = 0; i < user.getGroups().size(); i++){
            if(user.getGroups().get(i).getGroupName().equals(groupName)){
                for(int j = 0; j < user.getGroups().get(i).getGroupMembersList().size();j++){
                    if(user.getGroups().get(i).getGroupMembersList().get(j).getGroups().get(j).getGroupName().equals(groupName)){
                        user.getGroups().get(i).getGroupMembersList().get(j).getGroups().get(j).addGroupMember(user);

                        //TODO add user to the database once their object has been updated by the user being added to the group

                    }
                }
            }
        }
    }
}
