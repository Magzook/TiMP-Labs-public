package com.example.timp_labs;

import javafx.application.Platform;
import java.io.FileNotFoundException;
import java.util.*;

public class Habitat {
    private static Habitat instance;
    public Controller mainController;
    public static Timer timer;
    public boolean timeFlag, startFlag;
    private boolean statisticFlag;
    private long startTime;
    private int seconds = 0, minutes = 0;
    private static int width = 1000, height = 600;
    private ArrayList<Person> array = new ArrayList<Person>();
    private float p1, p2;
    private int n1, n2;

    public Habitat(Controller mainController) {
        this.mainController = mainController;
    }
    public static void setInstance(Habitat instance) {
        Habitat.instance = instance;
    }
    public void setParamPhysicalPerson(float p, int n){
        n1 = n;
        p1 = p;
    }
    public void setParamJuridicalPerson(float p, int n){
        n2 = n;
        p2 = p;
    }
    public static int getWidth() {
        return width;
    }
    public static int getHeight() {
        return height;
    }
    public boolean getStatisticFlag() {return statisticFlag; }
    public long getStartTime() {return startTime; }
    public int getMinutes() {return minutes;}
    public int getSeconds() {return seconds;}
    public void startAction() {
        startFlag = timeFlag = true;
        statisticFlag = false;
        seconds = -1;
        minutes = 0;
        timer = new Timer();
        Controller.showStatisticLabel();
        startTime = System.currentTimeMillis();
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                seconds++;
                if (seconds == 60){
                    minutes++;
                    seconds = 0;
                }
                Platform.runLater(() -> {
                    Controller.updateTimer();
                    update((System.currentTimeMillis() - startTime)/1000);
                });
            }
        }, 0, 1000);
    }
    public void stopAction() {
        startFlag = timeFlag = false;
        statisticFlag = true;
        Controller.showStatisticLabel();
        timer.cancel();
        timer = new Timer();
        startTime = System.currentTimeMillis();

        array.forEach((tmp) -> mainController.getPane().getChildren().remove(tmp.getImageView()));
        array.clear();
        PhysicalPerson.count = 0;
        JuridicalPerson.count = 0;
    }
    public void update(long time){
        Random rand = new Random();
        float p = rand.nextFloat();
        try {
            if ((time % n1 == 0) && (p <= p1)) {
                PhysicalPerson phy = new PhysicalPerson(rand.nextInt(0, width) - 60, rand.nextInt(0, height) - 40);
                mainController.getPane().getChildren().add(phy.getImageView());
                array.add(phy);
                PhysicalPerson.count++;
            }
            if ((time % n2 == 0) && (p <= p2)) {
                JuridicalPerson jur = new JuridicalPerson(rand.nextInt(0, width) - 60, rand.nextInt(0, height) - 40);
                mainController.getPane().getChildren().add(jur.getImageView());
                array.add(jur);
                JuridicalPerson.count++;
            }
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
    }
    public static Habitat getInstance() {
        return instance;
    }
}