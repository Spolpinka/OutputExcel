package org.example;

import java.util.ArrayList;


public class Analyse {
    static String[][] fullBase;

    public static void main(String[] args) {
        //запрашиваем путь к папке, содержашей файлы для анализа
        AnalysePath analysPath = new AnalysePath();
        String path = analysPath.countFiles();
        switch (AnalysePath.numbOfChoice) {
            case 1:
            case 2:
                analyseXml(path);
                break;
            case 3:
                SearchNamesOfResolutions snor = new SearchNamesOfResolutions();
                ArrayList<String> fullListOfResolutions = snor.searching(path);
                break;
            case 4:
                ListOfNames lon = new ListOfNames();
                lon.setArrayOfNames(new ArrayList<>());
        }

        //анализ database на нарушения со стороны пристава
        if (AnalysePath.numbOfChoice == 2) {
            FormingComplaint formingComplaint = new FormingComplaint();
            formingComplaint.analisForComplaint(fullBase, path);
        }

        if (AnalysePath.turnOff) {
            TurnOff turnOff = new TurnOff();
            turnOff.getTurnOff();
        }

    }

    public static void analyseXml(String path) {
        AnalysePath analysPath = new AnalysePath();
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
                "Остаток долга по данным ФССП",//ниже этой строки можно вставлять новые поля, чтобы индексы не уехали
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
        //создаем потоки
        MultyThread multyThread1 = new MultyThread(forFirstThread, 0);
        multyThread1.start();
        MultyThread multyThread2 = new MultyThread(forSecondThread, lengthForFirst);
        multyThread2.start();
        MultyThread multyThread3 = new MultyThread(forThirdThread, lengthForFirst*2);
        multyThread3.start();
        MultyThread multyThread4 = new MultyThread(forLastThread, lengthForFirst*3);
        multyThread4.start();

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
    }
}
