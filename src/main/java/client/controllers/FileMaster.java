package client.controllers;

import client.model.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

public class FileMaster {
    private static final String folder = System.getProperty("user.dir");
    private static FileChooser fileChooser;
    static {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(folder));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Сохранённые объекты", "*.dat"));
    }
    public static void loadConfig() throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(folder + "\\config.txt"));
        Controller ct = Statistics.getInstance().mainController;
        // Показывать информацию?
        ct.btnShowInfo.setSelected(Boolean.parseBoolean(reader.readLine()));
        // Показывать время?
        String showTime = reader.readLine();
        if (showTime.equals("true")) {
            ct.btnShowTime.setSelected(true);
        }
        else {
            ct.btnHideTime.setSelected(true);
        }
        // Периоды рождения
        ct.fieldN1.setText(reader.readLine());
        ct.fieldN2.setText(reader.readLine());
        // Вероятности рождения
        ct.boxP1.setValue(reader.readLine());
        ct.boxP2.setValue(reader.readLine());
        // Время жизни объектов
        ct.fieldLifeTimePhy.setText(reader.readLine());
        ct.fieldLifeTimeJur.setText(reader.readLine());
        // Интеллект объектов
        ct.btnPhyIntellect.setText(reader.readLine());
        ct.btnJurIntellect.setText(reader.readLine());
        // Приоритет потоков
        ct.boxPhyPriority.setValue(Integer.parseInt(reader.readLine()));
        ct.boxJurPriority.setValue(Integer.parseInt(reader.readLine()));
        reader.close();
    }
    public static void saveConfig() throws IOException {
        FileWriter fw = new FileWriter(folder + "\\config.txt");
        Controller ct = Statistics.getInstance().mainController;
        // Показывать информацию?
        fw.write((ct.btnShowInfo.isSelected()) + "\n");
        // Показывать время?
        fw.write((ct.btnShowTime.isSelected()) + "\n");
        // Периоды рождения
        fw.write(ct.fieldN1.getText() + "\n"); //String
        fw.write(ct.fieldN2.getText() + "\n");
        // Вероятности рождения
        fw.write(ct.boxP1.getValue() + "\n"); //String
        fw.write(ct.boxP2.getValue() + "\n");
        // Время жизни объектов
        fw.write(ct.fieldLifeTimePhy.getText() + "\n"); //String
        fw.write(ct.fieldLifeTimeJur.getText() + "\n");
        // Интеллект объектов
        fw.write(ct.btnPhyIntellect.getText() + "\n"); //String
        fw.write(ct.btnJurIntellect.getText() + "\n");
        // Приоритет потоков
        fw.write(ct.boxPhyPriority.getValue() + "\n"); //int
        fw.write(ct.boxJurPriority.getValue() + "\n");
        fw.close();
    }
    public static void loadObjects() throws IOException, ClassNotFoundException {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) return;
        FileInputStream fis = new FileInputStream(file.getPath());
        ObjectInputStream ois = new ObjectInputStream(fis);
        Habitat hab = Habitat.getInstance();
        Statistics st = Statistics.getInstance();
        Controller ct = st.mainController;
        // Остановить текущую симуляцию и потоки
        if (st.timer != null) st.timer.cancel();
        st.restartFlag = false;
        AIPhysical.getInstance().isActive = false;
        AIJuridical.getInstance().isActive = false;
        ct.btnStart.setDisable(false);
        ct.menuStart.setDisable(false);
        ct.btnStop.setDisable(true);
        ct.menuStop.setDisable(true);
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
        PhysicalPerson.spawnedCount = ois.readInt(); // Число созданных за все время объектов
        JuridicalPerson.spawnedCount = ois.readInt();
        int time = ois.readInt(); // Текущее время
        st.minutes = time / 60;
        st.seconds = time % 60;
        if (st.seconds != -1) st.updateTimer();

        ois.close();
        fis.close();
    }
    public static void saveObjects() throws IOException {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) return;
        FileOutputStream fos = new FileOutputStream(file.getPath());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Vector<Person> objCollection = Habitat.getInstance().getObjCollection();
        HashMap<Integer, Integer> bornCollection = Habitat.getInstance().getBornCollection();
        synchronized (objCollection) {
            int a = objCollection.size();
            oos.writeInt(a); // Количество объектов
            for (var obj : objCollection) {
                oos.writeObject(obj); // Объект
                oos.writeInt(bornCollection.get(obj.getId())); // Время рождения
                oos.writeDouble(obj.getX()); // Координата X
                oos.writeDouble(obj.getY()); // Координата Y
            }
        }
        oos.writeInt(PhysicalPerson.spawnedCount); // Количество живых объектов одного типа
        oos.writeInt(JuridicalPerson.spawnedCount);
        oos.writeInt(Statistics.getInstance().getTime()); // Текущее время
        oos.close();
        fos.close();
    }
}
