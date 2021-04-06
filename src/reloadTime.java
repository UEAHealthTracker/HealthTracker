import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


/**
Timer classes to be able to print out error messages then load the page again.
 */

  /*
public class reloadTime {
    Timer timer;
    private Parent root;
    private Stage stage;
    private Scene scene;
    public reloadTime(int seconds){
        timer = new Timer();


    }



    class printMessage extends TimerTask{
        public void run(){
            System.out.println("Testing timer");
            timer.cancel();
        }

    }



    class reloadPage extends TimerTask {
        public void run(){
            try {
                root = FXMLLoader.load(getClass().getResource("CreateGroupPage.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    public static void main(String args[]){
    new reloadTime(5);
        System.out.println("Test message");



    }
}


 */