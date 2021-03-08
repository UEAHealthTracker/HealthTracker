public class Food extends Diet {

    private FoodType foodType;

    enum FoodType{

        VEGAN,
        PESCATERIAN,
        VEGATARIAN,
        GLUTENFREE,
        REGULAR


    }
    public static void main(String[] args) {
        Food food = new Food();
        food.getName();
    }
}



