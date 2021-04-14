public abstract class Diet {

    private String name;

    @Override
    public String toString() {
        return "Diet{" +
                "name='" + name + '\'' +
                ", calorieAmount=" + calorieAmount +
                ", quantity=" + quantity +
                '}';
    }

    private int calorieAmount;
    private int quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(int calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
