package org.VPKfsspAnalyze;

class GetTextOfError {
    protected String getText(String content) {
        String decomp = "<fssp:Text>";
        String decompEnd = "</fssp:Text>";
        String text = "описание причины отказа отсутствует";
        try {
            text = content.substring(content.indexOf(decomp) + decomp.length(), content.indexOf(decompEnd));
        } catch (Exception e) {
//            System.out.println("Видимо, несоблюдение структуры xml в файле " + fileName + ", там написано " +
//                    content.substring(120, 180));
        }
        return text;
    }
}
