package org.VPKfsspAnalyze;

import java.io.IOException;

class TurnOff {

    public void getTurnOff() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec("shutdown.exe -s -t 0");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }
}
