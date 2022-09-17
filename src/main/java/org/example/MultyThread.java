package org.example;

import java.util.ArrayList;

public class MultyThread extends Thread{
    private final ArrayList<String> listOfFiles;
    private int numberOFPrevious;


    public MultyThread(ArrayList<String> listOfFiles, int numberOFPrevious) {
        this.listOfFiles = listOfFiles;
        this.numberOFPrevious = numberOFPrevious;
    }

    public void run() {
        CountAllDocs countAllDocs = new CountAllDocs();
        for (String listOfFile : listOfFiles) {
            //прогоняем файлы через счетчик
            ArrayList<String> counts = countAllDocs.countDocs(listOfFile);
            numberOFPrevious++;
            for (int j = 0; j < counts.size(); j++) {
                Analyse.fullBase[numberOFPrevious][j] = counts.get(j);

                //System.out.println(counts.get(j));
            }
        }
    }
}
