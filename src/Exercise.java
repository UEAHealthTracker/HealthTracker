public class Exercise {

    private ExerciseType exerciseType;

    //duration for individual exercise
    private int durationMinutes;

    //decided not to use sets and reps as MET value (see enum ExerciseType) can better calculate calories burned

    private int caloriesBurned;

    //constructor, no default constructor as type must be selected
    public Exercise(ExerciseType exerciseType){
        this.exerciseType = exerciseType;
        this.durationMinutes = 0;
        this.caloriesBurned = 0;
    }

    public void setExerciseType(ExerciseType exerciseType){
        this.exerciseType = exerciseType;
    }

    public String getExerciseType(){
        return this.exerciseType.toString();
    }

    public void setDurationMinutes(int durationMinutes){
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes(){
        return this.durationMinutes;
    }

    //method to calculate the calories burned by the exercise- this needs to be connected to the user class somehow to get the weight
    private void calculateCaloriesBurned(int weight){
        //standard formula for calories burned using MET value
        this.caloriesBurned = (int) Math.round(this.durationMinutes*(this.exerciseType.MET*3.5*weight)/200);
    }

    public int getCaloriesBurned(){
        return this.caloriesBurned;
    }

    //testing
    public static void main(String[] args) {
        Exercise exercise1 = new Exercise(ExerciseType.YOGA);
        System.out.println(exercise1.getExerciseType());
        System.out.println(exercise1.getDurationMinutes());
        System.out.println(exercise1.getCaloriesBurned());
        exercise1.setDurationMinutes(30);
        exercise1.calculateCaloriesBurned(50);
        System.out.println(exercise1.getDurationMinutes());
        System.out.println(exercise1.getCaloriesBurned());

    }
}
