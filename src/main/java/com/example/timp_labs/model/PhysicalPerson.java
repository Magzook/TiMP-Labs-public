package com.example.timp_labs.model;

import com.example.timp_labs.model.Person;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PhysicalPerson extends Person {
    public static int spawnedCount = 0;
    static Image image;
    private static int lifeTime;

    static {
        try {
            image = new Image(new FileInputStream("src/main/resources/physical_person.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public PhysicalPerson(double x, double y) throws FileNotFoundException {
        super();
        imageIV = new ImageView(image);
        imageIV.setX(x);
        imageIV.setY(y);
        imageIV.setFitWidth(80); // Делаем картинку квадратной
        imageIV.setFitHeight(80); // Делаем картинку квадратной
        imageIV.setPreserveRatio(false); // Исходное фото НЕ квадратное, поэтому начальное соотношение сторон не сохраняем
    }
    public ImageView getImageView() {return imageIV;}
    public static void setLifeTime(int lifeTime) {
        PhysicalPerson.lifeTime = lifeTime;
    }
    public static int getLifeTime() {
        return lifeTime;
    }
}
