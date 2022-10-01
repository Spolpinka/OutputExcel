package org.VPKfsspAnalyze;

import java.io.File;
import java.util.ArrayList;

class SearchNamesOfResolutions {
    public ArrayList<String> searching(String path) {
        File f = new File(path);
        File[] pathNames = f.listFiles();
        ArrayList<String> fullNames = new ArrayList<>();
        assert pathNames != null: "количество файлов для поиска - 0";
        System.out.println("количество файлов - " + pathNames.length);

        for (File pathName : pathNames) {
            fullNames.add(pathName.getPath());
        }
        /*SaveObjects so = new SaveObjects();
        ArrayList<String> namesOfResolutions = so.loadArray();*/
        ListOfNames lon = new ListOfNames();


        for (String fullName : fullNames) {
            ArrayList<String> namesTemp = namesSearch(fullName);
            for (int k = 0; k < namesTemp.size(); ++k) {
                if (!lon.getArrayOfNames().contains(namesTemp.get(k))) {
                    lon.getArrayOfNames().add(namesTemp.get(k));
                    System.out.println("в файле " + fullName);
                    System.out.println("обнаружено новое постановление: " + namesTemp.get(k));
                }
            }
        }

        /*for (i = 0; i < lon.getArrayOfNames().size(); ++i) {
            System.out.println(lon.getArrayOfNames().get(i));
        }*/
        return fullNames;
    }

    private ArrayList<String> namesSearch(String filename) {
        String content = "";
        ArrayList<String> names = new ArrayList<String>();

        CreateContent cc = new CreateContent();
        content = cc.getString(filename, content);

        String startName = "<fssp:DocName>";
        String stopName = "</fssp:DocName>";
        String startTypeName = "<fssp:DocTypeName>";
        String stopTypeName = "</fssp:DocTypeName>";

        if (content.indexOf(stopName) < content.indexOf(startName)) {
            content = content.substring(0, content.indexOf(stopName)) +
                    content.substring(content.indexOf(stopName) + stopName.length());
        } else {
            while (content.contains(startName)) {
                String result = content.substring(content.indexOf(startName) + startName.length(),
                        content.indexOf(stopName));
                if (!names.contains(result)) {
                    names.add(result);
                }
                content = content.substring(0, content.indexOf(startName))
                        + content.substring(content.indexOf(stopName) + stopName.length());
            }
        }


        while (content.contains(startTypeName)) {
            String result = content.substring(content.indexOf(startTypeName) + startTypeName.length(),
                    content.indexOf(stopTypeName));
            if (!names.contains(result)) {
                names.add(result);
            }
            content = content.substring(0, content.indexOf(startTypeName))
                    + content.substring(content.indexOf(stopTypeName) + stopTypeName.length());
        }


        return names;
    }
}

