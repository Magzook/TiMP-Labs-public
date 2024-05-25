package client.model;

import client.controllers.Habitat;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import java.io.Serializable;
import java.util.Random;

public abstract class Person implements Serializable {
    protected transient ImageView imageView;
    protected int id;
    protected double destinationX, destinationY; // Пункт назначения картинки
    protected double shiftX, shiftY; // Сдвиг картинки за 1 раз
    protected int shiftsTotal; // Количество сдвигов
    protected int hasToTravel = -1; // Нужно ли вообще двигать картинку?
    public Person() {
        Habitat hab = Habitat.getInstance();
        Random rand = new Random();
        do {
            id = rand.nextInt(100000, 1000000); // Генерация случайного id
        } while (hab.getIdCollection().contains(id)); // Проверка на уникальность
    }
    public Person(int id, double destX, double destY, int hasToTravel) {
        this.id = id;
        this.destinationX = destX;
        this.destinationY = destY;
        this.hasToTravel = hasToTravel;
    }
    public abstract ImageView getImageView();
    public abstract void createImageView(double x, double y);
    public int getId() {
        return id;
    }
    public double getX() {
        return imageView.getX();
    }
    public double getY() {
        return imageView.getY();
    }
    public double getDestinationX() {return destinationX;}
    public double getDestinationY() {return destinationY;}
    public int getHasToTravel() {return hasToTravel;}
    public void moveTo(double x, double y) {
        Platform.runLater(() -> {
            imageView.setX(x);
            imageView.setY(y);
        });
    }

}
