package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CreateContent {

    static String getString(String filename, String content) {
        File file = new File(filename);
        if (file.isDirectory()) {
            File[] pathNames;
            File f = new File(filename);
            pathNames = f.listFiles();
            ArrayList<String> fullNamesForSubdir = new ArrayList<>();
            for (File pathName : pathNames) {
                fullNamesForSubdir.add(pathName.getPath());
            }
            try {
                StringBuilder contentBuilder = new StringBuilder(content);
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
