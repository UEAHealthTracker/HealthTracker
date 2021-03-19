import java.util.ArrayList;

public class User {
    //Initialise variables
    private String firstName;
    private String secondName;
    private double weight;
    private double height;
    private double BMI;
   // private ArrayList<Goal> goals = new ArrayList<Goal>();
  //  private ArrayList<Group> groups = new ArrayList<Group>();
    private ArrayList<DailyActivity> activityLog = new ArrayList<DailyActivity>();

    public double getBMI() {
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
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

    public User(String firstName, String secondName, double weight, double height) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.weight = weight;
        this.height = height;
        this.calculateBMI();
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "firstName='" + firstName + '\'' +
//                ", secondName='" + secondName + '\'' +
//                ", weight=" + weight +
//                ", height=" + height +
//                ", BMI=" + BMI +
//                ", goals=" + goals +
//                ", groups=" + groups +
//                ", activityLog=" + activityLog +
//                '}';
//    }

    public void addDailyActivity(DailyActivity day){
        this.activityLog.add(day);
    }

//    public void addGoal(Goal goal){
//        this.goals.add(goal);
//    }
//
//    public void addGroup(Group group){
//        this.groups.add(group);
//    }

    public static void main(String[] args) {
        //Testing for the user class:
        User testUser = new User("Daniella", "Ammo", 70, 1.73);
        testUser.setHeight(1.76);
        testUser.calculateBMI();
        System.out.println(testUser.BMI);
        System.out.println(testUser);
    }

    }






