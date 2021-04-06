import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //Initialise variables
    Integer userid;
    String username;
    String password;
    String email;
    int age;
    String name;
    double weight;
    double height;
    double BMI;
    private ArrayList<Goal> goals = new ArrayList<>();
    private ArrayList<Group> groups = new ArrayList<>();
    //private ArrayList<DailyActivity> activityLog = new ArrayList<DailyActivity>();

    public User(String username,String password) {
        this.username=username;
        this.password=password;
    }

    public User(String username, String password, String email, double height, double weight) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.height = height;
        this.weight = weight;

        calculateBMI();
    }

    public ArrayList<Goal> getGoals() {
        return goals;
    }
    public void setGoals(ArrayList<Goal> goals) {
        this.goals = goals;
    }
    public ArrayList<Group> getGroups() {
        return groups;
    }
    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
    public double getBMI() {
        return BMI;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
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
        //TODO Connect to a database and delete a user.
    }

    //Calculate the BMI of the user.
    public void calculateBMI(){
        this.BMI= this.weight/ (height*height);
    }

    //Methods to serialize and de-serialize the user object for storage in the db
    static String toDatabaseString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    static Object fromDatabaseString( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", BMI=" + BMI +
                ", goals=" + goals +
                ", groups=" + groups +
                '}';
    }
    //public void addDailyActivity(DailyActivity day){
    //this.activityLog.add(day);
    //}

    public void addGoal(Goal goal){
        this.goals.add(goal);
    }

    public void addGroup(Group group){
        this.groups.add(group);
    }

    public void removeGoal(Goal goal){
        goals.remove(goal);
    }

    public static void addTestData(User user){
        user.addGoal(new Goal("Goal 1", LocalDate.now(), LocalDate.now().plusDays(2), "N/A"));
    }

//    public static void main(String[] args) {
//        //Testing for the user class:
//        User testUser = new User("Daniella", "Ammo", 70, 1.73);
//        testUser.setHeight(1.76);
//        testUser.calculateBMI();
//        System.out.println(testUser.BMI);
//        System.out.println(testUser);
//    }

}