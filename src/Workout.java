import java.io.Serial;
import java.io.Serializable;

enum WorkoutType {


    //exercise types- MET value for each type based on information at https://golf.procon.org/met-values-for-800-activities/

    AEROBICS(6,"AEROBICS"),
    BASKETBALL(8,"BASKETBALL"),
    BOXING(12,"BOXING"),
    CRICKET(5,"CRICKET"),
    CIRCUIT_TRAINING(8, "CIRCUIT_TRAINING"),
    CYCLING(7,"CYCLING"),
    DANCING(5,"DANCING"),
    FOOTBALL(8,"FOOTBALL"),
    GYMNASTICS(6,"GYMNASTICS"),
    HIKING(7,"HIKING"),
    HOCKEY(8,"HOCKEY"),
    HORSE_RIDING(5,"HORSE_RIDING"),
    MOUNTAIN_BIKING(9,"MOUNTAIN_BIKING"),
    MARTIAL_ARTS(10,"MARTIAL_ARTS"),
    PILATES(3,"PILATES"),
    RUNNING(8,"RUNNING"),
    ROCK_CLIMBING(8,"ROCK_CLIMBING"),
    ROWING(6,"ROWING"),
    RUGBY(8,"RUGBY"),
    SKATING(7,"SKATING"),
    SKIING(7,"SKIING"),
    SKIPPING(12,"SKIPPING"),
    SWIMMING(7,"SWIMMING"),
    TENNIS(7,"TENNIS"),
    WALKING(3,"WALKING"),
    WEIGHT_LIFTING(5,"WEIGHT_LIFTING"),
    YOGA(3,"YOGA");

    //MET = metabolic equivalent for task- it is based on the intensity and used to calculate calories burnt
    public int MET;
    public String ID;

    //default constructor for enum
    WorkoutType(int MET,String ID){
        this.MET = MET;
        this.ID=ID;
    }
}

public class Workout implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private WorkoutType workoutType;

    //duration for individual exercise
    private int workoutDuration;

    //decided not to use sets and reps as MET value (see enum ExerciseType) can better calculate calories burned

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public WorkoutType getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(WorkoutType workoutType) {
        this.workoutType = workoutType;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "workoutType=" + workoutType +
                ", workoutDuration=" + workoutDuration +
                ", caloriesBurned=" + caloriesBurned +
                '}';
    }

    private int caloriesBurned;

    //constructor, no default constructor as type must be selected
    public Workout(WorkoutType workoutType, int workoutDuration, int caloriesBurned){
        this.workoutType = workoutType;
        this.workoutDuration = workoutDuration;
        this.caloriesBurned = caloriesBurned;
    }
    public void setExerciseType(WorkoutType workoutType){
        this.workoutType = workoutType;
    }

    public String getExerciseType(){
        return this.workoutType.toString();
    }

    public void setWorkoutDuration(int workoutDuration){
        this.workoutDuration = workoutDuration;
    }

    public int getWorkoutDuration(){
        return this.workoutDuration;
    }

    //method to calculate the calories burned by the exercise- this needs to be connected to the user class somehow to get the weight
    private void calculateCaloriesBurned(int weight){
        //standard formula for calories burned using MET value
        this.caloriesBurned = (int) Math.round(this.workoutDuration *(this.workoutType.MET*3.5*weight)/200);
    }

    public int getCaloriesBurned(){
        return this.caloriesBurned;
    }

    //testing
    public static void main(String[] args) {

        long millis=System.currentTimeMillis();
        java.util.Date date=new java.util.Date(millis);

        Workout exercise1 = new Workout(WorkoutType.YOGA, 2, 2);
        System.out.println(exercise1.getExerciseType());
        System.out.println(exercise1.getWorkoutDuration());
        System.out.println(exercise1.getCaloriesBurned());
        exercise1.setWorkoutDuration(30);
        exercise1.calculateCaloriesBurned(50);
        System.out.println(exercise1.getWorkoutDuration());
        System.out.println(exercise1.getCaloriesBurned());

    }
}
