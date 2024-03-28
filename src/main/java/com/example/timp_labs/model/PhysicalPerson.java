package com.example.timp_labs.model;

import com.example.timp_labs.model.Person;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PhysicalPerson extends Person {
    public static int count = 0;
    static Image image;
    private static int lifeTime;

    static {
        try {
            image = new Image(new FileInputStream("src/main/resources/physical_person.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public PhysicalPerson(int x, int y) throws FileNotFoundException {
        super();
        imageIV = new ImageView(image);
        imageIV.setX(x);
        imageIV.setY(y);
        imageIV.setFitWidth(80);
        imageIV.setFitHeight(80);
        imageIV.setPreserveRatio(true);
    }
    public ImageView getImageView() {return imageIV;}
    public static void setLifeTime(int lifeTime) {
        PhysicalPerson.lifeTime = lifeTime;
    }
    public static int getLifeTime() {
        return lifeTime;
    }
}
