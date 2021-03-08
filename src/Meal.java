import java.util.ArrayList;
import java.util.Date;

public class Meal {

    private ArrayList<Food> foodList;
    private ArrayList<Drink> drinkList;
    private Date timeConsumed;
    private int calorieCount;

    public Meal() {
        Date date = new Date();
        this.timeConsumed = date;
        this.calorieCount = 0;
    }

     void addFood(Food food)
     {
        this.foodList.add(food);

     }

     void addDrink(Drink drink){
        this.drinkList.add(drink);
     }
     void updateCalories(int calorieCount)
     {
         this.calorieCount += calorieCount;

     }




}
