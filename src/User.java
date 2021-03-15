

public class User {
    //Initialise variables
    private String firstName;
    private String secondName;
    private float weight;
    private float height;
    private float BMI;
    private ArrayList<Goal> goals = new ArrayList<Goal>();
    private ArrayList<Group> groups = new ArrayList<Group>();
    private ArrayList<Day> days = new ArrayList<Day>();

    public float getBMI() {
        return BMI;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    //Initialise the methods
    public static void deleteUser(User user){
        //Connect to a database and delete a user.
    }
    //Calculate the BMI of the user.
    public void calculateBMI(){
        this.BMI= this.weight/ (height*height);
    }

    public User(String firstName, String secondName, float weight, float height) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.weight = weight;
        this.height = height;
        this.calculateBMI();
    }

    public void addDay(Day day){
        this.days.add(day);
    }

    public void addGoal(Goal goal){
        this.goals.add(goal);
    }

    public void addGroup(Group group){
        this.groups.add(group);
    }

}
