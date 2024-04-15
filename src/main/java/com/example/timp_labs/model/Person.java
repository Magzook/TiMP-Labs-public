package com.example.timp_labs.model;

import com.example.timp_labs.Habitat;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import java.util.Random;

public abstract class Person implements IBehaviour {
    protected ImageView imageView;
    protected int id;
    protected double destinationX, destinationY; // Пункт назначения картинки
    protected double shiftX, shiftY; // Сдвиг картинки за 1 раз
    protected int shiftsTotal; // Количество сдвигов
    protected Boolean hasToTravel = null; // Нужно ли вообще двигать картинку?
    public Person() {
        Habitat hab = Habitat.getInstance();
        Random rand = new Random();
        do {
            id = rand.nextInt(100000, 1000000); // Генерация случайного id
        } while (hab.getIdCollection().contains(id)); // Проверка на уникальность
    }
    public abstract ImageView getImageView();
    public int getId() {
        return id;
    }
    public double getX() {
        return imageView.getX();
    }
    public double getY() {
        return imageView.getY();
    }
    public void moveTo(double x, double y) {
        Platform.runLater(() -> {
            imageView.setX(x);
            imageView.setY(y);
        });
    }
}
