import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class GroupsPageController extends BaseController {

    public Button createGroupbtn;
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

    public void createGroup(){

        String nameOfGroup = groupNameTextField.getText();
        String groupMail = groupMembersEmail.getText();
        String groupPass = groupPassword.getText();
        User groupAdmin = user;
        Group newGroup = new Group(nameOfGroup, groupAdmin, groupPass);

        if(nameOfGroup.isEmpty() || groupMail.isEmpty()){
            System.out.println("Please fill in all details");
        }
        else{
            user.addGroup(newGroup);
            //SendMail.sendMail(groupMail, nameOfGroup);
        }

    }


}
