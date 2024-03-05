package com.example.timp_labs;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class JuridicalPerson extends Person {
    public static int count = 0;
    static Image image;

    static {
        try {
            image = new Image(new FileInputStream("src/main/resources/juridical_person.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public JuridicalPerson(int x, int y) throws FileNotFoundException {
        super();
        imageIV = new ImageView(image);
        imageIV.setX(x);
        imageIV.setY(y);
        imageIV.setFitWidth(80);
        imageIV.setFitHeight(80);
        imageIV.setPreserveRatio(true);
    }
    public ImageView getImageView() {return imageIV;}
}