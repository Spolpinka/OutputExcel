package org.example;

import java.util.ArrayList;


public class Analyse {
    static String[][] fullBase;

    public static void main(String[] args) {
        //запрашиваем путь к папке, содержашей файлы для анализа
        AnalysPath analysPath = new AnalysPath();
        String path = analysPath.countFiles();

        //получаем перечень наименований файлов из папки
        ArrayList<String> fullNames = analysPath.filesArray(path);

        //получаем набор поставлений/действий/наименований типов документов
        //ListOfNames listOfNames = new ListOfNames();
        SaveObjects so = new SaveObjects();
        ArrayList<String> namesOfResolutions = so.loadArray();

        System.out.println("количество наименований документов в анализе - " + namesOfResolutions.size()
                + "\n количество файлов для анализа " + fullNames.size() + " шт.");

        //массив строк с названиями дополнительных данных, которые будут в конце
        String[] addNames = new String[]{
                "Счета",
                "Количество счетов с ненулевым остатком",
                "общая сумма найденых денежных средств",
                "Транспорт",
                "Место работы",
                "Недвижимость",
                "Иное",
                "Фамилия",
                "Имя",
                "Отчество",
                "ID запроса в ФССП"
        };
        fullBase = new String[fullNames.size() + 1][3 + namesOfResolutions.size() + addNames.length];
        //устанавливаем верхнюю строку
        fullBase[0][0] = "идентификатор";
        fullBase[0][1] = "тип ответа";
        fullBase[0][2] = "№ ИП / причина отказа";
        for (int i = 0; i < namesOfResolutions.size(); i++) {
            fullBase[0][i + 3] = namesOfResolutions.get(i);
        }

        for (int i = 0; i < addNames.length; i++) {
            fullBase[0][i + 3 + namesOfResolutions.size()] = addNames[i];
        }
        //делим fullNames для распределения по потокам
        final int numberOfTheards = 4;
        int lengthForFirst = fullNames.size() / numberOfTheards;
        int lenghtForSecond = fullNames.size() / numberOfTheards;
        int lenghtForThird = fullNames.size() / numberOfTheards;
        int lengthForLast = fullNames.size() - ((fullNames.size() / numberOfTheards)*3);
        ArrayList<String> forFirstThread = new ArrayList<>();
        ArrayList<String> forSecondThread = new ArrayList<>();
        ArrayList<String> forThirdThread = new ArrayList<>();
        ArrayList<String> forLastThread = new ArrayList<>();
        //наполняем список файло для первого потока
        for (int i = 0; i < lengthForFirst; i++) {
            forFirstThread.add(fullNames.get(0));
            fullNames.remove(0);
        }
        //для второго
        for (int i = 0; i < lenghtForSecond; i++) {
            forSecondThread.add(fullNames.get(0));
            fullNames.remove(0);
        }
        //для третьего
        for (int i = 0; i < lenghtForThird; i++) {
            forThirdThread.add(fullNames.get(0));
            fullNames.remove(0);
        }
        //для последнего
        for (int i = 0; i < lengthForLast; i++) {
            forLastThread.add(fullNames.get(0));
            fullNames.remove(0);
        }
       //печатает список первого потока
        /*for (int i = 0; i < forFirstThread.size(); i++) {
            System.out.println(forFirstThread.get(i));
        }*/
        System.out.println("это был список файлов первого потока");
        //печатаем список последнего потока
        /*for (int i = 0; i < forLastThread.size(); i++) {
            System.out.println(forLastThread.get(i));
        }*/

        //создаем потоки
        MultyThread multyThread1 = new MultyThread(forFirstThread, 0);
        multyThread1.start();
        MultyThread multyThread2 = new MultyThread(forSecondThread, lengthForFirst);
        multyThread2.start();
        MultyThread multyThread3 = new MultyThread(forThirdThread, lengthForFirst*2);
        multyThread3.start();
        MultyThread multyThread4 = new MultyThread(forLastThread, lengthForFirst*3);
        multyThread4.start();
        //заполняем базу по всем файлам
        /*CountAllDocs countAllDocs = new CountAllDocs();
        int countOfLines = 0;
        for (int i = 0; i < forLastThread.size(); i++) {

            //прогоняем файлы через счетчик
            ArrayList<String> counts = countAllDocs.countDocs(forLastThread.get(i));
            for (int j = 0; j < counts.size(); j++) {
                fullBase[i+1][j] = counts.get(j);
                //System.out.println(counts.get(j));
            }
        }*/
        //ждем, пока все потоки отработают
        try {
            multyThread1.join();
            multyThread2.join();
            multyThread3.join();
            multyThread4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //выводим в txt
        //Output.txt(fullBase, path);

        //выводим в эксель
        Output.excel(fullBase, path, "MainReport");

        //анализ database на нарушения со стороны пристава
        FormingComplaint formingComplaint = new FormingComplaint();
        formingComplaint.analisForComplaint(fullBase, path);

    }


}
