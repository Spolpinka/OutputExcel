package org.VPKfsspAnalyze;

public class RejectionBase {
    private static String[][] rejectionBase = new String[Analyse.fullBase.length][3];//по всем файлам id : тип отказа : пояснения
    private static int rowCount;
    public void addRejection(String id, String type, String reason) {
        rejectionBase[rowCount][0] = id;
        rejectionBase[rowCount][1] = type;
        rejectionBase[rowCount][2] = reason;
        rowCount++;
    }

    public void writeRejectionFile(String path) {
        GetTime gt = new GetTime();
        String fileName = "RejectionsReport" + gt.getTime() + ".xlsx";
        OutputThreads ot = new OutputThreads(rejectionBase, path, fileName);
    }
}
