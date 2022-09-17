package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class AnalysPath {
    protected String countFiles() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введи что-ли путь к папке... ");
        String path = sc.nextLine();
        while (true) {
            if (new File(path).isFile()) {
                System.out.println("Вообще то это просто файл, давай не косячь, дай имя папки!");
                path = sc.nextLine();

            } else if (!new File(path).exists() && path.contains(":\\")) {
                System.out.println("Такая папка не существует! Давай еще раз, только теперь сначала подумай...");
                path = sc.nextLine();

            } else if (new File(path).listFiles().length == 0) {
                System.out.println("А каталог то пустой! Может хоть будешь проверять путь перед вводом?");
                path = sc.nextLine();

            } else if (path.contains(":\\")) {
                sc.close();
                return path;
            } else {
                System.out.println("Слышь, криворукий, давай еще разок трайни, ничё не понятно");
                path = sc.nextLine();
            }
        }
    }

    protected ArrayList<String> filesArray (String path){
        ArrayList<String> fullNames = new ArrayList<>();
        File[] pathNames;
        File f = new File(path);
        pathNames = f.listFiles();
        for (File pathName : pathNames) {
            fullNames.add(pathName.getPath());
        }
        return fullNames;
    }

}
