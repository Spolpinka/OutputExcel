package org.example;

import java.util.ArrayList;


public class analisys {
    static int countOfFiles;

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
        String[][] fullBase = new String[fullNames.size() + 1][3 + namesOfResolutions.size() + addNames.length];
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

        //заполняем базу по всем файлам
        CountAllDocs countAllDocs = new CountAllDocs();
        for (int i = 0; i < fullNames.size(); i++) {
            //прогоняем файлы через счетчик
            ArrayList<String> counts = countAllDocs.countDocs(fullNames.get(i));
            for (int j = 0; j < counts.size(); j++) {
                fullBase[i + 1][j] = counts.get(j);
                //System.out.println(counts.get(j));
            }
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
