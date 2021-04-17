import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //Initialise variables
    String username;
    String password;
    String email;
    int age;
    double weight;
    double height;
    double BMI;
    private ArrayList<Goal> goals = new ArrayList<>();
    private ArrayList<Integer> groupsIds = new ArrayList<>();
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
    public ArrayList<Meal> getMeals(){
        ArrayList<Meal> meals = new ArrayList<>();
        for(int i = 0; i < this.dailyActivities.size(); i++){
            meals.addAll(this.getDailyActivities().get(i).getMeals());
        }
        return meals;
    }
    public void setGoals(ArrayList<Goal> goals) {
        this.goals = goals;
    }
    public ArrayList<Integer> getGroupsIds() {
        return groupsIds;
    }
    public void setGroupsIds(ArrayList<Integer> groupsIds) {
        this.groupsIds = groupsIds;
    }
    public double getBMI() {
        return BMI;
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
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", BMI=" + BMI +
                ", goals=" + goals +
                ", groups=" + groupsIds +
                ", dailyActivities=" + dailyActivities +
                '}';
    }

    public void addDailyActivity(DailyActivity day){
        this.dailyActivities.add(day);
    }

    public void addGoal(Goal goal){
        this.goals.add(goal);
    }

    public void addGroup(Integer groupReference){
        this.groupsIds.add(groupReference);
    }

    public void removeGoal(Goal goal){
        goals.remove(goal);
    }

    public static void addTestData(User user){
        user.addGoal(new Goal("Goal 1", LocalDate.now(), LocalDate.now().plusDays(2), "N/A"));
        //user.addGroup(new Group("Group1", user, "password"));
    }

    public void addWorkout(Workout workout){
        LocalDate date = LocalDate.now();

        System.out.println(workout);

        System.out.println(dailyActivities.isEmpty());

        if(!dailyActivities.isEmpty()){
            for(int i = 0; i < dailyActivities.size(); i++){
                if(dailyActivities.get(i).getDate().equals(date)){
                    dailyActivities.get(i).addWorkout(workout);
                }
            }
        }
        else {
            dailyActivities.add(new DailyActivity(date, workout));
        }


    }

    public void addMeal(Meal meal){
        LocalDate date = LocalDate.now();

        if(!dailyActivities.isEmpty()){
            for(int i = 0; i < dailyActivities.size(); i++){
                if(dailyActivities.get(i).getDate().equals(date)){
                    dailyActivities.get(i).addMeal(meal);
                }
            }
        }
        else {
            dailyActivities.add(new DailyActivity(date, meal));
        }


    }

    public ArrayList<Group> getGroups() throws SQLException {

        ArrayList<Group> groups = new ArrayList<>();

        for(int i = 0; i < this.groupsIds.size(); i++){
            Group group= getGroupFromId(this.groupsIds.get(i));
            groups.add(group);
        }

        return groups;
    }

    public Group getGroupFromId(Integer groupId) throws SQLException {

        String SQL_QUERY = "SELECT groupObject FROM groups WHERE groupid = ?";

        Group groupObject = null;
        String groupString = null;

        try {

            //Create a new db connection
            Connection connection = DatabaseConnect.connect();
            PreparedStatement pst = connection.prepareStatement(SQL_QUERY);

            pst.setString(1, groupId.toString());

            ResultSet rs = pst.executeQuery();

            //Loop through db results
            while (rs.next()) {

                //Set username and password variables
                groupString = rs.getString("groupObject");

                //If the user exists, retrieve their object from the db
                groupObject = (Group) Group.fromDatabaseString(groupString);

            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        return groupObject;
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