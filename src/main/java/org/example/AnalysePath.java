package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.IllegalFormatConversionException;
import java.util.Scanner;

public class AnalysePath {
    static int numbOfChoice;
    static boolean turnOff;
    protected String countFiles() {
        Scanner sc = new Scanner(System.in);
        //делаем основной выбор
        System.out.println("Что будем делать:\n" +
                "1 - анализировать xml файлы\n" +
                "2 - анализировать и сразу выдавать файл с жалобами для отправки через Финуслуги\n" +
                "3 - вести поиск имен всех упомянутых запросов, постановлений и т.п.\n" +
                "Введи число 1 или 2 или 3");
        String choise = sc.nextLine();
        //защита от дурака
        String path = "";
        while (true) {
            try {
                Integer.parseInt(choise);
                break;
            } catch (Exception e) {
                System.out.println("введено не число, введи 1 или 2 или 3");
                choise = sc.nextLine();
            }
        }
        while (true) {
            if (Integer.parseInt(choise) < 1 || Integer.parseInt(choise) > 3) {
                System.out.println("Введено число меньше 1 или больше 3, введи число от 1 до 3");
                choise = sc.nextLine();
            } else{
                break;
            }
        }
        numbOfChoice = Integer.parseInt(choise);
        //просим путь к папке
        if (numbOfChoice == 1 || numbOfChoice == 2 || numbOfChoice == 3) {
            System.out.println("Тогда введи путь к папке, где xml-ки лежат:");

            path = sc.nextLine();
            //защита от дурака
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

                    break;
                } else {
                    System.out.println("Слышь, криворукий, давай еще разок трайни, ничё не понятно");
                    path = sc.nextLine();
                }
            }
        }
        System.out.println("После окончания программы комп выключать?\n" +
                "1 - да\n" +
                "0 - нет");
        String choiceTurnOff = sc.nextLine();
        //защита от дурака
        while (true) {
            try {
                Integer.parseInt(choiceTurnOff);
                while (true) {
                    int choiceTurnOffInt = Integer.parseInt(choiceTurnOff);
                    if (choiceTurnOffInt == 0) {
                        turnOff = false;
                        break;
                    } else if (choiceTurnOffInt == 1) {
                        turnOff = true;
                        break;
                    } else {
                        System.out.println("Введено неверное число, надо 0 или 1");
                        choiceTurnOff = sc.nextLine();
                    }
                }
                break;
            } catch (IllegalFormatConversionException e) {
                System.out.println("введено не число, введи 1 или 2");
                choiceTurnOff = sc.nextLine();
            }
        }
        sc.close();
        return path;
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
