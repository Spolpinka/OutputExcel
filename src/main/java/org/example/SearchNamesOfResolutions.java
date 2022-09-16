package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchNamesOfResolutions {
    public ArrayList<String> searching() {
        File f = new File("Z:\\Обменник\\Специалисты\\firelabs\\свод на 23052022\\namesTest");
        File[] pathNames = f.listFiles();
        ArrayList<String> fullNames = new ArrayList();
        System.out.println("количество файлов - " + pathNames.length);

        for (int i = 0; i < pathNames.length; ++i) {
            fullNames.add(pathNames[i].getPath());
        }

        ArrayList<String> namesOfResolutions = new ArrayList(Arrays.asList("Запрос в банк",
                "Постановление об обращении взыскания на денежные средства должника, находящиеся в банке или иной кредитной организации",
                "Постановление об обращении взыскания на денежные средства в иностранной валюте при исчислении долга в рублях",
                "Запрос на получения сведений о зарегистрированных автомототранспортных средствах",
                "Постановление о распределении денежных средств, поступающих во временное распоряжение подразделения судебных приставов",
                "Ответ на запрос информации о должнике или его имуществе"));

        int i;
        for (i = 0; i < fullNames.size(); ++i) {
            ArrayList<String> namesTemp = namesSearch(namesOfResolutions, fullNames.get(i));

            for (int k = 0; k < namesTemp.size(); ++k) {
                if (!namesOfResolutions.contains(namesTemp.get(k))) {
                    namesOfResolutions.add(namesTemp.get(k));
                }
            }
        }

        for (i = 0; i < namesOfResolutions.size(); ++i) {
            System.out.println((String) namesOfResolutions.get(i));
        }
        return fullNames;
    }

    public static ArrayList<String> namesSearch(ArrayList<String> namesOfResolutions, String filename) {
        String content = "";
        ArrayList<String> names = new ArrayList();

        for (int i = 0; i < namesOfResolutions.size(); ++i) {
            try {
                new File(filename);
                content = Files.lines(Paths.get(filename)).reduce("", String::concat);
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

        String s;
        for (String f = "<fssp:DocTypeName>"; content.contains(f); content = s) {
            s = content.substring(content.indexOf(f) + f.length());
            String resol = s.substring(0, s.indexOf("</fssp:DocTypeName>"));
            if (!names.contains(resol)) {
                names.add(resol);
            }
        }

        System.out.println("че за хня?");
        return names;
    }
}

