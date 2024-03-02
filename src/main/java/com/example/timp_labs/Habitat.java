package com.example.timp_labs;

import javafx.application.Platform;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Habitat {
    private static volatile Habitat instance;
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
    public void startAction() {
        startFlag = timeFlag = true;
        statisticFlag = false;
        seconds = -1;
        minutes = 0;
        timer = new Timer();
        showStatisticLabel();
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
                    updateTimer();
                    update((System.currentTimeMillis() - startTime)/1000);
                });
            }
        }, 0, 1000);
    }
    public void stopAction() {
        startFlag = timeFlag = false;
        statisticFlag = true;
        showStatisticLabel();
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
    public void showStatisticLabel(){
        if (statisticFlag) {
            String statistic = "Физические лица: " + PhysicalPerson.count+ "\nЮридические лица: " + JuridicalPerson.count;
            statistic += "\nВремя: " + (System.currentTimeMillis() - startTime)/1000 + " сек";
            mainController.getStatistic().setText(statistic);
            mainController.getStatistic().setVisible(true);
            mainController.getLabelTimer().setVisible(false);
            mainController.getLabelTextTIMER().setVisible(false);
        }
        else{
            mainController.getStatistic().setVisible(false);
            mainController.getStatistic().setText("");
            mainController.getLabelTimer().setVisible(true);
            mainController.getLabelTextTIMER().setVisible(true);
        }
    }
    public void updateTimer(){
        String min = minutes + "";
        String sec = seconds + "";
        if (min.length() < 2)
            min = "0"+ min;
        if (sec.length() < 2)
            sec = ("0" + sec);
        String time = min + ":" + sec;
        mainController.getLabelTimer().setText(time);
    }
    public void showTimer(){
        timeFlag = !timeFlag;
        if (timeFlag) {
            mainController.getLabelTextTIMER().setVisible(true);
            mainController.getLabelTimer().setVisible(true);
        }
        else {
            mainController.getLabelTextTIMER().setVisible(false);
            mainController.getLabelTimer().setVisible(false);
        }
    }
    public static Habitat getInstance() {
        Habitat localInstance = instance;
        if (localInstance == null) {
            synchronized (Habitat.class) {
                localInstance = instance;
            }
        }
        return localInstance;
    }
}