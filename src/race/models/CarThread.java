package race.models;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.Timer;
import java.util.TimerTask;

public class CarThread extends Thread {

    private final Car car;
    private final Ring ring;
    private final Timer timer = new Timer();
    private Rectangle rectangle;
    private int endIndex;
    private boolean turning;

    public static class Car {
        private double x, y;
        private final double speed;
        private final Color color;
        private final int side; // 0 - слева, 1 - сверху, 2 - справа, 3 - снизу

        public Car(int side) {
            this.side = side;
            switch (side) {
                case 0 -> {
                    x = 0;
                    y = 190;
                }
                case 1 -> {
                    x = 290;
                    y = -50;
                }
                case 2 -> {
                    x = 600;
                    y = 190;
                }
                case 3 -> {
                    x = 290;
                    y = 400;
                }
            }
            speed = (Math.random() + 0.1) / 3;
            color = new Color(Math.random(), Math.random(), Math.random(), 1);
        }
    }

    public CarThread(Car car, Ring ring) {
        this.ring = ring;
        this.car = car;
        switch (car.side) {
            case 0, 2 -> rectangle = new Rectangle(car.x, car.y, 50, 20);
            case 1, 3 -> rectangle = new Rectangle(car.x, car.y, 20, 50);
        }
        do {
            endIndex = (int) (Math.random() * 3 + 1);
        } while (endIndex == car.side);
        rectangle.setFill(car.color);
        rectangle.setStroke(Color.BLACK);
        turning = false;
        ring.addCar(rectangle);
    }

    @Override
    public void run() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ride();
            }
        }, 0, 1);
        while (!interrupted()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (car.x < -51 || car.x > 600 || car.y < -51 || car.y > 400) {
                this.interrupt();
                Platform.runLater(() -> {
                    ring.removeCar(rectangle);
                });
            }
        }
    }

    public void ride() {
        if (!turning) {
            switch (car.side) {
                case 0 -> {
                    if (car.x > 100) {
                        ring.setBusy();
                        turnLeft();
                        return;
                    }
                    car.x += car.speed;
                    Platform.runLater(() -> rectangle.setX(car.x));
                }
                case 1 -> {
                    if (car.y > 0) {
                        ring.setBusy();
                        turnLeft();
                        return;
                    }
                    car.y += car.speed;
                    Platform.runLater(() -> rectangle.setY(car.y));
                }
                case 2 -> {
                    if (car.x < 455) {
                        ring.setBusy();
                        turnLeft();
                        return;
                    }
                    car.x -= car.speed;
                    Platform.runLater(() -> rectangle.setX(car.x));
                }
                case 3 -> {
                    if (car.y < 350) {
                        ring.setBusy();
                        turnLeft();
                        return;
                    }
                    car.y -= car.speed;
                    Platform.runLater(() -> rectangle.setY(car.y));
                }
            }
        }
    }

    private void rideOnRing() {
        System.out.println("Car.side:" + car.side + " End:" + endIndex);
        Rotate rotate = new Rotate();
        rotate.setAngle(car.speed);
        rotate.setPivotX(300);
        rotate.setPivotY(200);
        rectangle.getTransforms().addAll(rotate);
        int i = 0;
        int sum = car.side;
        do {
            sum++;
            i += 1;
            sum %= 4;
        } while (sum != endIndex);
        while (rotate.getAngle() <= 90 * i) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rotate.setAngle(rotate.getAngle() + car.speed);
        }
        Platform.runLater(()->rectangle.getTransforms().remove(rotate));
        switch (endIndex) {
            case 0 -> {
                car.x = 100;
                car.y = 190;
                    rectangle.setWidth(50);
                    rectangle.setHeight(20);
            }
            case 1 -> {
                car.x = 290;
                car.y = 0;
                    rectangle.setWidth(20);
                    rectangle.setHeight(50);
            }
            case 2 -> {
                car.x = 455;
                car.y = 190;
                rectangle.setWidth(50);
                rectangle.setHeight(20);
            }
            case 3 -> {
                car.x = 290;
                car.y = 350;
                    rectangle.setWidth(20);
                    rectangle.setHeight(50);
            }
        }
            rectangle.setX(car.x);
            rectangle.setY(car.y);
        ring.setNotBusy();
        rideOut();
    }

    /**
     * Выезд машины из кольца
     */
    private void rideOut() {
        System.out.println("Ride out:" + endIndex);
        do {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switch (endIndex) {
                case 0 -> {
                    car.x -= car.speed;
                    Platform.runLater(() -> rectangle.setX(car.x));
                }
                case 1 -> {
                    car.y -= car.speed;
                    Platform.runLater(() -> rectangle.setY(car.x));
                }
                case 2 -> {
                    car.x += car.speed;
                    Platform.runLater(() -> rectangle.setX(car.x));
                }
                case 3 -> {
                    car.y += car.speed;
                    Platform.runLater(() -> rectangle.setY(car.y));
                }
            }
        } while (!(car.x < -51) && !(car.x > 600) && !(car.y < -51) && !(car.y > 400));
        interrupt();
    }

    /**
     * Поворот машины и установка координаты
     */
    private void turnLeft() {
        switch (car.side) {
            case 0 -> {
                car.x = 165;
                car.y = 185;
                rectangle.setWidth(20);
                rectangle.setHeight(50);
            }
            case 1 -> {
                car.x = 290;
                car.y = 65;
                rectangle.setWidth(50);
                rectangle.setHeight(20);
            }
            case 2 -> {
                car.x = 415;
                car.y = 185;
                rectangle.setWidth(20);
                rectangle.setHeight(50);
            }
            case 3 -> {
                car.x = 290;
                car.y = 315;
                rectangle.setWidth(50);
                rectangle.setHeight(20);
            }
        }
        rectangle.setX(car.x);
        rectangle.setY(car.y);
        turning = true;
        rideOnRing();
    }
}
