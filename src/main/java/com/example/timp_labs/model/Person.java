package com.example.timp_labs.model;

import com.example.timp_labs.Habitat;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.Random;


public abstract class Person implements IBehaviour {
    protected ImageView imageIV;
    protected int id;
    protected double destinationX, destinationY; // Пункт назначения картинки
    protected double shiftX, shiftY; // Сдвиг картинки за 1 раз
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
        return imageIV.getX();
    }
    public double getY() {
        return imageIV.getY();
    }
    public void moveTo(double x, double y) {
        imageIV.setX(x);
        imageIV.setY(y);
    }
}
