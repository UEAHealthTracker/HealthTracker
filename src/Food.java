public class Food extends Diet {

    private final FoodType foodType;

    public Food(String name){
        super();
        this.foodType = FoodType.REGULAR;
        this.setName(name);
    }

    public Food(FoodType foodType, String name, int calories) {
        super();
        this.foodType = foodType;
        this.setName(name);
        this.setCalorieAmount(calories);
    }

    enum FoodType {
        VEGAN,
        PESCATERIAN,
        VEGATARIAN,
        GLUTENFREE,
        REGULAR
    }

    //Testing Harness
    public static void main(String[] args) {
        Food food = new Food(FoodType.VEGAN, "Banana", 89);
        String name = food.getName();
        System.out.println("Name of test food: " + name);
        System.out.println("Type of test food: " + food.foodType);
        System.out.println("Name of test food: " + food.getCalorieAmount());

    }
}



