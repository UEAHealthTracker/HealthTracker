import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class DietPageController extends BaseController {

    private TableView dietTable;

    //add data to diet table
    public void populateDietTable(){

    }

    //allow user to select a table item/row and delete it using the delete button
    public void removeTableItem(){

    }

    public void openAddDietItemPage(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AddDietItemPage.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
