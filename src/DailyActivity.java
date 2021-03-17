import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class  DailyActivity implements Serializable {

    private LocalDate date;
    private ArrayList<Meal> meals = new ArrayList<Meal>();
    private ArrayList<Workout> workout = new ArrayList<Workout>();

    public DailyActivity(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public void addMeal(Meal newMeal){
        meals.add(newMeal);
    }

    public void addWorkout(Workout newWorkout){
        workout.add(newWorkout);
    }

    public ArrayList<Meal> getMeals(){
        return this.meals;
    }

    public ArrayList<Workout> getWorkout(){
        return this.workout;
    }

    private void saveData(){

    }
}
