
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

    //Initialise the methods
    public static void deleteUser(User user){
        //Connect to a database and delete a user.
    }
    //Calculate the BMI of the user.
    public float calculateBMI(){
        float BMI = this.weight/ (height*height);
         this.BMI= BMI;
         return BMI;
    }

    public User(String firstName, String secondName, float weight, float height) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.weight = weight;
        this.height = height;
        this.calculateBMI();
    }


}
