package com.example.timp_labs.model;

import com.example.timp_labs.Habitat;
import javafx.scene.image.ImageView;

import java.util.Random;


public abstract class Person implements IBehaviour {
    protected ImageView imageIV;
    protected int id;
    public Person() {
        Habitat hab = Habitat.getInstance();
        Random rand = new Random();
        do {
            id = rand.nextInt(); // Генерация случайного id
        } while (hab.getIdCollection().contains(id)); // Проверка на уникальность
    }
    public abstract ImageView getImageView();
    public int getId() {
        return id;
    }
}
