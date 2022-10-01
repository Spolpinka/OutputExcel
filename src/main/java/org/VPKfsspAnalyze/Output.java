package org.VPKfsspAnalyze;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

class Output {
    static void txt(String[] @NotNull [] fullBase, String path) {
        try (FileWriter writer = new FileWriter(path.substring(0, path.lastIndexOf('\\') + 1) + "result.txt", false)) {
            for (int i = 0; i < fullBase.length; i++) {
                for (int j = 0; j < fullBase[i].length; j++) {
                    writer.write(fullBase[i][j] + ";");
                }
                writer.write("\n");
            }
            writer.close();
            System.out.println("Все ок, ищи файл " + path.substring(0, path.lastIndexOf('\\') + 1) + "result.txt");
        } catch (IOException ex) {

        }
    }

    static int countOfRow;

    static void excel(String[][] fullBase, String path, String xlsxFileName) {
        // String xlsxFileName = "report" + path.substring(path.lastIndexOf("\\") + 1) + ".xlsx";

        String fileName = path.substring(0, path.lastIndexOf("\\") + 1) + xlsxFileName + ".xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Анализ ответов приставов");

        int rowNum = 0;
        System.out.println("Заливаем данные");

        for (String[] string : fullBase) {
            if (string != null) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (int i = 0; i < string.length; i++) {
                    Cell cell = row.createCell(colNum++);

                    if (string[i] != null) {
                        if (string[i].length() < 8000) {
                            cell.setCellValue(string[i]);
                        } else {
                            string[i] = string[i].substring(0, 7900) + " строка была обрезана, слишком много символов";
                        }
                    }

                }
            }
            try {
                FileOutputStream outputStream = new FileOutputStream(fileName);
                workbook.write(outputStream);
                countOfRow++;
                System.out.println("Процесс записи строк " + countOfRow);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println(" нет такого файла");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(" ошибка ввода-вывода");
            }
        }
        System.out.println("Все ок, ищи файл " + path.substring(0, path.lastIndexOf('\\') + 1) + xlsxFileName + ".xlsx");
    }
}
