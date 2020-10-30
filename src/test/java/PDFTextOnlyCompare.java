public class PDFTextOnlyCompare {

    private String errorMessage;

    public boolean pdfCompare(PDFFile file1, PDFFile file2) {
        try {
            // get file name add create new 2 result file names
            // ex. old-oldFileName-newFileName.pdf
            // ex. new-oldFileName-newFileName.pdf
            String fileName1 = file1.getTargetFileName().split("[.]")[0];
            String fileName2 = file2.getTargetFileName();

            file1.setResultFileName("old-" + fileName1 + "-" + fileName2);
            file2.setResultFileName("new-" + fileName1 + "-" + fileName2);

            // find highlight position in word range form
            new PDFCompareText(file1, file2).findNotMatchPos();

            // you can share 2 PDFHighlighter.highlight() to each Thread
            new PDFHighlighter(file1).highlight(Setting.getOldDifColor());
            System.out.println("Created : " + file1.getResultPath());

            new PDFHighlighter(file2).highlight(Setting.getNewDifColor());
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

    public String getErrorMessage() {
        return errorMessage;
    }

}
