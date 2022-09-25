package org.VPKfsspAnalyze;

import javax.swing.*;
import java.util.ArrayList;

public class Analyse {
    static String[][] fullBase;

    static WinInterface r;

    public static void main(String[] args) {
        //запрашиваем путь к папке, содержашей файлы для анализа
        r = new WinInterface("Анализ ответов приставов");
        r.setLocation(450, 150);
        r.setVisible(true);
        r.setFocusableWindowState(true);
        r.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        r.setSize(400, 500);

    }


}
