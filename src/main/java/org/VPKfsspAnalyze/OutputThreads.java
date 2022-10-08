package org.VPKfsspAnalyze;

public class OutputThreads extends Thread {
    String[][] base;
    String path;
    String fileName;

    public OutputThreads(String[][] base, String path, String fileName) {
        this.base = base;
        this.path = path;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        Output.excel(base, path, fileName);
    }
}
