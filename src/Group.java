import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.RandomAccess;

public class Group implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String groupName;
    private User groupAdmin;
    private String groupPassword;

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

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < groupMembers.size(); i++){
            stringBuilder.append(groupMembers.get(i).getName());
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
}
