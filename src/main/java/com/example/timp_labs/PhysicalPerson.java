package com.example.timp_labs;

import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;

public class PhysicalPerson extends Person implements IBehaviour {
    public static int count = 0;
    final ImageView imageIV;
    public PhysicalPerson(int x, int y) throws FileNotFoundException {
        super("src/main/resources/physical_person.png");
        imageIV = new ImageView(image);
        imageIV.setX(x);
        imageIV.setY(y);
        imageIV.setFitWidth(80);
        imageIV.setFitHeight(80);
        imageIV.setPreserveRatio(true);
    }
    public ImageView getImageView() {return imageIV;}
}
