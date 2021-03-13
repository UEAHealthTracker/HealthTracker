import java.util.ArrayList;
import java.util.Date;

public class Workout {

    private String ExerciseName;
    private int Sets;
    private int Reps;
    private int calorieCount;
    private String WeekDay;

    public Workout() {
        this.ExerciseName="";
        this.Reps = 0;
        this.Sets = 0;
        this.calorieCount = 0;
        this.WeekDay="";
    }

    public void addExercise(String exercise)
     {
        this.ExerciseName=exercise;
     }

    public void deleteExercise()
    {

    }
    public void changeWeekDay(String day)
    {
        this.WeekDay=day;
    }



    //Testing harness
    public static void main(String[] args) {


    }
}
