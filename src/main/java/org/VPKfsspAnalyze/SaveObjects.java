package org.VPKfsspAnalyze;

import java.io.*;
import java.util.ArrayList;

public class SaveObjects {

    public static void setFILENAMEBASE(String FILENAMEBASE) {
        SaveObjects.FILENAMEBASE = FILENAMEBASE;
    }

    public static String getFilename() {
        return FILENAMEARRAY;
    }

    public static String getFILENAMEARRAY() {
        return FILENAMEARRAY;
    }

    public static void setFILENAMEARRAY(String FILENAMEARRAY) {
        SaveObjects.FILENAMEARRAY = FILENAMEARRAY;
    }

    private static String FILENAMEARRAY = "C:\\Users\\user\\IdeaProjects\\OutputExcel\\saveArray.xxx";
    private static String FILENAMEBASE = "C:\\Users\\user\\IdeaProjects\\OutputExcel\\saveBase.xxx";

    public void saveArray (ArrayList <String> list){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAMEARRAY));
            oos.writeObject(list);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<String> loadArray() {
        ArrayList<String> arrayOfNames = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAMEARRAY));
            arrayOfNames = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (IOException e) {
            throw new RuntimeException(e + " Загрузка не сработала");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return arrayOfNames;
    }

    public void saveFullBase(String[][] fullBase) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAMEBASE));
            oos.writeObject(fullBase);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String[][] loadFullBase() {
        String[][] fullBase;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAMEBASE));
            fullBase = (String[][]) ois.readObject();
            ois.close();
        } catch (IOException e) {
            throw new RuntimeException(e + " Загрузка не сработала");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return fullBase;
    }
}
