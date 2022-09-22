package org.example;

import java.util.ArrayList;

public class CountAllDocs {
    static int countOfFiles;

    public ArrayList<String> countDocs(String filename) {
        ListOfNames listOfNames = new ListOfNames();
        ArrayList<String> counts = new ArrayList<>();
        SaveObjects so = new SaveObjects();

        //создаем счетчики
        int[] count = new int[listOfNames.getArrayOfNames().size()];

        //переменные для типов ответов
        String deloNumForSearch = "<fssp:DeloNum>";
        String reject = "Уведомление об отсутствии исполнительного производства";
        String notReject = "Уведомление о ходе исполнительного производства";
        String negative = "Уведомление об отказе в предоставлении информации об исполнительном производстве";
        String message = "Сообщение (уведомление) с ответом на запрос";
        String content = "";

        //идентификатор первой строкой
        counts.add(filename.substring(filename.lastIndexOf("\\") + 1, filename.lastIndexOf(" ")));

        //получаем строку из файла (если поддиректория, то строка содержит все файлы)
        CreateContent cc = new CreateContent();
        content = cc.getString(filename, content);
        //выводим счетчик файлов
        System.out.println("анализируется файл № " + (countOfFiles + 1) + " " + filename);
        countOfFiles++;

        //получаем текст пояснений к отказу
        GetTextOfError getTextOfError = new GetTextOfError();

        //считываем сумма долга по данным ФССП
        String sumOfDebt = sumOfDebt(content);

        //считываем ФИО пристава
        String nameIdentificator1 = "<fssp:AuthorExecutorName>";
        String nameIdentificator2 = "</fssp:AuthorExecutorName>";
        String fullName = content.substring(content.indexOf(nameIdentificator1) + nameIdentificator1.length(),
                content.indexOf(nameIdentificator2));
        String lastName = fullName.substring(0, fullName.indexOf(" "));
        fullName = fullName.substring(fullName.indexOf(" ") + 1);
        String firstName = fullName.substring(0, fullName.indexOf(" "));
        String middleName = fullName.substring(fullName.indexOf(" ") + 1);

        //считываем ID ФССП
        String startOfID = "<fssp:QueryNumber>";
        String endOfID = "</fssp:QueryNumber>";
        String ID = content.substring(content.indexOf(startOfID) + startOfID.length(), content.indexOf(endOfID));

        //определяем тип ответа

        if (content.contains(reject)) {
            counts.add(reject);
            counts.add(getTextOfError.getText(content, filename));
            fillingOfCounts(counts);//заполняем нулями для соответствия полей
            counts.add(lastName);
            counts.add(firstName);
            counts.add(middleName);
            counts.add(ID);
            return counts;
        } else if (content.contains(notReject)) {
            counts.add(notReject);
        } else if (content.contains(negative)) {
            counts.add(negative);
            counts.add(getTextOfError.getText(content, filename));
            fillingOfCounts(counts);//заполняем нулями для соответствия полей
            counts.add(lastName);
            counts.add(firstName);
            counts.add(middleName);
            counts.add(ID);
            return counts;
        } else if (content.contains(message)) {
            counts.add(message);
            counts.add(getTextOfError.getText(content, filename));
            fillingOfCounts(counts);//заполняем нулями для соответствия полей
            counts.add(lastName);
            counts.add(firstName);
            counts.add(middleName);
            counts.add(ID);
            return counts;
        } else {
            counts.add("фигня какая-то");
            return counts;
        }

        //ищем и вставляем в базу номер ИП
        if (content.contains(deloNumForSearch)) {
            try {
                String deloNum = content.substring(content.indexOf(deloNumForSearch) + deloNumForSearch.length(), content.indexOf("-ИП") + 3);
                counts.add(deloNum);
            } catch (Exception e) {
                System.out.println(e + " в файле " + filename);
            }
        } else {
            counts.add("№ ИП в файле отсутствует");
        }

        //считаем постановления

        for (int i = 0; i < so.loadArray().size(); i++) {
            while (content.contains(so.loadArray().get(i))) {
                content = content.substring(0, content.indexOf(so.loadArray().get(i))) +
                        content.substring(content.indexOf(so.loadArray().get(i)) +
                                so.loadArray().get(i).length());
                count[i]++;
            }
        }

        for (int j : count) {
            counts.add("" + j);
        }
        //заводим описания по счетам, машинам, недвижимости
        StringBuilder accounts = new StringBuilder();
        int countOFNotNullAccounts = 0;
        double sumOfFindedFunds = 0;
        StringBuilder placeOfWork = new StringBuilder();
        StringBuilder transport = new StringBuilder();
        StringBuilder estate = new StringBuilder();
        StringBuilder other = new StringBuilder();
        while (content.contains("<fssp:Description>")) {
            content = content.substring(content.indexOf("<fssp:Description>") + "<fssp:Description>".length());
            String s = content.substring(0, content.indexOf("</fssp:Description"));

            if (s.contains("Счет|")) {
                try {
                    String start = s.substring(s.indexOf("|") + "|".length());
                    String acc = start.substring(0, 20);
                    start = start.substring(start.indexOf("сумма") + "сумма".length());
                    String sumOFAcc = start.substring(0, start.indexOf("|"));
                    if (sumOFAcc.contains(" ")) sumOFAcc = sumOFAcc.replace(" ", "");
                    //acc = s.substring(s.indexOf("|") + 1, s.indexOf("|") + 20);
                    if (!accounts.toString().contains(acc)) {
                        accounts.append(acc).append(" ");
                        if (!sumOFAcc.isEmpty() && Double.parseDouble(sumOFAcc) > 0) {
                            countOFNotNullAccounts += 1;
                            sumOfFindedFunds += Double.parseDouble(sumOFAcc);//добавляем найденную сумму в общую
                            //System.out.println(sumOFAcc);
                            //System.out.println(Double.parseDouble(sumOFAcc));
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e + " похоже неверно сформирован файл");
                }
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

            content = content.substring(content.indexOf("</fssp:Description>") + "</fssp:Description>".length());
        }

        //добавляем счета и т.п.
        counts.add(accounts.toString());
        counts.add(countOFNotNullAccounts + "");
        counts.add(sumOfFindedFunds + "");
        counts.add(transport.toString());
        counts.add(placeOfWork.toString());
        counts.add(estate.toString());
        counts.add(other.toString());
        counts.add(sumOfDebt);//здесь можно добавлять, что индексы не улетели
        counts.add(lastName);
        counts.add(firstName);
        counts.add(middleName);
        counts.add(ID);
        //возвращаем список
        return counts;
    }

    private ArrayList<String> fillingOfCounts(ArrayList<String> counts) {
        SaveObjects so = new SaveObjects();
        for (int i = 0; i < so.loadArray().size() + 8; i++) {
            counts.add("0");
        }
        return counts;
    }

    private String sumOfDebt(String content) {
        String sumOfDebt = "";
        String openBoard = "<fssp:IPAcctRecords>";
        String closeBoard = "</fssp:IPAcctRecords>";
        String openSum = "<fssp:Amount>";
        String closeSum = "</fssp:Amount>";
        if (content.contains(openBoard)) {
            String subContent = content.substring(content.indexOf(openBoard) + openBoard.length(), content.indexOf(closeBoard));
            if (subContent.contains("<fssp:RegType>201") && subContent.contains("<fssp:DestType>01")) {
                sumOfDebt = subContent.substring(subContent.indexOf(openSum) + openSum.length(), subContent.indexOf(closeSum));
            } else {
                sumOfDebt = "ошибка, сумма задолженности не в первой части IPAcctRecords";
            }
        } else {
            sumOfDebt = null;
        }
        return sumOfDebt;
    }
}

