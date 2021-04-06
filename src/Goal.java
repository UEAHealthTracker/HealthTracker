import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

//Implement serial class as the goal object needs to be stored as part of a list in the user object
public class Goal implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    String goalName;
    LocalDate goalStartDate;
    LocalDate goalEndDate;
    String goalStatus;

    //Constructor for the goal class
    public Goal(String goalName, LocalDate goalStartDate, LocalDate goalEndDate, String goalStatus) {
        this.goalName = goalName;
        this.goalStartDate = goalStartDate;
        this.goalEndDate = goalEndDate;
        this.goalStatus = goalStatus;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public LocalDate getGoalDate() {
        return goalStartDate;
    }

    public void setGoalDate(LocalDate goalDate) {
        this.goalStartDate = goalDate;
    }

    public String getGoalStatus() {
        return goalStatus;
    }

    public void setGoalStatus(String goalStatus) {
        this.goalStatus = goalStatus;
    }

}
