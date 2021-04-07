import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class  DailyActivity implements Serializable {

    private LocalDate date;

    @Override
    public String toString() {
        return "DailyActivity{" +
                "date=" + date +
                ", meals=" + meals +
                ", workouts=" + workouts +
                '}';
    }

    private ArrayList<Meal> meals = new ArrayList<>();
    private ArrayList<Workout> workouts = new ArrayList<>();

    public DailyActivity(LocalDate date, Workout workout) {
        this.date = date;
        addWorkout(workout);
    }

    public LocalDate getDate(){
        return this.date;
    }

    public void addMeal(Meal newMeal){
        this.meals.add(newMeal);
    }

    public void addWorkout(Workout newWorkout){
        this.workouts.add(newWorkout);
    }

    public ArrayList<Meal> getMeals(){
        return this.meals;
    }

    public ArrayList<Workout> getWorkout(){
        return this.workouts;
    }

}
