package org.VPKfsspAnalyze;

class GetTextOfError {
    protected String getText(String content, String fileName) {
        String decomp = "<fssp:Text>";
        String decompEnd = "</fssp:Text>";
        String text = "";
        try {
            text = content.substring(content.indexOf(decomp) + decomp.length(), content.indexOf(decompEnd));
        } catch (Exception e) {
//            System.out.println("Видимо, несоблюдение структуры xml в файле " + fileName + ", там написано " +
//                    content.substring(120, 180));
        }
        return text;
    }
}
