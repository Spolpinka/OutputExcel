package org.VPKfsspAnalyze;

import javax.swing.*;

public class Analyse {
    static String[][] fullBase;

    static WinInterface r;

    public static void main(String[] args) {
        //запрашиваем путь к папке, содержашей файлы для анализа
        JFrame.setDefaultLookAndFeelDecorated(true);
        r = new WinInterface("Анализ ответов приставов");
        r.setLocation(450, 150);
        r.setFocusableWindowState(true);
        r.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        r.setSize(400, 500);
        r.getGlassPane();
        r.setVisible(true);

    }

    static void setFieldInterface(String s) {
        r.setl3(s);
    }


}
