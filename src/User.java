import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

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
    private ArrayList<DailyActivity> dailyActivities = new ArrayList<>();

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

    public ArrayList<DailyActivity> getDailyActivities() {
        return dailyActivities;
    }

    public void setDailyActivities(ArrayList<DailyActivity> dailyActivities) {
        this.dailyActivities = dailyActivities;
    }

    public ArrayList<Goal> getGoals() {
        return goals;
    }
    public ArrayList<Workout> getWorkouts(){
        ArrayList<Workout> workouts = new ArrayList<>();

        for(int i = 0; i < this.getDailyActivities().size(); i++){
            System.out.println(this.dailyActivities.get(i).toString());
            workouts.addAll(this.getDailyActivities().get(i).getWorkout());
        }

        return workouts;
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
    public void addDailyActivity(DailyActivity day){
        this.dailyActivities.add(day);
    }

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
        user.addGroup(new Group("Group1", user));
    }

    public void addWorkout(Workout workout){
        LocalDate date = LocalDate.now();

        System.out.println(workout);

        System.out.println(dailyActivities.isEmpty());

        if(!dailyActivities.isEmpty()){
            for(int i = 0; i < dailyActivities.size(); i++){
                System.out.println(dailyActivities.get(i).toString());
                System.out.println(dailyActivities.get(i).getDate());
                System.out.println(date);
                if(dailyActivities.get(i).getDate().equals(date)){
                    System.out.println(workout);
                    dailyActivities.get(i).addWorkout(workout);
                    System.out.println(dailyActivities.toString());
                }
                else{
                    System.out.println("Here2");
                }
            }
        }
        else {
            System.out.println("Here");
            dailyActivities.add(new DailyActivity(date, workout));
        }


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