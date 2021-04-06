import java.util.ArrayList;

public class User {
    public final static User INSTANCE = new User();

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    //Initialise variables
     int userid;
     String username;
     String password;
     String email;
     int age;
     String RealName;
     double weight;
     double height;
     double BMI;

//    public ArrayList<Group> getGroups() {
//        return groups;
//    }
//
//    // private ArrayList<Goal> goals = new ArrayList<Goal>();
//    private ArrayList<Group> groups = new ArrayList<Group>();
//    private ArrayList<DailyActivity> activityLog = new ArrayList<DailyActivity>();

    public double getBMI() {
        return BMI;
    }

    public String getRealName() {
        return RealName;
    }
    public void setRealName(String name) {
        this.RealName = RealName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
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

    public User(String Realname,String username,String password,String email, int age, double weight, double height) {
        this.RealName = Realname;
        this.username=username;
        this.password=password;
        this.email=email;
        this.age=age;
        this.weight = weight;
        this.height = height;
        this.calculateBMI();
    }
    public User() {

    }

    public void addGroup(Group newGroup){
        groups.add(newGroup);

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

//    public void addDailyActivity(DailyActivity day){
//        this.activityLog.add(day);
//    }

//    public void addGoal(Goal goal){
//        this.goals.add(goal);
//    }
//
//    public void addGroup(Group group){
//        this.groups.add(group);
//    }

//    public static void main(String[] args) {
//        //Testing for the user class:
//        User testUser = new User("Daniella", "Ammo", 70, 1.73);
//        testUser.setHeight(1.76);
//        testUser.calculateBMI();
//        System.out.println(testUser.BMI);
//        System.out.println(testUser);
//    }

    }






