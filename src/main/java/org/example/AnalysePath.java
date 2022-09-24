package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.IllegalFormatConversionException;
import java.util.Scanner;

public class AnalysePath {
    static int numbOfChoice;
    static boolean turnOff;

    static boolean needAnalyseFile = true;
    static boolean needComplaintFile = true;

    static boolean exit = false;

    protected String countFiles() {
        Scanner sc = new Scanner(System.in);
        //делаем основной выбор
        System.out.println("Что будем делать:\n" +
                "1 - анализировать xml файлы,\n" +
                "2 - анализировать и сразу выдавать файл с жалобами для отправки через Финуслуги,\n" +
                "3 - вести поиск имен всех упомянутых запросов, постановлений и т.п.,\n" +
                "4 - сохранить выявленные имена в файл.\n" +
                "0 - закрыть программу.\n" +
                "Введи число");
        String choise = sc.nextLine();
        choise = sc.nextLine();
        //защита от дурака
        String path = "";
        while (true) {
            if (isDigit(choise)){
                break;
            }else{
                System.out.println("введено не число, введи 1 или 2 или 3");
                choise = sc.nextLine();
            }
        }
        while (true) {
            if (Integer.parseInt(choise) < 0 || Integer.parseInt(choise) > 4) {
                System.out.println("Введено число меньше 0 или больше 3, введи число от 1 до 3");
                choise = sc.nextLine();
            } else {
                numbOfChoice = Integer.parseInt(choise);
                break;
            }
        }
        //просим путь к папке
        switch (numbOfChoice) {
            case 0:
                exit = true;
                break;
            case 1:
            case 2:
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
                System.out.println("формировать файл для анализа ответов?\n" +
                        "0 - нет,\n" +
                        "1 - да.");
                int x = choise0Or1(sc.nextLine());
                if (x == 0) {
                    needAnalyseFile = false;
                }
                if (numbOfChoice == 2) {
                    System.out.println("Формировать файл для направления жалоб?\n" +
                            "0 - нет,\n" +
                            "1 - да.");
                    int z = choise0Or1(sc.nextLine());
                    if (z == 0) {
                        needComplaintFile = false;
                    }
                }

        }

        System.out.println("После окончания программы комп выключать?\n" +
                "1 - да\n" +
                "0 - нет");
        int y = choise0Or1(sc.nextLine());
        if (y == 0) {
            turnOff = false;
        } else {
            turnOff = true;
        }
        sc.close();
        return path;
    }

    protected ArrayList<String> filesArray(String path) {
        ArrayList<String> fullNames = new ArrayList<>();
        File[] pathNames;
        File f = new File(path);
        pathNames = f.listFiles();
        for (File pathName : pathNames) {
            fullNames.add(pathName.getPath());
        }
        return fullNames;
    }

    boolean isDigit(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    int choise0Or1(String s) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            if (isDigit(s)) {
                int i = Integer.parseInt(s);
                if (i >= 0 && i <= 1) {
                    return i;
                } else {
                    System.out.println("0 или 1");
                    s = sc.nextLine();
                }
            } else {
                System.out.println("Введи число!");
                s = sc.nextLine();
            }
        }
    }
}
