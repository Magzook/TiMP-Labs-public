package com.example.timp_labs;

import com.example.timp_labs.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.Map;
import java.util.Timer;
import java.util.Vector;

public class Controller {
    @FXML
    private Label labelTextTIMER;
    @FXML
    private Label labelTimer;
    @FXML
    private Pane modalPane;
    public Label getLabelTextTIMER() {
        return labelTextTIMER;
    }
    public Label getLabelTimer() {
        return labelTimer;
    }
    public Pane getPane() {
        return modalPane;
    }

    @FXML
    public Button btnStart, btnStop, btnPhyIntellect, btnJurIntellect;
    @FXML
    public CheckBox btnShowInfo;
    @FXML
    public RadioButton btnShowTime, btnHideTime;
    @FXML
    public TextField fieldN1, fieldN2, fieldLifeTimePhy, fieldLifeTimeJur;
    @FXML
    public ComboBox<String> boxP1, boxP2;
    @FXML
    public ComboBox<Integer> boxPhyPriority, boxJurPriority;
    @FXML
    public MenuItem menuStart, menuStop, menuExit;
    @FXML
    public CheckMenuItem menuShowInfo;
    @FXML
    public RadioMenuItem menuShowTime, menuHideTime;
    @FXML
    void initialize() {
        btnStop.setDisable(true); // Кнопка "Стоп" изначально заблокирована
        menuStop.setDisable(true);
        btnShowTime.setSelected(true); // Переключатель "Показать время" изначально выбран
        menuShowTime.setSelected(true);
        fieldN1.setText("1"); // Значения для текстовых полей по умолчанию
        fieldN2.setText("2");
        fieldLifeTimePhy.setText("8");
        fieldLifeTimeJur.setText("30");
        // Дальше идёт заполнение комбобоксов
        for (int value = 0; value <= 100; value += 10) {
            boxP1.getItems().add(value + "%");
            boxP2.getItems().add(value + "%");
        }
        for (int value = 1; value <= 10; value++) {
            boxPhyPriority.getItems().add(value);
            boxJurPriority.getItems().add(value);
        }
        boxP1.setValue("80%"); // Значения для комбобоксов по умолчанию
        boxP2.setValue("100%");
        boxPhyPriority.setValue(5);
        boxJurPriority.setValue(5);
    }
    @FXML
    private void clickStart() {
        Habitat hab = Habitat.getInstance();
        int n1 = 1, n2 = 1, lifeTimePhy = 1, lifeTimeJur = 1;
        try {
            n1 = Integer.parseInt(fieldN1.getText());
            n2 = Integer.parseInt(fieldN2.getText());
            lifeTimePhy = Integer.parseInt(fieldLifeTimePhy.getText());
            lifeTimeJur = Integer.parseInt(fieldLifeTimeJur.getText());
            if (n1 < 1 || n2 < 1 || lifeTimePhy < 1 || lifeTimeJur < 1) {
                throw new NumberFormatException("Слишком маленькое число");
            }
            // Если данные некорректны, работает блок catch, иначе идём дальше
            hab.n1 = n1; // Установление периодов рождения
            hab.n2 = n2;
            PhysicalPerson.setLifeTime(lifeTimePhy); // Установление времени жизнм
            JuridicalPerson.setLifeTime(lifeTimeJur);
            hab.p1 = Float.parseFloat(boxP1.getValue().replace("%", "")) / 100; // Установление вероятностей рождения
            hab.p2 = Float.parseFloat(boxP2.getValue().replace("%", "")) / 100;
            AIPhysical.getInstance().setPriority(boxPhyPriority.getValue());
            AIJuridical.getInstance().setPriority(boxJurPriority.getValue());
            fieldN1.setDisable(true);
            fieldN2.setDisable(true);
            fieldLifeTimePhy.setDisable(true);
            fieldLifeTimeJur.setDisable(true);
            boxP1.setDisable(true);
            boxP2.setDisable(true);
            boxPhyPriority.setDisable(true);
            boxJurPriority.setDisable(true);
            btnStart.setDisable(true);
            btnStop.setDisable(false);
            menuStart.setDisable(true);
            menuStop.setDisable(false);
            // Разбудить потоки, если они спят

            Statistics.getInstance().startAction();
        }
        catch (NumberFormatException ex) {
            if (!fieldN1.getText().matches("\\d+") || n1 < 1) fieldN1.setText("1");
            if (!fieldN2.getText().matches("\\d+") || n2 < 1) fieldN2.setText("2");
            if (!fieldLifeTimePhy.getText().matches("\\d+") || lifeTimePhy < 1) fieldLifeTimePhy.setText("8");
            if (!fieldLifeTimeJur.getText().matches("\\d+") || lifeTimeJur < 1) fieldLifeTimeJur.setText("30");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Некорректное значение текстового поля");
            alert.setContentText("Требуется целое положительное число, не превышающее 2^31-1");
            alert.showAndWait();
        }
    }
    @FXML
    private void clickStop() {
        Statistics st = Statistics.getInstance();
        btnStart.setDisable(false);
        btnStop.setDisable(true);
        menuStart.setDisable(false);
        menuStop.setDisable(true);
        btnPhyIntellect.setText("OFF");
        btnJurIntellect.setText("OFF");

        if (!btnShowInfo.isSelected()) {
            st.restartFlag = true;
            fieldN1.setDisable(false);
            fieldN2.setDisable(false);
            fieldLifeTimePhy.setDisable(false);
            fieldLifeTimeJur.setDisable(false);
            boxP1.setDisable(false);
            boxP2.setDisable(false);
            boxPhyPriority.setDisable(false);
            boxJurPriority.setDisable(false);
        }
        st.stopAction();
    }
    @FXML
    private void clickInfo() {
        menuShowInfo.setSelected(!menuShowInfo.isSelected());
    }
    @FXML
    private void menuClickInfo() {
        btnShowInfo.setSelected(!btnShowInfo.isSelected());
    }
    @FXML
    private void clickTimeSwitch() {
        Statistics st = Statistics.getInstance();
        if (btnShowTime.isSelected()) {
            st.timeFlag = false;
            menuShowTime.setSelected(true);
            menuHideTime.setSelected(false);
        }
        else if (btnHideTime.isSelected())
        {
            st.timeFlag = true;
            menuShowTime.setSelected(false);
            menuHideTime.setSelected(true);
        }
        st.showTimer();
    }
    @FXML
    private void menuClickTimeSwitch() {
        Statistics st = Statistics.getInstance();
        if (menuShowTime.isSelected()) {
            st.timeFlag = false;
            btnShowTime.setSelected(true);
            btnHideTime.setSelected(false);
        }
        else if (menuHideTime.isSelected())
        {
            st.timeFlag = true;
            btnShowTime.setSelected(false);
            btnHideTime.setSelected(true);
        }
        st.showTimer();
    }
    @FXML
    public void menuClickExit() {
        System.exit(0);
    }
    @FXML
    public void clickCurrentObjects() {
        Statistics st = Statistics.getInstance();
        Habitat hab = Habitat.getInstance();
        if (!st.firstActionFlag) {
            st.timer.cancel();
            st.timer = new Timer();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText("Живые объекты");
        String statistic = "";

        for (Map.Entry<Integer, Integer> entry : hab.getBornCollection().entrySet()) {
            int id = entry.getKey();
            int bornTime = entry.getValue();
            statistic += "ID = " + id + "\tВремя рождения = ";
            if (bornTime < 60) statistic += bornTime + " сек\n";
            else statistic += (bornTime / 60) + " мин " + (bornTime % 60) + " сек\n";
        }

        TextArea textArea = new TextArea(statistic);
        textArea.setPrefColumnCount(25);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
        if (st.startFlag) st.startAction();
    }
    @FXML
    public void clickPhyIntellect() {
        String monitor = AIPhysical.getInstance().monitor;
        if (btnPhyIntellect.getText().equals("ON")) {
            AIPhysical.getInstance().isActive = false;
            btnPhyIntellect.setText("OFF");
        }
        else {
            AIPhysical.getInstance().isActive = true;
            synchronized (monitor) {
                monitor.notify();
            }
            btnPhyIntellect.setText("ON");
        }
    }
    @FXML
    public void clickJurIntellect() {
        String monitor = AIJuridical.getInstance().monitor;
        if (btnJurIntellect.getText().equals("ON")) {
            AIJuridical.getInstance().isActive = false;
            btnJurIntellect.setText("OFF");
        }
        else {
            AIJuridical.getInstance().isActive = true;
            synchronized (monitor) {
                monitor.notify();
            }
            btnJurIntellect.setText("ON");
        }
    }
    @FXML
    void keyPressed(KeyEvent keyEvent) {
        keyEvent.consume();
        Statistics st = Statistics.getInstance();
        switch (keyEvent.getCode()) {
            case KeyCode.T:
                st.showTimer();
                if (btnShowTime.isSelected()) {
                    btnShowTime.setSelected(false);
                    menuShowTime.setSelected(false);
                    btnHideTime.setSelected(true);
                    menuHideTime.setSelected(true);
                }
                else if (btnHideTime.isSelected()) {
                    btnShowTime.setSelected(true);
                    menuShowTime.setSelected(true);
                    btnHideTime.setSelected(false);
                    menuHideTime.setSelected(false);
                }
                break;
            case KeyCode.B:
                if (!st.startFlag) {
                    clickStart();
                }
                break;
            case KeyCode.E:
                if (st.startFlag) {
                    clickStop();
                }
                break;
        }
    }


}