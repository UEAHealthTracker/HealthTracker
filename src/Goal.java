import java.util.Date;

public class Goal {
    public static final Goal Instance=new Goal();
    public String getGoalname() {
        return goalname;
    }

    public void setGoalname(String goalname) {
        this.goalname = goalname;
    }



    public String getGoaldate() {
        return goaldate;
    }

    public void setGoaldate(String goaldate) {
        this.goaldate = goaldate;
    }

    public String getGoalstatus() {
        return goalstatus;
    }

    public void setGoalstatus(String goalstatus) {
        this.goalstatus = goalstatus;
    }

    public String getGoalgroups() {
        return goalgroups;
    }

    public void setGoalgroups(String goalgroups) {
        this.goalgroups = goalgroups;
    }

    public Goal(Integer goalid,String goalname, String goaldate, String goalstatus, String goalgroups) {
        this.goalid = goalid;
        this.goalname = goalname;
        this.goaldate = goaldate;
        this.goalstatus = goalstatus;
        this.goalgroups = goalgroups;
    }
    public Goal() { }

    public Integer getGoalid() {
        return goalid;
    }

    public void setGoalid(Integer goalid) {
        this.goalid = goalid;
    }

    Integer goalid;
    String goalname;
    String goaldate;
    String goalstatus;
    String goalgroups;
}
