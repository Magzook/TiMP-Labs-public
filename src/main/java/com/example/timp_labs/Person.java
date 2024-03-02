package com.example.timp_labs;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

abstract class Person implements IBehaviour{
    private String path;
    final ImageView imageIV;

    public String getPath() {return path;}

    public void setPath(String path) {
        this.path = path;
    }

    public Person(int _x, int _y, String _path) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(_path));
        imageIV = new ImageView(image);
        imageIV.setX(_x);
        imageIV.setY(_y);
        imageIV.setFitWidth(80);
        imageIV.setFitHeight(90);
        imageIV.setPreserveRatio(true);
    }

    public ImageView getImageView() {return imageIV;}
}
