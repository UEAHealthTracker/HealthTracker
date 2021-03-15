import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class DailyActivity implements Serializable {

    private LocalDate date;
    private ArrayList<Meal> meals = new ArrayList<Meal>();
    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();

    public DailyActivity(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public void addMeal(Meal newMeal){
        meals.add(newMeal);
    }

    public void addExercise(Exercise newExercise){
        exercise.add(newExercise);
    }

    public ArrayList<Meal> getMeals(){
        return this.meals;
    }

    public ArrayList<Exercise> getExercise(){
        return this.exercise;
    }

    private void saveData(){

    }
}
