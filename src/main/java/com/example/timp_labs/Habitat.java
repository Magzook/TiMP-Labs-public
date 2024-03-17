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
    private ArrayList<Person> array = new ArrayList<>();
    private static Habitat instance;
    public static void setInstance(Habitat instance) {
        Habitat.instance = instance;
    }
    public static Habitat getInstance() {
        return instance;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public ArrayList<Person> getArray() {
        return array;
    }
    public void update() {
        Random rand = new Random();
        Statistics st = Statistics.getInstance();
        long time = st.getTime();
        float p = rand.nextFloat();
        try {
            // [0; 1000] при width = 1300
            // [0; 520] при height = 600
            if ((time % n1 == 0) && (p <= p1)) {
                PhysicalPerson phy = new PhysicalPerson(rand.nextInt(0, width - 300), rand.nextInt(0, height - 80));
                st.mainController.getPane().getChildren().add(phy.getImageView());
                array.add(phy);
                PhysicalPerson.count++;
            }
            if ((time % n2 == 0) && (p <= p2)) {
                JuridicalPerson jur = new JuridicalPerson(rand.nextInt(0, width - 300), rand.nextInt(0, height - 80));
                st.mainController.getPane().getChildren().add(jur.getImageView());
                array.add(jur);
                JuridicalPerson.count++;
            }
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
    }
}