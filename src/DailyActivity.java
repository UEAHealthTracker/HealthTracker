import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class  DailyActivity implements Serializable {

    private Date date;

    @Override
    public String toString() {
        return "DailyActivity{" +
                "date=" + date +
                ", meals=" + meals +
                ", workout=" + workouts +
                '}';
    }

    private ArrayList<Meal> meals = new ArrayList<>();
    private ArrayList<Workout> workouts = new ArrayList<>();

    public DailyActivity(Date date, Workout workout) {
        this.date = date;
        addWorkout(workout);
    }

    public Date getDate(){
        return this.date;
    }

    public void addMeal(Meal newMeal){
        meals.add(newMeal);
    }

    public void addWorkout(Workout newWorkout){
        workouts.add(newWorkout);
    }

    public ArrayList<Meal> getMeals(){
        return this.meals;
    }

    public ArrayList<Workout> getWorkout(){
        return this.workouts;
    }

}
