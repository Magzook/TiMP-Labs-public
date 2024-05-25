package client.model;

import client.controllers.Habitat;
import java.util.Random;
import java.util.Vector;
import static java.lang.Math.*;

public abstract class BaseAI extends Thread {
    protected static final double SHIFT_DIAGONAL = 5; // На сколько пикселей смещается картинка за один сдвиг
    protected String objectType;
    protected double leftBoundX, rightBoundX, upperBoundY, lowerBoundY;
    public boolean isActive = false;
    public String monitor; // дополнительная заглушка исключительно для корректной работы notify()
    public BaseAI(String name) {
        super(name);
    }
    public static void calculateShifting(Person obj) {
        // Вычисление параметров сдвига объекта
        double currentX = obj.getX();
        double currentY = obj.getY();
        double distanceX = abs(currentX - obj.destinationX);
        double distanceY = abs(currentY - obj.destinationY);
        obj.shiftsTotal = (int)(sqrt(pow(distanceX, 2) + pow(distanceY, 2)) / SHIFT_DIAGONAL);
        double angle = atan(distanceY / distanceX);
        obj.shiftX = SHIFT_DIAGONAL * cos(angle);
        obj.shiftY = SHIFT_DIAGONAL * sin(angle);
        if (currentX > obj.destinationX) obj.shiftX *= -1;
        if (currentY > obj.destinationY) obj.shiftY *= -1;
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
                            if (obj.hasToTravel == -1) { // Первая проверка объекта
                                // Проверить, появилась ли картинка в "своей" части области симуляции
                                if (!(currentX >= leftBoundX && currentX <= rightBoundX && currentY >= lowerBoundY && currentY <= upperBoundY)) {
                                    obj.hasToTravel = 1;
                                    obj.destinationX = rand.nextDouble(leftBoundX, rightBoundX);
                                    obj.destinationY = rand.nextDouble(lowerBoundY, upperBoundY);
                                    calculateShifting(obj);
                                }
                                else {
                                    obj.hasToTravel = 0;
                                }
                            }

                            if (obj.hasToTravel == 1) {
                                if (obj.shiftsTotal == 0) {
                                    obj.hasToTravel = 0;
                                    obj.moveTo(obj.destinationX, obj.destinationY);

                                }
                                else {
                                    obj.moveTo(currentX + obj.shiftX, currentY + obj.shiftY);
                                    obj.shiftsTotal--;
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