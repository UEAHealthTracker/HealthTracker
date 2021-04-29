import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Meal {

    private int mealid;
    private ArrayList<DietItem> items;
    private LocalTime timeConsumed;
    private int calorieCount;

    public Meal() {
        this.mealid = -1;
        this.items = null;
        this.timeConsumed = LocalTime.now();
        this.calorieCount = 0;
    }

    public Meal(int mealid, ArrayList<DietItem> items, LocalTime timeConsumed, int calorieCount){
        this.mealid = mealid;
        this.items = items;
        this.timeConsumed = timeConsumed;
        this.calorieCount = calorieCount;
    }

    public void setMealid(int mealid){
        this.mealid = mealid;
    }

    public int getMealid(){
        return mealid;
    }

    public void addDietItem(DietItem dietItem) {
        this.calorieCount += dietItem.getCalorieCount();
        this.items.add(dietItem);
    }

    public void removeDietItem(DietItem dietItem) {
        if (items.contains(dietItem)){
            this.calorieCount -= dietItem.getCalorieCount();
            this.items.remove(dietItem);
        }
    }

    public ArrayList<DietItem> getItems(){return items;}

    public String getItemsToString(){
        StringBuilder itemsToString = new StringBuilder();

        for(DietItem item : items){
            itemsToString.append(item.getName());
        }
        return itemsToString.toString();
    }

    public ArrayList<DietItem> getFoods(){
        ArrayList<DietItem> foods = new ArrayList<>();

        for(DietItem item : items){
            if(item.getType() == DietItem.Type.FOOD){
                foods.add(item);
            }
        }
        return foods;
    }

    public ArrayList<DietItem>getDrinks(){
        ArrayList<DietItem> drinks = new ArrayList<>();

        for(DietItem item : items){
            if(item.getType() == DietItem.Type.DRINK){
                drinks.add(item);
            }
        }
        return drinks;
    }

    public int getCalorieCount() {
        return calorieCount;
    }

    public LocalTime getTimeConsumed() {
        return timeConsumed;
    }

    public String toString() {

        StringBuilder result = new StringBuilder();

        result.append("Meal consists of : \n");

        for(DietItem item : items){
            if(item.getType() == DietItem.Type.FOOD){
                result.append("Food : ").append(item.getName());
            }
            else if(item.getType() == DietItem.Type.DRINK){
                result.append("Drink : ").append(item.getName());
            }
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
        meal.addDietItem(new DietItem("Apple", 81, DietItem.Type.FOOD));
        meal.addDietItem(new DietItem("Banana", 105, DietItem.Type.FOOD));
        meal.addDietItem(new DietItem("Orange juice", 111, DietItem.Type.DRINK));
        meal.addDietItem(new DietItem("Cola classic", 140, DietItem.Type.DRINK));

        //Print all food and drinks in the meal
        for (int i = 0; i < meal.getItems().size(); i++) {
            if (meal.getItems().get(i).getType() == DietItem.Type.FOOD){
                System.out.println("Food : " + meal.getItems().get(i).getName());
            }
            else if (meal.getItems().get(i).getType() == DietItem.Type.DRINK){
                System.out.println("Drink : " + meal.getItems().get(i).getName());
            }
        }

        //print calories and time consumed
        System.out.println("Total calorie count for meal : " + meal.getCalorieCount());
        System.out.println("Time consumed : " + meal.getTimeConsumed());

        System.out.println(meal);
    }
}
