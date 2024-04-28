package com.example.timp_labs.controllers;

import com.example.timp_labs.model.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

public class FileMaster {
    private static final String defaultConfigFolder = System.getProperty("user.dir");
    private static String directory;
    private static FileChooser fileChooser;
    static {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(defaultConfigFolder));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
    }
    public static void setDefaultDirectory() {
        directory = defaultConfigFolder + "\\last_config.txt";
    }
    public static void callSaveDialogWindow() {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) directory = null;
        else directory = file.getPath();
    }
    public static void callLoadDialogWindow() {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) directory = null;
        else directory = file.getPath();
    }
    public static void loadFromFile() throws IOException, ClassNotFoundException {
        if (directory == null) return;
        FileInputStream fis = new FileInputStream(directory);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Habitat hab = Habitat.getInstance();
        Statistics st = Statistics.getInstance();
        Controller ct = st.mainController;
        // Остановить текущую симуляцию и потоки
        if (st.timer != null) st.timer.cancel();
        st.restartFlag = false;
        AIPhysical.getInstance().isActive = false;
        AIJuridical.getInstance().isActive = false;
        // Очистить изображения
        hab.getObjCollection().forEach((tmp) -> ct.getPane().getChildren().remove(tmp.getImageView()));
        // Очистить старые коллекции
        Vector<Person> objCollection = hab.getObjCollection();
        HashMap<Integer, Integer> bornCollection = hab.getBornCollection();
        TreeSet<Integer> idCollection = hab.getIdCollection();
        objCollection.clear();
        bornCollection.clear();
        idCollection.clear();
        // Чтение всех объектов, времён рождения, координат
        int size = ois.readInt();
        for (int i = 0; i < size; i++) {
            Person obj = (Person) ois.readObject();
            int bornTime = ois.readInt();
            double currentX = ois.readDouble();
            double currentY = ois.readDouble();
            objCollection.add(obj);
            bornCollection.put(obj.getId(), bornTime);
            idCollection.add(obj.getId());
            obj.createImageView(currentX, currentY);
            ct.getPane().getChildren().add(obj.getImageView());
        }
        PhysicalPerson.spawnedCount = ois.readInt();
        JuridicalPerson.spawnedCount = ois.readInt();
        // Чтение параметров симуляции
        int time = ois.readInt(); // Текущее время
        st.minutes = time / 60;
        st.seconds = time % 60;
        if (st.seconds != -1) st.updateTimer();
        boolean showInfo = ois.readBoolean(); // Показывать информацию?
        ct.btnShowInfo.setSelected(showInfo);
        ct.menuShowInfo.setSelected(showInfo);
        boolean showTime = ois.readBoolean(); // Показывать время?
        if (showTime) {
            ct.btnShowTime.setSelected(true);
            ct.menuShowTime.setSelected(true);
        }
        else {
            ct.btnHideTime.setSelected(true);
            ct.menuHideTime.setSelected(true);
            st.showTimer();
        }
        ct.fieldN1.setText(ois.readUTF()); // Периоды рождения
        ct.fieldN2.setText(ois.readUTF());
        ct.boxP1.setValue(ois.readUTF()); // Вероятности рождения
        ct.boxP2.setValue(ois.readUTF());
        ct.fieldLifeTimePhy.setText(ois.readUTF()); // Время жизни
        ct.fieldLifeTimeJur.setText(ois.readUTF());

        String mode = ois.readUTF(); // Интеллект объектов
        ct.btnPhyIntellect.setText(mode);
        //phyIntellect(mode);
        mode = ois.readUTF();
        ct.btnJurIntellect.setText(mode);
        //jurIntellect(mode);

        int priority = ois.readInt(); // Приоритет потоков
        ct.boxPhyPriority.setValue(priority);
        AIPhysical.getInstance().setPriority(priority);
        priority = ois.readInt();
        ct.boxJurPriority.setValue(priority);
        AIJuridical.getInstance().setPriority(priority);
        ois.close();
        fis.close();
    }

    public static void saveToFile() throws IOException {
        if (directory == null) return;
        FileOutputStream fos = new FileOutputStream(directory, false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Vector<Person> objCollection = Habitat.getInstance().getObjCollection();
        HashMap<Integer, Integer> bornCollection = Habitat.getInstance().getBornCollection();
        synchronized (objCollection) {
            oos.writeInt(objCollection.size()); // Количество объектов
            for (var obj : objCollection) {
                oos.writeObject(obj); // Объект
                oos.writeInt(bornCollection.get(obj.getId())); // Время рождения
                oos.writeDouble(obj.getX()); // Координата X
                oos.writeDouble(obj.getY()); // Координата Y
            }
            oos.writeInt(PhysicalPerson.spawnedCount); // Количество живых объектов одного типа
            oos.writeInt(JuridicalPerson.spawnedCount);
            // Далее надо записать параметры симуляции
            Statistics st = Statistics.getInstance();
            oos.writeInt(st.getTime()); // Текущее время
            Controller ct = st.mainController;
            oos.writeBoolean(ct.btnShowInfo.isSelected()); // Показывать информацию?
            oos.writeBoolean(ct.btnShowTime.isSelected()); // Показывать время?
            oos.writeUTF(ct.fieldN1.getText()); // Периоды рождения
            oos.writeUTF(ct.fieldN2.getText());
            oos.writeUTF(ct.boxP1.getValue()); // Вероятности рождения
            oos.writeUTF(ct.boxP2.getValue());
            oos.writeUTF(ct.fieldLifeTimePhy.getText()); // Время жизни
            oos.writeUTF(ct.fieldLifeTimeJur.getText());
            oos.writeUTF(ct.btnPhyIntellect.getText()); // Интеллект объектов
            oos.writeUTF(ct.btnJurIntellect.getText());
            oos.writeInt(ct.boxPhyPriority.getValue()); // Приоритет потоков
            oos.writeInt(ct.boxJurPriority.getValue());
        }
        oos.close();
        fos.close();
    }
}
