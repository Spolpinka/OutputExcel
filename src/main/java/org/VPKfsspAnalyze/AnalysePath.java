package org.VPKfsspAnalyze;

import java.io.File;
import java.util.ArrayList;

class AnalysePath {
    protected ArrayList<String> filesArray(String path) {
        ArrayList<String> fullNames = new ArrayList<>();
        File[] pathNames;
        File f = new File(path);
        pathNames = f.listFiles();
        for (File pathName : pathNames) {
            fullNames.add(pathName.getPath());
        }
        return fullNames;
    }
}
