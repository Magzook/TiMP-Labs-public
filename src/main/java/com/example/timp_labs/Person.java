package com.example.timp_labs;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

abstract class Person implements IBehaviour {
    Image image;
    public Person(String path) throws FileNotFoundException {
        image = new Image(new FileInputStream(path));
    }
    public abstract ImageView getImageView();
}
