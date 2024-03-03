package com.example.timp_labs;

import java.util.*;
import javafx.application.Platform;

public class Statistics {
    public static Timer timer;
    public static boolean timeFlag, startFlag;
    private static boolean statisticFlag;
    private static long startTime;
    private static int seconds = 0, minutes = 0;
    public static void showStatisticLabel() {
        Habitat hab = Habitat.getInstance();
        if (statisticFlag) {
            String statistic = "Физические лица: " + PhysicalPerson.count+ "\nЮридические лица: " + JuridicalPerson.count;
            statistic += "\nВремя: " + (System.currentTimeMillis() - startTime)/1000 + " сек";
            hab.mainController.getStatistic().setText(statistic);
            hab.mainController.getStatistic().setVisible(true);
            hab.mainController.getLabelTimer().setVisible(false);
            hab.mainController.getLabelTextTIMER().setVisible(false);
        }
        else {
            hab.mainController.getStatistic().setVisible(false);
            hab.mainController.getStatistic().setText("");
            hab.mainController.getLabelTimer().setVisible(true);
            hab.mainController.getLabelTextTIMER().setVisible(true);
        }
    }
    public static void showTimer() {
        Habitat hab = Habitat.getInstance();
        timeFlag = !timeFlag;
        if (timeFlag) {
            hab.mainController.getLabelTextTIMER().setVisible(true);
            hab.mainController.getLabelTimer().setVisible(true);
        }
        else {
            hab.mainController.getLabelTextTIMER().setVisible(false);
            hab.mainController.getLabelTimer().setVisible(false);
        }
    }
    public static void updateTimer() {
        Habitat hab = Habitat.getInstance();
        String min = minutes + "";
        String sec = seconds + "";
        if (min.length() < 2)
            min = "0"+ min;
        if (sec.length() < 2)
            sec = ("0" + sec);
        String time = min + ":" + sec;
        hab.mainController.getLabelTimer().setText(time);
    }
    public static void startAction() {
        Habitat hab = Habitat.getInstance();
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
        }, 0, 1000);
    }
    public static void stopAction() {
        Habitat hab = Habitat.getInstance();
        startFlag = timeFlag = false;
        statisticFlag = true;
        showStatisticLabel();
        timer.cancel();
        timer = new Timer();
        startTime = System.currentTimeMillis();

        hab.getArray().forEach((tmp) -> hab.mainController.getPane().getChildren().remove(tmp.getImageView()));
        hab.getArray().clear();
        PhysicalPerson.count = 0;
        JuridicalPerson.count = 0;
    }
}