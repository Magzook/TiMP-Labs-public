package com.example.timp_labs;

import javafx.scene.image.ImageView;


abstract class Person implements IBehaviour {
    protected ImageView imageIV;

    public Person() {

    }
    public abstract ImageView getImageView();
}
