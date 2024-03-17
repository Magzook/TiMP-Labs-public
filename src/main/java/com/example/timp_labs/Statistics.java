package com.example.timp_labs;

import java.util.*;

import com.example.timp_labs.model.JuridicalPerson;
import com.example.timp_labs.model.PhysicalPerson;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

public class Statistics {
    public Timer timer;
    public boolean timeFlag = true, startFlag, restartFlag = false, firstActionFlag = true;
    private long startTime, prevTime;
    private int seconds = -1, minutes = 0;
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
        Habitat hab = Habitat.getInstance();

        if (restartFlag || firstActionFlag) {
            hab.getArray().forEach((tmp) -> mainController.getPane().getChildren().remove(tmp.getImageView()));
            hab.getArray().clear();
            PhysicalPerson.count = 0;
            JuridicalPerson.count = 0;
            seconds = -1;
            minutes = 0;
            timer = new Timer();
            startTime = System.currentTimeMillis();
            restartFlag = false;
            firstActionFlag = false;
        }

        startFlag = true;

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
                    if (seconds >= 1) {
                        Habitat.getInstance().update((System.currentTimeMillis() - startTime)/1000);
                    }
                });
            }
        }, 4, 100);
    }
    public void stopAction() {
        Habitat hab = Habitat.getInstance();
        startFlag = false;

        timer.cancel();
        timer = new Timer();

        if (mainController.btnShowInfo.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Статистика");
            alert.setHeaderText("OK - прекратить симуляцию\nCancel - продолжить симуляцию");
            String statistic = "Физические лица: " + PhysicalPerson.count+ "\nЮридические лица: " + JuridicalPerson.count;
            statistic += "\nВремя: ";
            if (minutes >= 1)
            {
                statistic += minutes + " мин ";
            }
            statistic += seconds + " сек";

            TextArea textArea = new TextArea(statistic);
            textArea.setPrefColumnCount(20);
            textArea.setPrefRowCount(5);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setContent(textArea);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                restartFlag = true;
            }
            else {
                mainController.btnStart.setDisable(true);
                mainController.btnStop.setDisable(false);
                startAction();
            }
        }

    }
}