import java.util.ArrayList;

public class Group {
    private String groupName;
    private String groupAdmin;
    private String groupPassword;
    String memberName;

    public Group(String groupName, String groupAdmin, String groupPassword){
        this.groupName=groupName;
        this.groupAdmin=groupAdmin;
        this.groupPassword=groupPassword;

    }

    public Group(String groupName, String groupAdmin, String groupPassword, ArrayList groupMembers){
        this.groupName=groupName;
        this.groupAdmin=groupAdmin;
        this.groupPassword=groupPassword;
        this.groupMembers= groupMembers;


    }



    public String getGroupName() {
        return groupName;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupAdmin() {
        return groupAdmin;
    }

    public String setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
        return groupAdmin;
    }

    public Goal getGroupGoal() {
        return groupGoal;
    }

    public void setGroupGoal(Goal groupGoal) {
        this.groupGoal = groupGoal;
    }

    private ArrayList groupMembers;
    private Goal groupGoal;
    private int groupId;

    public void addGroupMember(String memberName){
        groupMembers.add(memberName);

    }

    public String getGroupMembers(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<groupMembers.size(); i++){
            stringBuilder.append(groupMembers.get(i));
            if(i!=groupMembers.size()-1) {
                stringBuilder.append(", ");
            }
        }
       return  stringBuilder.toString();

    }


    public void deleteGroupMember(User user){
        groupMembers.remove(user);
    }

    public void emailGroupGoal(){
            for(int i=0; i<groupMembers.size(); i++) {
                User newMember= new User();
                newMember.setUsername(groupMembers.get(i).toString());
                String email = newMember.getEmail();
                //TODO Create email class with methods
                // Email.sendGroupGoalUpdate(email);
            }
    }

    public void deleteGroupGoal(){
        groupGoal = null;
    }
}
