package com.example.timp_labs.model;

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
        imageView = new ImageView(image);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(80); // Делаем картинку квадратной
        imageView.setFitHeight(80); // Делаем картинку квадратной
        imageView.setPreserveRatio(false); // Исходное фото НЕ квадратное, поэтому начальное соотношение сторон не сохраняем
    }
    public ImageView getImageView() {return imageView;}
    public static void setLifeTime(int lifeTime) {
        PhysicalPerson.lifeTime = lifeTime;
    }
    public static int getLifeTime() {
        return lifeTime;
    }
}
