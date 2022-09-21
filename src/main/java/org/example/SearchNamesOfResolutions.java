package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchNamesOfResolutions {
    public ArrayList<String> searching(String path) {
        File f = new File(path);
        File[] pathNames = f.listFiles();
        ArrayList<String> fullNames = new ArrayList();
        System.out.println("количество файлов - " + pathNames.length);

        for (int i = 0; i < pathNames.length; ++i) {
            fullNames.add(pathNames[i].getPath());
        }
        /*SaveObjects so = new SaveObjects();
        ArrayList<String> namesOfResolutions = so.loadArray();*/
        ListOfNames lon = new ListOfNames();


        int i;
        for (i = 0; i < fullNames.size(); ++i) {
            ArrayList<String> namesTemp = namesSearch(lon.getArrayOfNames(), fullNames.get(i));
            for (int k = 0; k < namesTemp.size(); ++k) {
                if (!lon.getArrayOfNames().contains(namesTemp.get(k))) {
                    lon.getArrayOfNames().add(namesTemp.get(k));
                    System.out.println("в файле " + fullNames.get(i));
                    System.out.println("обнаружено новое постановление: " + namesTemp.get(k));
                }
            }
        }

        /*for (i = 0; i < lon.getArrayOfNames().size(); ++i) {
            System.out.println(lon.getArrayOfNames().get(i));
        }*/
        return fullNames;
    }

    private ArrayList<String> namesSearch(ArrayList<String> namesOfResolutions, String filename) {
        String content = "";
        ArrayList<String> names = new ArrayList();

            try {
                new File(filename);
                content = Files.lines(Paths.get(filename)).reduce("", String::concat);
            } catch (IOException var7) {
                var7.printStackTrace();
            }

        String startName = "<fssp:DocName>";
        String stopName = "</fssp:DocName>";
        String startTypeName = "<fssp:DocTypeName>";
        String stopTypeName = "</fssp:DocTypeName>";

        while (content.contains(startName)) {
            String result = content.substring(content.indexOf(startName) + startName.length(),
                    content.indexOf(stopName));
            if (!names.contains(result)) {
                names.add(result);
            }
            content = content.substring(0, content.indexOf(startName))
                    + content.substring(content.indexOf(stopName) + stopName.length());
        }

        while (content.contains(startTypeName)) {
            String result = content.substring(content.indexOf(startTypeName) + startTypeName.length(),
                    content.indexOf(stopTypeName));
            if (!names.contains(result)) {
                names.add(result);
            }
            content = content.substring(0, content.indexOf(startTypeName))
                    + content.substring(content.indexOf(stopTypeName));
        }

        return names;
    }
}

