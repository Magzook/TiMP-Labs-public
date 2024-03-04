package com.example.timp_labs;

import java.util.*;
import javafx.application.Platform;

public class Statistics {
    public Timer timer;
    public boolean timeFlag, startFlag;
    private boolean statisticFlag;
    private long startTime;
    private int seconds = 0, minutes = 0;
    public Controller mainController;
    private static Statistics instance;
    public Statistics(Controller mainController) {
        this.mainController = mainController;
    }
    public static void setInstance(Statistics instance) {
        Statistics.instance = instance;
    }
    public static Statistics getInstance() {
        return instance;
    }
    public void showStatisticLabel() {

        if (statisticFlag) {
            String statistic = "Физические лица: " + PhysicalPerson.count+ "\nЮридические лица: " + JuridicalPerson.count;
            statistic += "\nВремя: " + (System.currentTimeMillis() - startTime)/1000 + " сек";
            mainController.getStatistic().setText(statistic);
            mainController.getStatistic().setVisible(true);
            mainController.getLabelTimer().setVisible(false);
            mainController.getLabelTextTIMER().setVisible(false);
        }
        else {
            mainController.getStatistic().setVisible(false);
            mainController.getStatistic().setText("");
            mainController.getLabelTimer().setVisible(true);
            mainController.getLabelTextTIMER().setVisible(true);
        }
    }
    public void showTimer() {

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
    public void updateTimer() {

        String min = minutes + "";
        String sec = seconds + "";
        if (min.length() < 2)
            min = "0"+ min;
        if (sec.length() < 2)
            sec = ("0" + sec);
        String time = min + ":" + sec;
        mainController.getLabelTimer().setText(time);
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
            public void run() {
                seconds++;
                if (seconds == 60){
                    minutes++;
                    seconds = 0;
                }
                Platform.runLater(() -> {
                    updateTimer();
                    Habitat.getInstance().update((System.currentTimeMillis() - startTime)/1000);
                });
            }
        }, 4, 1000);
    }
    public void stopAction() {
        Habitat hab = Habitat.getInstance();
        startFlag = timeFlag = false;
        statisticFlag = true;
        showStatisticLabel();
        timer.cancel();
        timer = new Timer();
        startTime = System.currentTimeMillis();
        hab.getArray().forEach((tmp) -> mainController.getPane().getChildren().remove(tmp.getImageView()));
        hab.getArray().clear();
        PhysicalPerson.count = 0;
        JuridicalPerson.count = 0;
    }
}