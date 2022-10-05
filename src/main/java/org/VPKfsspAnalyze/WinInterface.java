package org.VPKfsspAnalyze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;


class WinInterface extends JFrame {
    private final JButton clean, start;
    private final JLabel l3;
    private final JLabel l4;
    private final JTextField t1;
    private final JCheckBox analyze, complaint, analyzeFile, complaintFile, newResolutionsSearch, newResolutionsSave, turnOff;

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
        JToolBar tool1 = new JToolBar("Выбор нужного действия", SwingConstants.VERTICAL);
        tool1.add(analyze);
        tool1.add(complaint);
        tool1.add(newResolutionsSearch);
        tool1.add(newResolutionsSave);
        analyzeFile = new JCheckBox("нужен ли файл для данных анализа?", false);
        complaintFile = new JCheckBox("нужен ли файл для жалоб?", false);
        l3 = new JLabel("");
        l4 = new JLabel("");
        t1 = new JTextField("путь до xml");
        //t2 = new JTextField();
        tool1.add(t1);
        tool1.add(analyzeFile);
        tool1.add(complaintFile);
        tool1.add(turnOff);
        tool1.add(clean);
        tool1.add(start);
        tool1.add(l3);
        tool1.add(l4);
        getContentPane().add(tool1);
        eHandler handler = new eHandler();
        clean.addActionListener(handler);
        start.addActionListener(handler);
        t1.addActionListener(handler);

    }

    //класс по обработке нажатий
    public class eHandler implements ActionListener  {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == start || e.getSource() == t1) {
                AddonThread thread1 = new AddonThread(analyze.isSelected(), complaint.isSelected(),
                        analyzeFile.isSelected(), complaintFile.isSelected(), turnOff.isSelected(), t1.getText());
                thread1.start();
                l4.setText("Поехали анализировать! казалось бы");
                XmlAnalyze xa = new XmlAnalyze();
                if (analyze.isSelected()) {
                    Analyse.setFieldInterface("привет из анализа xml");
                    /*l3.setText("второй привет");
                    l4.setText("третий привет");*/
                    xa.analyseXml(t1.getText(), analyzeFile.isSelected());
                }
                if (complaint.isSelected()) {
                    FormingComplaint fc = new FormingComplaint();
                    fc.analyzeForComplaint(t1.getText(), complaintFile.isSelected());
                }
                if (turnOff.isSelected()) {
                    TurnOff to = new TurnOff();
                    to.getTurnOff();
                }

            }
            if (e.getSource() == clean) {
                t1.setText(null);
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

    void setl3(String s) {
        if (s != null && !s.isBlank() && !s.isEmpty()) {
            l3.setText(s);
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
