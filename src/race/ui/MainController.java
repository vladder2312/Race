package race.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import race.Main;
import race.models.CarThread;
import race.models.Ring;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    private final MainModel model = new MainModel();

    @FXML
    private Pane pane;

    private Ring ring;

    @FXML
    public void initialize() {
        ring = new Ring(pane.getChildren());
        pane.setOnMouseClicked(mouseEvent -> {
            double x = mouseEvent.getX(), y = mouseEvent.getY();
            if (y>175 && y<225){
                if (x<150){
                    new CarThread(new CarThread.Car(0), ring).start();
                }
                if (x>450){
                    new CarThread(new CarThread.Car(2), ring).start();
                }
            } else if (x>275 && x<325){
                if (y<50){
                    new CarThread(new CarThread.Car(1), ring).start();
                }
                if (y>350){
                    new CarThread(new CarThread.Car(3), ring).start();
                }
            }
        });
    }
}
