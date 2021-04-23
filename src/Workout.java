import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Workout {



  /*  public Workout(int workoutIDtb, int caloriestb, int durationtb,String workoutTypetb) {
        this.workoutIDtb = new SimpleIntegerProperty(workoutIDtb) ;
        this.caloriestb = new SimpleIntegerProperty(caloriestb) ;
        this.durationtb = new SimpleIntegerProperty(durationtb);
        this.workoutTypetb = new SimpleStringProperty (workoutTypetb) ;

    }*/

    public Workout() {

    }

   /* public WorkoutType getWorkoutType() {
        return workoutType;
    }
*/

    //private WorkoutType workoutType;
    public final static Workout Instance= new Workout();
    public int getWorkoutid() {
        return workoutid;
    }

    public void setWorkoutid(int workoutid) {
        this.workoutid = workoutid;
    }

    public int workoutid;
    //duration for individual exercise
    private int durationMinutes;
    //decided not to use sets and reps as MET value (see enum ExerciseType) can better calculate calories burned
    private int caloriesBurned;

    public String getWorkoutTypestr() {
        return workoutTypestr;
    }

    public void setWorkoutTypestr(String workoutTypestr) {
        this.workoutTypestr = workoutTypestr;
    }

    private String workoutTypestr;
    //constructor, no default constructor as type must be selected
   /* public Workout(WorkoutType workoutType){
        this.workoutType = workoutType;
        this.durationMinutes = 0;
        this.caloriesBurned = 0;
    }*/

  /*  public void setExerciseType(WorkoutType workoutType){
        this.workoutType = workoutType;
    }

    public String getExerciseType(){
        return this.workoutType.toString();
    }
*/
  public Workout(Integer workoutid,Integer caloriesBurned,Integer durationMinutes, String workoutTypestr) {
      this.workoutid = workoutid;
      this.caloriesBurned = caloriesBurned;
      this.durationMinutes = durationMinutes;
      this.workoutTypestr = workoutTypestr;
  }


    public void setDurationMinutes(int durationMinutes){
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes(){
        return this.durationMinutes;
    }

    //method to calculate the calories burned by the exercise- this needs to be connected to the user class somehow to get the weight
    /*private void calculateCaloriesBurned(int weight){
        //standard formula for calories burned using MET value
        this.caloriesBurned = (int) Math.round(this.durationMinutes*(this.workoutType.MET*3.5*weight)/200);
    }*/

    public int getCaloriesBurned(){
        return this.caloriesBurned;
    }


    //testing
    public static void main(String[] args) {
     /*   Workout exercise1 = new Workout(WorkoutType.YOGA);
        System.out.println(exercise1.getExerciseType());
        System.out.println(exercise1.getDurationMinutes());
        System.out.println(exercise1.getCaloriesBurned());
        exercise1.setDurationMinutes(30);
        exercise1.calculateCaloriesBurned(50);
        System.out.println(exercise1.getDurationMinutes());
        System.out.println(exercise1.getCaloriesBurned());*/

    }
}
