import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class  DailyActivity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    public DailyActivity(LocalDate date, Meal meal){
        this.date = date;
        addMeal(meal);
    }

    public LocalDate getDate(){
        return this.date;
    }

    public void addMeal(Meal newMeal){
        this.meals.add(newMeal);
    }

    public void removeMeal(Meal meal){
        meals.remove(meal);
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
