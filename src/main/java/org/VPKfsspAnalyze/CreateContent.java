package org.VPKfsspAnalyze;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class CreateContent {

    String getString(String filename, String content) {
        File file = new File(filename);
        if (file.isDirectory()) {
            File[] pathNames;
            File f = new File(filename);
            pathNames = f.listFiles();
            ArrayList<String> fullNamesForSubdir = new ArrayList<>();
            for (File pathName : pathNames) {
                fullNamesForSubdir.add(pathName.getPath());
            }
            int maxSumOfFiles = 100;
            if (fullNamesForSubdir != null && fullNamesForSubdir.size() > maxSumOfFiles) return "Больше " +
                    maxSumOfFiles + "в поддиректории " + filename + " ты мне всю оперативку забил, балбес!";
            try {
                StringBuilder contentBuilder = new StringBuilder(content);//что то тут не так для файла S01-17-311452-1
                for (String s : fullNamesForSubdir) {
                    contentBuilder.append(Files.lines(Paths.get(s)).reduce("", String::concat));
                }
                content = contentBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Проблемы с чтением файла " + filename);
            }
        } else {

            try {
                content = Files.lines(Paths.get(filename)).reduce("", String::concat);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Проблемы с чтением файла " + filename);
            }
        }
        return content;
    }
}
