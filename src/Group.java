import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.RandomAccess;

public class Group implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String groupName;
    private User groupAdmin;
    private String groupPassword;

    public static Integer createGroup(Group groupToAdd) {

        String groupid = null;

        try {
            //Create a new db connection
            Connection connection = DatabaseConnect.connect();

            //SQL query to add user information to db
            String SQL_INSERT="INSERT INTO groups(groupObject) VALUES (?)";


            PreparedStatement pst = connection.prepareStatement(SQL_INSERT);

            pst.setString(1, Group.toDatabaseString(groupToAdd));



            pst.executeUpdate();


        } catch (Exception e) {
            System.out.println(e);
        }

        return Group.getGroupByObject(groupToAdd);

    }

    public static Integer getGroupByObject(Group groupToJoin) {
        String groupid = null;

        String SQL_QUERY = "SELECT groupid FROM groups WHERE groupObject = ?";

        try{

            //Create a new db connection
            Connection connection = DatabaseConnect.connect();
            PreparedStatement pst = connection.prepareStatement(SQL_QUERY);

            pst.setString(1, Group.toDatabaseString(groupToJoin));

            ResultSet rs=pst.executeQuery();

            //Loop through db results
            while(rs.next()) {

                //Set username and password variables
                groupid = rs.getString("groupid");
            }

        }
        catch(Exception e){
            System.out.println(e);
        }

        return Integer.parseInt(groupid);
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    public ArrayList<User> getGroupMembersList(){
        return groupMembers;
    }

    private ArrayList<User> groupMembers = new ArrayList<>();
    private Goal groupGoal;

    public Group(String nameOfGroup, User groupAdmin, String groupPassword) {
        this.groupName = nameOfGroup;
        this.groupAdmin = groupAdmin;
        this.groupPassword = groupPassword;
        this.addGroupMember(groupAdmin);
    }

    public Group(String groupName, User groupAdmin, ArrayList<User> groupMembers, Goal groupGoal){
        this.groupName = groupName;
        this.groupAdmin = groupAdmin;
        this.groupMembers = groupMembers;
        this.groupGoal = groupGoal;
    }

    public String getGroupMembers() {

        System.out.println(groupMembers.toString());

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < groupMembers.size(); i++){
            stringBuilder.append(groupMembers.get(i).getUsername());
        }
        return stringBuilder.toString();
    }

    public void setGroupMembers(ArrayList<User> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public User getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(User groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public Goal getGroupGoal() {
        return groupGoal;
    }

    public void setGroupGoal(Goal groupGoal) {
        this.groupGoal = groupGoal;
    }

    public void addGroupMember(User user){
        groupMembers.add(user);
    }

    public void deleteGroupMember(User user){
        groupMembers.remove(user);
    }

    public void emailGroupGoal(){
        for (User groupMember : groupMembers) {
            String email = groupMember.email;
            //TODO Create email class with methods
            //Email.sendGroupGoalUpdate(email);
        }
    }

    public void deleteGroupGoal(){
        groupGoal = null;
    }

    public void removeUser(String removeEmail) {
        for(int i = 0; i < groupMembers.size(); i++){
            if(groupMembers.get(i).getEmail().equals(removeEmail)){
                groupMembers.remove(groupMembers.get(i));
            }
        }
    }

    static String toDatabaseString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    static Object fromDatabaseString( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }
}
