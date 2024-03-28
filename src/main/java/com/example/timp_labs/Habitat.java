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

    private Vector<Person> objCollection; // Коллекция для объектов
    private HashMap<Person, Integer> bornCollection; // Коллекция для пар <Объект, Время рождения>
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
    public HashMap<Person, Integer> getBornCollection() {return bornCollection;}
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
            for (int i = 0; i < objCollection.size(); i++) {
                Person obj = objCollection.get(i);
                int lifeTime = 0;
                if (obj instanceof PhysicalPerson) lifeTime = PhysicalPerson.getLifeTime();
                else if (obj instanceof JuridicalPerson) lifeTime = JuridicalPerson.getLifeTime();

                if (bornCollection.get(obj) + lifeTime == st.getTime()) {
                    st.mainController.getPane().getChildren().remove(obj.getImageView()); // Удаление изображения
                    objCollection.remove(obj); // Удаление из основной коллекции
                    i--;
                    bornCollection.remove(obj); // Удаление пары <Объект, Время рождения>
                    idCollection.remove(obj.getId()); // Удаление идентификатора
                }
            }

            // [0; 1000] при width = 1300
            // [0; 520] при height = 600
            // Создание объектов
            if ((time % n1 == 0) && (p <= p1)) {
                PhysicalPerson phy = new PhysicalPerson(rand.nextInt(0, width - 300), rand.nextInt(0, height - 80));

                st.mainController.getPane().getChildren().add(phy.getImageView());
                objCollection.add(phy);
                bornCollection.put(phy, st.getTime());
                PhysicalPerson.spawnedCount++;
            }
            if ((time % n2 == 0) && (p <= p2)) {
                JuridicalPerson jur = new JuridicalPerson(rand.nextInt(0, width - 300), rand.nextInt(0, height - 80));

                st.mainController.getPane().getChildren().add(jur.getImageView());
                objCollection.add(jur);
                bornCollection.put(jur, st.getTime());
                JuridicalPerson.spawnedCount++;
            }
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
    }
}