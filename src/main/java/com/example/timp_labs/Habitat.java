package com.example.timp_labs;

import com.example.timp_labs.model.JuridicalPerson;
import com.example.timp_labs.model.Person;
import com.example.timp_labs.model.PhysicalPerson;

import java.io.FileNotFoundException;
import java.util.*;

public class Habitat {
    private int width = 1300, height = 600;
    public int n1, n2;
    public float p1, p2;

    private Vector<Person> vector; // Коллекция для объектов
    private HashMap<Person, Integer> hashMap; // Коллекция для времён рождения

    private static Habitat instance;
    private Habitat() {
        vector = new Vector<>();
        hashMap = new HashMap<>();
    }
    public static Habitat getInstance() {
        if (instance == null) {
            instance = new Habitat();
        }
        return instance;
    }
    public Vector<Person> getVector() {
        return vector;
    }
    public HashMap<Person, Integer> getHashMap() {return hashMap;}
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
            // Удалить объекты, время жизни которых истекло (проход по коллекции типа Vector)
            for (int i = 0; i < vector.size(); i++) {
                Person obj = vector.get(i);
                int lifeTime = 0;
                if (obj instanceof PhysicalPerson) lifeTime = PhysicalPerson.getLifeTime();
                else if (obj instanceof JuridicalPerson) lifeTime = JuridicalPerson.getLifeTime();

                if (hashMap.get(obj) + lifeTime == st.getTime()) {
                    st.mainController.getPane().getChildren().remove(obj.getImageView());
                    vector.remove(obj);
                    i--;
                    hashMap.remove(obj);
                }
            }

            // [0; 1000] при width = 1300
            // [0; 520] при height = 600
            if ((time % n1 == 0) && (p <= p1)) {
                PhysicalPerson phy = new PhysicalPerson(rand.nextInt(0, width - 300), rand.nextInt(0, height - 80));

                st.mainController.getPane().getChildren().add(phy.getImageView());
                vector.add(phy);
                hashMap.put(phy, st.getTime());
                PhysicalPerson.count++;
            }
            if ((time % n2 == 0) && (p <= p2)) {
                JuridicalPerson jur = new JuridicalPerson(rand.nextInt(0, width - 300), rand.nextInt(0, height - 80));

                st.mainController.getPane().getChildren().add(jur.getImageView());
                vector.add(jur);
                hashMap.put(jur, st.getTime());
                JuridicalPerson.count++;
            }
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
    }
}