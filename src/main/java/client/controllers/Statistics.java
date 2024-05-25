package client.controllers;

import client.model.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.util.*;

public class Statistics {
    public Timer timer;
    public boolean timeFlag = true, startFlag, restartFlag = false;
    public int seconds = -1, minutes = 0;
    public Controller mainController;
    private static Statistics instance;
    private Statistics() {
        timer = new Timer();
    }
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
        // Разбудить потоки, если это необходимо
        phyIntellect();
        jurIntellect();

        if (restartFlag) {
            Habitat hab = Habitat.getInstance();
            hab.getObjCollection().forEach((tmp) -> mainController.getPane().getChildren().remove(tmp.getImageView())); // Очистка изображений
            hab.getObjCollection().clear();  // Очистка всех коллекций
            hab.getBornCollection().clear();
            hab.getIdCollection().clear();
            PhysicalPerson.spawnedCount = 0;
            JuridicalPerson.spawnedCount = 0;
            seconds = -1;
            minutes = 0;
            restartFlag = false;
        }
        timer = new Timer();
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
    public void phyIntellect() {
        if (mainController.btnPhyIntellect.getText().equals("ON")) {
            AIPhysical.getInstance().isActive = true;
            String monitor = AIPhysical.getInstance().monitor;
            synchronized (monitor) {
                monitor.notify();
            }
        }
        else {
            AIPhysical.getInstance().isActive = false;
        }
    }
    public void jurIntellect() {
        if (mainController.btnJurIntellect.getText().equals("ON")) {
            AIJuridical.getInstance().isActive = true;
            String monitor = AIJuridical.getInstance().monitor;
            synchronized (monitor) {
                monitor.notify();
            }
        }
        else {
            AIJuridical.getInstance().isActive = false;
        }
    }

    public void pauseAndClear() { // Остановка симуляции с возможностью продолжения со стиранием объектов и загрузкой новых
        Habitat hab = Habitat.getInstance();
        // Остановить текущую симуляцию и потоки
        if (timer != null) timer.cancel();
        restartFlag = false;
        AIPhysical.getInstance().isActive = false;
        AIJuridical.getInstance().isActive = false;
        // Включить кнопки
        mainController.enableButtons();
        // Очистить изображения
        hab.getObjCollection().forEach((tmp) -> mainController.getPane().getChildren().remove(tmp.getImageView()));
        // Очистить старые коллекции
        Vector<Person> objCollection = hab.getObjCollection();
        HashMap<Integer, Integer> bornCollection = hab.getBornCollection();
        TreeSet<Integer> idCollection = hab.getIdCollection();
        objCollection.clear();
        bornCollection.clear();
        idCollection.clear();
    }
    public void prepareTimer(int time) {
        minutes = time / 60;
        seconds = time % 60;
        if (seconds != -1) updateTimer();
    }
}