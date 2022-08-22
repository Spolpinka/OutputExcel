package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Output {
    static void txt(String[][] fullBase, String path) {
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


    static void excel(String[][] fullBase, String path) {
        String fileName = path.substring(0, path.lastIndexOf("\\") + 1) + "report1.xlsx";
        System.out.println(fileName);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Анализ ответов приставов");

        int rowNum = 0;
        System.out.println("Заливаем данные");

        for (String[] string : fullBase) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String field : string) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(field);
            }
            try {
                FileOutputStream outputStream = new FileOutputStream(fileName);
                workbook.write(outputStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
