package org.VPKfsspAnalyze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;


public class WinInterface extends JFrame {
    JButton clean, start;
    JLabel l1;
    //JLabel l2;
    JLabel l3;
    JLabel l4;
    JTextField t1, t2;
    JToolBar tool1;
    JCheckBox analyze, complaint, analyzeFile, complaintFile, newResolutionsSearch, newResolutionsSave, turnOff;
    eHandler handler = new eHandler();

    public WinInterface(String s) {
        super(s);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        clean = new JButton("Очистить все поля");
        start = new JButton("Начать процесс анализа");
        analyze = new JCheckBox("1. Анализ xml");
        complaint = new JCheckBox("2. Формирование жалоб");
        newResolutionsSearch = new JCheckBox("3. Поиск новых наименований постановлений");
        newResolutionsSave = new JCheckBox("4. Сохранение обновленного списка постановлений");
        turnOff = new JCheckBox("По завершении выключить комп?");
        l1 = new JLabel("Введи путь до .xml");
        tool1 = new JToolBar("Выбор нужного действия", SwingConstants.VERTICAL);
        tool1.add(analyze);
        tool1.add(complaint);
        tool1.add(newResolutionsSearch);
        tool1.add(newResolutionsSave);
        //l2 = new JLabel("нужны ли файлы?");
        analyzeFile = new JCheckBox("нужен ли файл для данных анализа?", false);
        complaintFile = new JCheckBox("нужен ли файл для жалоб?", false);
        l3 = new JLabel("");
        l4 = new JLabel("");
        t1 = new JTextField(25);
        t2 = new JTextField(25);
        //t1.addActionListener(handler);
        add(tool1);
        add(l1);
        add(t1);
        //add(l2);
        add(analyzeFile);
        add(complaintFile);
        add(turnOff);
        //add(t2);
        add(clean);
        add(start);
        clean.addActionListener(handler);
        start.addActionListener(handler);
        add(l3);
        add(l4);
    }

    //класс по обработке нажатий
    public class eHandler implements ActionListener  {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == start && checkPath(t1.getText())) {
                InterfaceTread it = new InterfaceTread();
                it.start();
                AddonThread thread1 = new AddonThread(analyze.isSelected(), complaint.isSelected(),
                        analyzeFile.isSelected(), complaintFile.isSelected(), turnOff.isSelected(), t1.getText());
                l4.setText("Поехали анализировать! казалось бы");
                thread1.start();
                try {
                    thread1.join();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getSource() == clean) {
                t1.setText(null);
                t2.setText(null);
                l3.setText("");
                l4.setText("");
                if (analyze.isSelected()) {
                    analyze.doClick();
                }
                if (complaint.isSelected()) {
                    complaint.doClick();
                }
                if (newResolutionsSearch.isSelected()) {
                    newResolutionsSearch.doClick();
                }
                if (newResolutionsSave.isSelected()) {
                    newResolutionsSave.doClick();
                }
                if (analyzeFile.isSelected()) {
                    analyzeFile.doClick();
                }
                if (complaintFile.isSelected()) {
                    complaintFile.doClick();
                }
                if (turnOff.isSelected()) {
                    turnOff.doClick();
                }

            }

        }
    }

    public class InterfaceTread extends Thread {
        public void run(){
            l3.setText("Привет из второго потока!");
        }
    }

    private boolean checkPath(String path) {
        if (new File(path).exists() /*&&
                new File(path).isDirectory() &&
                new File(path).listFiles().length == 0*/) {
            return true;
        } else if (new File(path).isFile()) {
            l3.setText("это путь к файлу, нужна папка!");
            t1.setText(null);
            return false;
        } else if (Objects.requireNonNull(new File(path).listFiles()).length == 0) {
            l3.setText("папка пуста!");
            t1.setText(null);
            return false;
        } else {
            l3.setText("Неизвестная ошибка, введи верный путь к xml!");
            t1.setText(null);
            return false;
        }
    }
}
