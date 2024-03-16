package com.example.timp_labs.model;

import javafx.scene.image.ImageView;


public abstract class Person implements IBehaviour {
    protected ImageView imageIV;

    public Person() {

    }
    public abstract ImageView getImageView();
}