package race.models;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Ring {

    private ObservableList<Node> rectangles;
    private final Image map = new Image("images/map.png");
    private boolean busy;

    public Ring(ObservableList<Node> rectangles){
        this.rectangles = rectangles;
        busy=false;
    }

    public void addCar(Rectangle r){
        rectangles.add(r);
    }

    public synchronized void removeCar(Rectangle r){
        rectangles.remove(r);
    }

    public synchronized boolean isBusy() {
        return busy;
    }

    public synchronized void setBusy() {
        while (this.busy){
            try{
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        busy=true;
        notify();
    }

    public synchronized void setNotBusy(){
        while (!this.busy){
            try {
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        busy=false;
        notify();
    }
}
