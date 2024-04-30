package com.example.timp_labs.controllers;

import com.example.timp_labs.model.JuridicalPerson;
import com.example.timp_labs.model.Person;
import com.example.timp_labs.model.PhysicalPerson;

import java.io.FileNotFoundException;
import java.util.*;

public class Habitat {
    private int width = 1300, height = 600;
    public int n1, n2;
    public float p1, p2;

    private Vector<Person> objCollection; // Коллекция для объектов
    private HashMap<Integer, Integer> bornCollection; // Коллекция для пар <ID, Время рождения>
    private TreeSet<Integer> idCollection; // Коллекция для уникальных идентификаторов

    private static Habitat instance;
    private Habitat() {
        objCollection = new Vector<>();
        bornCollection = new HashMap<>();
        idCollection = new TreeSet<>();
    }
    public static Habitat getInstance() {
        if (instance == null) {
            instance = new Habitat();
        }
        return instance;
    }
    public Vector<Person> getObjCollection() {
        return objCollection;
    }
    public HashMap<Integer, Integer> getBornCollection() {return bornCollection;}
    public TreeSet<Integer> getIdCollection() {return idCollection;}
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public void update() {
        Random rand = new Random();
        Statistics st = Statistics.getInstance();
        int time = st.getTime();
        float p = rand.nextFloat();
        try {
            // Удалить объекты, время жизни которых истекло (проход по коллекции объектов)
            synchronized (objCollection) {
                for (int i = 0; i < objCollection.size(); i++) {
                    Person obj = objCollection.get(i);
                    int id = obj.getId();
                    int lifeTime = 0;
                    if (obj instanceof PhysicalPerson) lifeTime = PhysicalPerson.getLifeTime();
                    else if (obj instanceof JuridicalPerson) lifeTime = JuridicalPerson.getLifeTime();

                    if (bornCollection.get(id) + lifeTime <= st.getTime()) {
                        st.mainController.getPane().getChildren().remove(obj.getImageView()); // Удаление изображения
                        objCollection.remove(obj); // Удаление объекта из основной коллекции
                        i--;
                        bornCollection.remove(id); // Удаление пары <ID, Время рождения>
                        idCollection.remove(id); // Удаление идентификатора
                    }
                }
            }

            // [0; 1020] при width = 1300, размере картинки 80 на 80 и modalPane.getWidth() == 900
            // [0; 520] при height = 600, размере картинки 80 на 80 и modalPane.getHeight() == 750
            // Создание объектов

            if ((time % n1 == 0) && (p <= p1)) {
                PhysicalPerson phy = new PhysicalPerson(rand.nextDouble(0, 1020), rand.nextDouble(0, 520));
                st.mainController.getPane().getChildren().add(phy.getImageView());
                objCollection.add(phy);
                bornCollection.put(phy.getId(), st.getTime());
                idCollection.add(phy.getId());
                PhysicalPerson.spawnedCount++;
            }
            if ((time % n2 == 0) && (p <= p2)) {
                JuridicalPerson jur = new JuridicalPerson(rand.nextDouble(0, 1020), rand.nextDouble(0, 520));
                st.mainController.getPane().getChildren().add(jur.getImageView());
                objCollection.add(jur);
                bornCollection.put(jur.getId(), st.getTime());
                idCollection.add(jur.getId());
                JuridicalPerson.spawnedCount++;
            }
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
    }
}