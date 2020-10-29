public class PDFTextOnlyCompare {

    private static String errorMessage;

    public static boolean pdfCompare(PDFFile file1, PDFFile file2) {
        try {
            // get file name add create new 2 result file names
            // ex. old-oldFileName-newFileName.pdf
            // ex. new-oldFileName-newFileName.pdf
            String fileName1 = file1.getTargetFileName().split("[.]")[0];
            String fileName2 = file2.getTargetFileName();

            file1.setResultFileName("old-" + fileName1 + "-" + fileName2);
            file2.setResultFileName("new-" + fileName1 + "-" + fileName2);

            new PDFCompareText(file1, file2).findNotMatchPos();

            // you can't share 2 PDFHighlighter.highlight() to each Thread because of there have static attribute
            PDFHighlighter.highlight(file1, Setting.getOldDifColor());
            System.out.println("Created : " + file1.getResultPath());

            PDFHighlighter.highlight(file2, Setting.getNewDifColor());
            System.out.println("Created : " + file2.getResultPath());

            // if there no error here so return null
            errorMessage = null;
        } catch (Exception ex) {
            // if there have error you can get cause message
            errorMessage = "PDF text only compare error : " + ex.getMessage();
            ex.printStackTrace();
        }

        // if there have no error message then send true
        return errorMessage == null;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }

}
