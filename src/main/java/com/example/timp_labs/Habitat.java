package com.example.timp_labs;

import java.io.FileNotFoundException;
import java.util.*;

public class Habitat {
    public int width = 1000, height = 600;
    private ArrayList<Person> array = new ArrayList<Person>();
    private float p1, p2;
    private int n1, n2;
    private static Habitat instance;
    public static void setInstance(Habitat instance) {
        Habitat.instance = instance;
    }
    public static Habitat getInstance() {
        return instance;
    }
    public void setParamPhysicalPerson(float p, int n){
        n1 = n;
        p1 = p;
    }
    public void setParamJuridicalPerson(float p, int n){
        n2 = n;
        p2 = p;
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
    public void update(long time) {
        Random rand = new Random();
        Statistics st = Statistics.getInstance();
        float p = rand.nextFloat();
        try {
            if ((time % n1 == 0) && (p <= p1)) {
                PhysicalPerson phy = new PhysicalPerson(rand.nextInt(0, width) - 60, rand.nextInt(0, height) - 40);
                st.mainController.getPane().getChildren().add(phy.getImageView());
                array.add(phy);
                PhysicalPerson.count++;
            }
            if ((time % n2 == 0) && (p <= p2)) {
                JuridicalPerson jur = new JuridicalPerson(rand.nextInt(0, width) - 60, rand.nextInt(0, height) - 40);
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