import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DietPageController extends BaseController implements Initializable {

    @FXML
    public TableColumn<Meal, String> mealTime;

    @FXML
    public TableColumn<Meal, String> mealFood;

    @FXML
    public TableColumn<Meal, String> mealDrink;

    @FXML
    public TableColumn<Meal, Integer> calorieCount;

    @FXML
    public ComboBox<String> mealTypeSelector;

    @FXML
    public TextField itemNameTextField;

    @FXML
    public TextField calorieCountTextField;
    public Button deleteButton;

    @FXML
    private TableView<Meal> dietTable;

    private boolean open=false;

    public void init(){
        if(!open) {
            mealTypeSelector.getItems().add("Food");
            mealTypeSelector.getItems().add("Drink");
            open=true;
        }

    }

    //add data to diet table
    public void populateDietTable(){

        System.out.println(user);
        //Get the attributes for the table from default getters and setters
        mealTime.setCellValueFactory(new PropertyValueFactory<>("mealTime"));
        mealFood.setCellValueFactory(new PropertyValueFactory<>("mealFood"));
        mealDrink.setCellValueFactory(new PropertyValueFactory<>("mealDrink"));
        calorieCount.setCellValueFactory(new PropertyValueFactory<>("calorieCount"));

        //Populate the table data
        dietTable.setItems(getMeals());

    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(ActionEvent actionEvent) throws IOException {
        Meal data = dietTable.getSelectionModel().getSelectedItem();
        if (data != null) {
            data = dietTable.getSelectionModel().getSelectedItem();

            for(int i = 0; i < user.getMeals().size(); i++){
                if(user.getMeals().get(i) == data){
                    user.removeMeal(user.getMeals().get(i));
                }
            }
        }

        loadPage(actionEvent, "DietPage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
                populateDietTable();
        });

    }

    private ObservableList<Meal> getMeals(){
        ObservableList<Meal> meals = FXCollections.observableArrayList();
        meals.addAll(user.getMeals());
        return meals;
    }

    public void addItem(ActionEvent actionEvent) throws IOException {
        //Food
        if(mealTypeSelector.getSelectionModel().getSelectedItem().equals("Food")){
            Food food = new Food(Food.FoodType.REGULAR, itemNameTextField.getText(), Integer.parseInt(calorieCountTextField.getText()));
            Meal meal = new Meal();
            meal.addFood(food);
            user.addMeal(meal);
        }
        //Drink
        else{
            Drink drink = new Drink(Drink.DrinkType.WATER, itemNameTextField.getText(), Integer.parseInt(calorieCountTextField.getText()));
            Meal meal = new Meal();
            meal.addDrink(drink);
            user.addMeal(meal);
            System.out.println(user);
        }

        loadPage(actionEvent, "DietPage.fxml");
    }
}
