import java.util.ArrayList;
import java.time.LocalDateTime;

public class Meal {

    private final ArrayList<Food> foodList = new ArrayList<>();
    private final ArrayList<Drink> drinkList = new ArrayList<>();
    private final LocalDateTime timeConsumed;
    private int calorieCount;

    public Meal() {
        this.timeConsumed = null;
        this.calorieCount = 0;
    }

    public void addFood(Food food) {
        this.calorieCount += food.getCalorieAmount();
        this.foodList.add(food);
    }

    void addDrink(Drink drink) {
        this.calorieCount += drink.getCalorieAmount();
        this.drinkList.add(drink);
    }

    void updateCalories(int calorieCount) {
        this.calorieCount += calorieCount;
    }

    int getCalorieCount() {
        return calorieCount;
    }

    public ArrayList<Food> getFoodList() {
        return foodList;
    }

    public ArrayList<Drink> getDrinkList() {
        return drinkList;
    }

    public LocalDateTime getTimeConsumed() {
        return timeConsumed;
    }

    public String toString() {

        StringBuilder result = new StringBuilder();

        result.append("Meal consists of : \n");

        for (Drink drink : this.drinkList) {
            result.append("Drink : ").append(drink.getName()).append("\n");
        }
        for (Food food : this.foodList) {
            result.append("Food : ").append(food.getName()).append("\n");
        }

        result.append("Time consumed : ").append(timeConsumed).append("\n");
        result.append("Total calories : ").append(calorieCount).append("\n");

        return result.toString();
    }

    //Testing harness
    public static void main(String[] args) {

        //Create a new meal
        Meal meal = new Meal();

        //Add some foods and drinks
        meal.addDrink(new Drink(Drink.DrinkType.JUICE, "Orange", 45));
        meal.addFood(new Food(Food.FoodType.VEGAN, "Banana", 89));
        meal.addDrink(new Drink(Drink.DrinkType.JUICE, "Apple", 46));
        meal.addFood(new Food(Food.FoodType.VEGAN, "Vegan Cake", 257));

        //Print all food and drinks in the meal
        for (int i = 0; i < meal.getDrinkList().size(); i++) {
            System.out.println("Drink : " + meal.getDrinkList().get(i).getName());
        }
        for (int i = 0; i < meal.getFoodList().size(); i++) {
            System.out.println("Food : " + meal.getFoodList().get(i).getName());
        }

        //print calories and time consumed
        System.out.println("Total calorie count for meal : " + meal.getCalorieCount());
        System.out.println("Time consumed : " + meal.getTimeConsumed());

        System.out.println(meal);
    }
}
