import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TaskCellFactory implements Callback<ListView<Goal>, ListCell<Goal>> {

    @Override
    public ListCell<Goal> call(ListView<Goal> param) {
        return new ListController();
    }
}