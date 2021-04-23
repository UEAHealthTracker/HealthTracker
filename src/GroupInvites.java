import java.util.ArrayList;

public class GroupInvites {
    private String groupAdmin;
    private String groupName;
    private String groupMember;
    private ArrayList<GroupInvites> groupInvites = new ArrayList();

    public GroupInvites(String groupAdmin, String groupName, String groupMember) {
        this.groupAdmin = groupAdmin;
        this.groupName = groupName;
        this.groupMember = groupMember;
    }
    public GroupInvites(){};
    public String getGroupAdmin(){
        return groupAdmin;
    }
    public String getGroupName(){
        return groupName;
    }

    public String getGroupMember(){
        return groupMember;
    }

    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupMember(String groupMember) {
        this.groupMember = groupMember;
    }

    public ArrayList<GroupInvites> getGroupInvites() {
        return groupInvites;
    }


    public void  addGroupInvites(GroupInvites newInvites) {
       groupInvites.add(newInvites);
    }



    public void setGroupInvites(ArrayList<GroupInvites> groupInvites) {
        this.groupInvites = groupInvites;
    }

    @Override
    public String toString() {
        return "GroupInvites{" +
                "groupAdmin='" + groupAdmin + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupMember='" + groupMember + '\'' +
                '}';
    }
}

