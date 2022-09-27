package org.VPKfsspAnalyze;

public class AddonThread extends Thread {
    boolean isNeedAnalyze, isNeedComplaint, isNeedAnalyzeFile, isNeedComplaintFile, turnOff;
    String path;

    public AddonThread(boolean isNeedAnalyze, boolean isNeedComplaint,
                       boolean isNeedAnalyzeFile, boolean isNeedComplaintFile,
                       boolean turnOff, String path) {
        this.isNeedAnalyze = isNeedAnalyze;
        this.isNeedComplaint = isNeedComplaint;
        this.isNeedAnalyzeFile = isNeedAnalyzeFile;
        this.isNeedComplaintFile = isNeedComplaintFile;
        this.turnOff = turnOff;
        this.path = path;
    }
    public void run() {
        XmlAnalyze xa = new XmlAnalyze();
        if (isNeedAnalyze) {
            //l4.setText("Процесс анализа файлов продолжается!");
            xa.analyseXml(path, isNeedAnalyzeFile);
        }
        if (isNeedComplaint) {
            FormingComplaint fc = new FormingComplaint();
            fc.analyzeForComplaint(path, isNeedComplaintFile);
        }
        if (turnOff) {
            TurnOff to = new TurnOff();
            to.getTurnOff();
        }
    }
}