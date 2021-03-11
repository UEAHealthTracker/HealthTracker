public class Drink extends Diet {

    private final DrinkType drinkType;

    public enum DrinkType{
        JUICE,
        SMOOTHIE,
        WATER,
        SODA
    }

    public Drink(DrinkType drinkType, String name, int calories) {
        super();
        this.drinkType = drinkType;
        this.setName(name);
        this.setCalorieAmount(calories);
    }

    //Testing Harness
    public static void main(String[] args) {
        Drink drink = new Drink(DrinkType.JUICE, "Orange", 89);
        String name = drink.getName();
        System.out.println("Name of test drink: " + name);
        System.out.println("Type of test drink: " + drink.drinkType);
        System.out.println("Name of test drink: " + drink.getCalorieAmount());

    }
}





