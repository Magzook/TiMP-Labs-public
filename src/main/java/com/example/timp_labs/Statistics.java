package com.example.timp_labs;

import java.util.*;
import com.example.timp_labs.model.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

public class Statistics {
    public Timer timer;
    public boolean timeFlag = true, startFlag, restartFlag = false, firstActionFlag = true;
    private int seconds = -1, minutes = 0;
    public Controller mainController;
    private static Statistics instance;
    private Statistics() {}
    public static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }
    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }
    public int getTime() {
        return minutes * 60 + seconds;
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
        Statistics st = Statistics.getInstance();
        AIPhysical th1 = AIPhysical.getInstance();
        AIJuridical th2 = AIJuridical.getInstance();
        // Разбудить потоки, если они спят
        if (!th1.isActive) {
            th1.isActive = true;
            st.mainController.btnPhyIntellect.setText("ON");
            String monitor = th1.monitor;
            synchronized (monitor) {
                monitor.notify();
            }
        }
        if (!th2.isActive) {
            th2.isActive = true;
            st.mainController.btnJurIntellect.setText("ON");
            String monitor = th2.monitor;
            synchronized (monitor) {
                monitor.notify();
            }
        }

        if (restartFlag || firstActionFlag) {
            hab.getObjCollection().forEach((tmp) -> mainController.getPane().getChildren().remove(tmp.getImageView())); // Очистка изображений
            hab.getObjCollection().clear();  // Очистка всех коллекций
            hab.getBornCollection().clear();
            hab.getIdCollection().clear();
            PhysicalPerson.spawnedCount = 0;
            JuridicalPerson.spawnedCount = 0;
            seconds = -1;
            minutes = 0;
            timer = new Timer();
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
                                   if (seconds >= 1 || minutes >= 1) {
                                       Habitat.getInstance().update();
                                   }
                               });
                           }
                       },
                4,
                1000); //1000
    }
    public void stopAction() {
        startFlag = false;
        timer.cancel();
        timer = new Timer();
        AIPhysical.getInstance().isActive = false;
        AIJuridical.getInstance().isActive = false;

        if (mainController.btnShowInfo.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Статистика");
            alert.setHeaderText("OK - прекратить симуляцию\nCancel - продолжить симуляцию");
            String statistic = "Создано:\nФизические лица: " + PhysicalPerson.spawnedCount+ "\nЮридические лица: " + JuridicalPerson.spawnedCount;
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
                mainController.fieldN1.setDisable(false);
                mainController.fieldN2.setDisable(false);
                mainController.fieldLifeTimePhy.setDisable(false);
                mainController.fieldLifeTimeJur.setDisable(false);
                mainController.boxP1.setDisable(false);
                mainController.boxP2.setDisable(false);
                mainController.boxPhyPriority.setDisable(false);
                mainController.boxJurPriority.setDisable(false);
            }
            else {
                mainController.btnStart.setDisable(true);
                mainController.btnStop.setDisable(false);
                mainController.menuStart.setDisable(true);
                mainController.menuStop.setDisable(false);

                startAction();
            }
        }

    }
}