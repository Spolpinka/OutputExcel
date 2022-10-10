package org.VPKfsspAnalyze;

import java.util.ArrayList;

class XmlAnalyze {
    public void analyseXml(String path, boolean isNeedAnalyzeFile) {
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
        Analyse.fullBase = new String[fullNames.size() + 1][3 + namesOfResolutions.size() + addNames.length];
        //устанавливаем верхнюю строку
        Analyse.fullBase[0][0] = "идентификатор";
        Analyse.fullBase[0][1] = "тип ответа";
        Analyse.fullBase[0][2] = "№ ИП / причина отказа";
        for (int i = 0; i < namesOfResolutions.size(); i++) {
            Analyse.fullBase[0][i + 3] = namesOfResolutions.get(i);
        }

        for (int i = 0; i < addNames.length; i++) {
            Analyse.fullBase[0][i + 3 + namesOfResolutions.size()] = addNames[i];
        }
        //делим fullNames для распределения по потокам
        final int numberOfTheards = 4;
        int lengthForFirst = (Analyse.fullBase.length - 1) / numberOfTheards;
        int lengthForSecond = (Analyse.fullBase.length - 1) / numberOfTheards;
        int lengthForThird = (Analyse.fullBase.length - 1) / numberOfTheards;
        int lengthForLast = Analyse.fullBase.length - (((Analyse.fullBase.length - 1) / numberOfTheards) * 3);
        /*System.out.println("высота массива " + fullBase.length);
        System.out.println("высота первого потока " + lengthForFirst);
        System.out.println("высота последнего потока " + lengthForLast);*/
        ArrayList<String> forFirstThread = new ArrayList<>();
        ArrayList<String> forSecondThread = new ArrayList<>();
        ArrayList<String> forThirdThread = new ArrayList<>();
        ArrayList<String> forLastThread = new ArrayList<>();
        //наполняем список файло для первого потока
        for (int i = 0; i < lengthForFirst; i++) {
            forFirstThread.add(fullNames.get(0));
            fullNames.remove(0);
        }
        //System.out.println("остаток массива имен после первого выделения " + fullNames.size());
        //для второго
        for (int i = 0; i < lengthForSecond; i++) {
            forSecondThread.add(fullNames.get(0));
            fullNames.remove(0);
        }
        //System.out.println("остаток массива имен после второго выделения " + fullNames.size());
        //для третьего
        for (int i = 0; i < lengthForThird; i++) {
            forThirdThread.add(fullNames.get(0));
            fullNames.remove(0);
        }
        //System.out.println("остаток массива имен после третьего выделения " + fullNames.size());
        //для последнего
        for (int i = 0; i < lengthForLast; i++) {
            if (fullNames.size() == 0) {
                break;
            }
            forLastThread.add(fullNames.get(0));
            fullNames.remove(0);
        }
        /*System.out.println("остаток массива имен после последнего выделения " + fullNames.size());
        System.out.println("длина массива для первого потока " + forFirstThread.size() + " а должна быть " + lengthForFirst);*/
        //создаем потоки
        AnalyzeThread analyzeThread1 = new AnalyzeThread(forFirstThread, 0);
        analyzeThread1.start();
        AnalyzeThread analyzeThread2 = new AnalyzeThread(forSecondThread, lengthForFirst);
        analyzeThread2.start();
        AnalyzeThread analyzeThread3 = new AnalyzeThread(forThirdThread, lengthForFirst * 2);
        analyzeThread3.start();
        AnalyzeThread analyzeThread4 = new AnalyzeThread(forLastThread, lengthForFirst * 3);
        analyzeThread4.start();

        //ждем, пока все потоки отработают
        try {
            analyzeThread1.join();
            analyzeThread2.join();
            analyzeThread3.join();
            analyzeThread4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //выводим в txt
        //Output.txt(fullBase, path);

        //выводим в эксель
        if (isNeedAnalyzeFile) {
            GetTime gt = new GetTime();
            OutputThreads ot = new OutputThreads(Analyse.fullBase, path, "MainReport" + gt.getTime());
            ot.start();
            System.out.println("пошел поток записи общего отчета");
        }
    }
}
