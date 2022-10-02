package org.VPKfsspAnalyze;

class AddonThread extends Thread {
    boolean isNeedAnalyze, isNeedComplaint, isNeedAnalyzeFile, isNeedComplaintFile, turnOff;
    String path;

    AddonThread(boolean isNeedAnalyze, boolean isNeedComplaint,
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
        System.out.println("привет из доппотока");
        Analyse.setFieldInterface("привет из анализа xml");
        /*XmlAnalyze xa = new XmlAnalyze();
        if (isNeedAnalyze) {
            Analyse.setFieldInterface("привет из анализа xml");
            xa.analyseXml(path, isNeedAnalyzeFile);
        }
        if (isNeedComplaint) {
            FormingComplaint fc = new FormingComplaint();
            fc.analyzeForComplaint(path, isNeedComplaintFile);
        }
        if (turnOff) {
            TurnOff to = new TurnOff();
            to.getTurnOff();
        }*/
    }
}
