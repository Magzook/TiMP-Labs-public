package com.example.timp_labs.model;

import com.example.timp_labs.Habitat;
import javafx.scene.shape.Rectangle;

import java.util.Random;
import java.util.Vector;
import static java.lang.Math.*;

public abstract class BaseAI extends Thread {
    protected final double SHIFT_DIAGONAL = 5; // На сколько пикселей смещается картинка за один сдвиг
    protected String objectType;
    protected double leftBoundX, rightBoundX, upperBoundY, lowerBoundY;
    public boolean isActive = true;
    public String monitor;
    public BaseAI(String name) {
        super(name);
    }
    public void run() {
        Vector<Person> objects = Habitat.getInstance().getObjCollection();
        Random rand = new Random();

        while(true) {
            synchronized (monitor) {
                if (!isActive) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                synchronized (objects) {
                    for (int i = 0; i < objects.size() && isActive; i++) {
                        Person obj = objects.get(i);
                        // Каждый поток работает только со своим типом объекта
                        if ((obj.getClass().getSimpleName()).equals(objectType)) {
                            double currentX = obj.getX();
                            double currentY = obj.getY();
                            if (obj.hasToTravel == null) { // Первая проверка объекта
                                // Проверить, появилась ли картинка в "своей" части области симуляции
                                if (!(currentX >= leftBoundX && currentX <= rightBoundX && currentY >= lowerBoundY && currentY <= upperBoundY)) {
                                    obj.hasToTravel = true;
                                    obj.destinationX = rand.nextDouble(leftBoundX, rightBoundX);
                                    obj.destinationY = rand.nextDouble(lowerBoundY, upperBoundY);

                                    double distanceX = abs(currentX - obj.destinationX);
                                    double distanceY = abs(currentY - obj.destinationY);
                                    double angle = atan(distanceY / distanceX);
                                    obj.shiftX = SHIFT_DIAGONAL * cos(angle);
                                    obj.shiftY = SHIFT_DIAGONAL * sin(angle);
                                    if (currentX > obj.destinationX) obj.shiftX *= -1;
                                    if (currentY > obj.destinationY) obj.shiftY *= -1;
                                }
                                else {
                                    obj.hasToTravel = false;
                                }
                            }

                            if (obj.hasToTravel) {
                                if (sqrt(pow(currentX - obj.destinationX, 2) + pow(currentY - obj.destinationY, 2)) < SHIFT_DIAGONAL) {
                                    obj.hasToTravel = false;
                                    obj.moveTo(obj.destinationX, obj.destinationY);
                                }
                                else {
                                    obj.moveTo(currentX + obj.shiftX, currentY + obj.shiftY);
                                }
                            }

                        }
                    }
                }
            }

            try {
                Thread.sleep(20); // при 20 работает хорошо
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
