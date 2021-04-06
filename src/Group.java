import java.util.ArrayList;

public class Group {


    private String groupName;
    private String groupAdmin;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public Goal getGroupGoal() {
        return groupGoal;
    }

    public void setGroupGoal(Goal groupGoal) {
        this.groupGoal = groupGoal;
    }

    private ArrayList<User> groupMembers;
    private Goal groupGoal;
    private int groupId;

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
           // Email.sendGroupGoalUpdate(email);
        }
    }

    public void deleteGroupGoal(){
        groupGoal = null;
    }
}
