public class DietItem {

    private String name;
    private int calorieCount;
    private Type type;

    public enum Type{
        FOOD,
        DRINK,
        MEAL
    }

    public DietItem(String name, int calorieCount, Type type){
        this.name = name;
        this.calorieCount = calorieCount;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(int calorieCount) {
        this.calorieCount = calorieCount;
    }

    public Type getType(){
        return type;
    }

    public void setType(String type){
        if(type.equalsIgnoreCase("FOOD")){
            this.type = Type.FOOD;
        }
        else if(type.equalsIgnoreCase("DRINK")){
            this.type = Type.DRINK;
        }
        else if(type.equalsIgnoreCase("MEAL")){
            this.type = Type.DRINK;
        }
    }

}
