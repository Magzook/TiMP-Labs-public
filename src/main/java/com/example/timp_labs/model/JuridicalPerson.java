package com.example.timp_labs.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class JuridicalPerson extends Person {
    public static int spawnedCount = 0;
    static Image image;
    private static int lifeTime;

    static {
        try {
            image = new Image(new FileInputStream("src/main/resources/juridical_person.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public JuridicalPerson(double x, double y) throws FileNotFoundException {
        super();
        imageView = new ImageView(image);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(true);
    }
    public ImageView getImageView() {return imageView;}
    public static void setLifeTime(int lifeTime) {
        JuridicalPerson.lifeTime = lifeTime;
    }
    public static int getLifeTime() {
        return lifeTime;
    }
}