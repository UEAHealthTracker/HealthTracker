import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

public class  DailyActivity implements Serializable {

    private final LocalDate date;
    private final ArrayList<Meal> meals = new ArrayList<>();
    private final ArrayList<Workout> workouts = new ArrayList<>();

    public DailyActivity(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public void addMeal(Meal newMeal){
        meals.add(newMeal);
    }

    public void removeMeal(Meal meal){meals.remove(meal);}

    public void addWorkout(Workout newWorkout){
        workouts.add(newWorkout);
    }

    public void removeWorkout(Workout workout){workouts.remove(workout);}

    public ArrayList<Meal> getMeals(){
        return this.meals;
    }

    public ArrayList<Workout> getWorkout(){
        return this.workouts;
    }

    private void saveData(){

    }
}
