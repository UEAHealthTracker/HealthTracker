import java.time.LocalDateTime;
import java.util.ArrayList;

public class Meal {

    private int mealid;
    private ArrayList<Food> foods = new ArrayList<>();
    private ArrayList<Drink> drinks = new ArrayList<>();
    private final LocalDateTime timeConsumed;
    private int calorieCount;

    public Meal() {
        this.timeConsumed = null;
        this.calorieCount = 0;
    }

    public Meal(int mealid, ArrayList<Food> foodList, ArrayList<Drink> drinkList, LocalDateTime timeConsumed, int calorieCount){
        this.mealid = mealid;
        this.foods = foodList;
        this.drinks = drinkList;
        this.timeConsumed = timeConsumed;
        this.calorieCount = calorieCount;
    }

    public void addFood(Food food) {
        this.calorieCount += food.getCalorieAmount();
        this.foods.add(food);
    }

    void addDrink(Drink drink) {
        this.calorieCount += drink.getCalorieAmount();
        this.drinks.add(drink);
    }

    void updateCalories(int calorieCount) {
        this.calorieCount += calorieCount;
    }

    public int getCalorieCount() {
        return calorieCount;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public ArrayList<Drink> getDrinks() {
        return drinks;
    }

    public LocalDateTime getTimeConsumed() {
        return timeConsumed;
    }

    public int getMealid(){
        return mealid;
    }

    public String toString() {

        StringBuilder result = new StringBuilder();

        result.append("Meal consists of : \n");

        for (Drink drink : this.drinks) {
            result.append("Drink : ").append(drink.getName()).append("\n");
        }
        for (Food food : this.foods) {
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
        for (int i = 0; i < meal.getDrinks().size(); i++) {
            System.out.println("Drink : " + meal.getDrinks().get(i).getName());
        }
        for (int i = 0; i < meal.getFoods().size(); i++) {
            System.out.println("Food : " + meal.getFoods().get(i).getName());
        }

        //print calories and time consumed
        System.out.println("Total calorie count for meal : " + meal.getCalorieCount());
        System.out.println("Time consumed : " + meal.getTimeConsumed());

        System.out.println(meal);
    }
}
