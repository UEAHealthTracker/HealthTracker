import java.util.Date;

public class Goal {
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

    public Integer getGoalgroups() {
        return goalgroups;
    }

    public void setGoalgroups(Integer goalgroups) {
        this.goalgroups = goalgroups;
    }

    public Goal(String goalname, String goaldate, String goalstatus, Integer goalgroups) {
        this.goalname = goalname;
        this.goaldate = goaldate;
        this.goalstatus = goalstatus;
        this.goalgroups = goalgroups;
    }

    String goalname;
    String goaldate;
    String goalstatus;
    Integer goalgroups;
}
