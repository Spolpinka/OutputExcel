package org.example;

import java.util.ArrayList;




public class analisys {

    public static void main(String[] args) {
        //запрашиваем путь к папке, содержашей файлы для анализа
        String path = analisPath.countFiles();

        //получаем перечень наименований файлов из папки
        ArrayList<String> fullNames = analisPath.filesArray(path);

        //получаем набор поставлений/действий/наименований типов документов
        ArrayList<String> namesOfResolutions = listOfNames.getArrayOfNames();

        System.out.println("количество наименований документов в анализе - " + namesOfResolutions.size());

        String[][] fullBase = new String[fullNames.size() + 1][namesOfResolutions.size() + 8];
        //устанавливаем верхнюю строку
        fullBase[0][0] = "идентификатор";
        fullBase[0][1] = "тип ответа";
        fullBase[0][2] = "№ ИП / причина отказа";
        for (int i = 0; i < namesOfResolutions.size(); i++) {
            fullBase[0][i + 3] = namesOfResolutions.get(i);
        }
        fullBase[0][3 + namesOfResolutions.size()] = "Счета";
        fullBase[0][4 + namesOfResolutions.size()] = "Место работы";
        fullBase[0][5 + namesOfResolutions.size()] = "Транспорт";
        fullBase[0][6 + namesOfResolutions.size()] = "Недвижимость";
        fullBase[0][7 + namesOfResolutions.size()] = "Иное";
        //заполняем базу по всем файлам
        for (int i = 0; i < fullNames.size(); i++) {
            //прогоняем файлы через счетчик
            ArrayList<String> counts = countDocs(fullNames.get(i));
            for (int j = 0; j < counts.size(); j++) {
                fullBase[i + 1][j] = counts.get(j);
                //System.out.println(counts.get(j));
            }
        }
        //выводим в txt
        Output.txt(fullBase, path);
        //выводим в эксель
        Output.excel(fullBase, path);

    }

    //метод для анализа каждого переданного файла
    public static ArrayList<String> countDocs(String filename) {
        ArrayList<String> counts = new ArrayList<>();

        //создаем счетчики
        int[] count = new int[listOfNames.getArrayOfNames().size()];
        String deloNumForSeach = "<fssp:DeloNum>";
        String reject = "Уведомление об отсутствии исполнительного производства";
        String notReject = "Уведомление о ходе исполнительного производства";
        String negative = "Уведомление об отказе в предоставлении информации об исполнительном производстве";
        String message = "Сообщение (уведомление) с ответом на запрос";
        String content = "";

        //идентификатор первой строкой
        counts.add(filename.substring(filename.lastIndexOf("\\") + 1, filename.lastIndexOf(" ")));

        //получаем строку из файла (если поддиректория, то строка содержит все файлы)
        content = CreateContent.getString(filename, content);

        //определяем тип ответа

        if (content.contains(reject)) {
            counts.add(reject);
            //counts.add(content.substring(content.indexOf("Text") + 5, content.indexOf("</")));
            return counts;
        } else if (content.contains(notReject)) {
            counts.add(notReject);
        } else if (content.contains(negative)) {
            counts.add(negative);
            //counts.add(content.substring(content.indexOf("Text") + 5, content.indexOf("</")));
            return counts;
        } else if (counts.contains(message)) {
            counts.add(message);
            //counts.add(content.substring(content.indexOf("Text") + 5, content.indexOf("</")));
            return counts;
        } else counts.add("фигня какая-то");

        //ищем и вставляем в базу номер ИП
        if (content.contains(deloNumForSeach)) {
            try {
                String deloNum = content.substring(content.indexOf(deloNumForSeach) + deloNumForSeach.length(), content.indexOf("-ИП") + 3);
                counts.add(deloNum);
            } catch (Exception e) {
                System.out.println(e + " в файле " + filename);
            }
        } else {
            counts.add("№ ИП в файле отсутствует");
        }

        //считаем постановления
        for (int i = 0; i < listOfNames.getArrayOfNames().size(); i++) {
            while (content.contains(listOfNames.getArrayOfNames().get(i))) {
                content = content.substring(0, content.indexOf(listOfNames.getArrayOfNames().get(i))) +
                        content.substring(content.indexOf(listOfNames.getArrayOfNames().get(i)) + listOfNames.getArrayOfNames().get(i).length());
                count[i]++;
            }
        }

        for (int j : count) {
            counts.add("" + j);
        }

        //заводим описания по счетам, машинам, недвижимости
        StringBuilder accounts = new StringBuilder();
        StringBuilder placeOfWork = new StringBuilder();
        StringBuilder transport = new StringBuilder();
        StringBuilder estate = new StringBuilder();
        StringBuilder other = new StringBuilder();
        while (content.contains("<fssp:Description>")) {
            content = content.substring(content.indexOf("<fssp:Description>") + "<fssp:Description>".length());
            String s = content.substring(0, content.indexOf("</fssp:Description"));
            if (s.contains("Счет")) {
                s = s.substring(s.indexOf("|") + 1, s.indexOf("|") + 21);
                if (!accounts.toString().contains(s)) {
                    accounts.append(s).append(" ");}
            } else if (s.contains("Место получения дохода")) {
                s = s.substring(s.indexOf("|") + 1);
                if (!placeOfWork.toString().contains(s)) placeOfWork.append(s).append(" ");
            } else if (s.contains("Транспорт")) {
                s = s.substring(s.indexOf("|") + 1);
                if (!transport.toString().contains(s)) transport.append(s).append(" ");
            } else if (s.contains("Недвижимость")) {
                s = s.substring(s.indexOf("|") + 1);
                if (!estate.toString().contains(s)) estate.append(s).append(" ");
            } else {
                if (!other.toString().contains(s)) other.append(s).append(" ");
            }

            content = content.substring(content.indexOf("</fssp:Description"));
        }

        //добавляем счета и т.п.
        counts.add(accounts.toString());
        counts.add(placeOfWork.toString());
        counts.add(transport.toString());
        counts.add(estate.toString());
        counts.add(other.toString());
        //возвращаем список
        return counts;
    }

}
