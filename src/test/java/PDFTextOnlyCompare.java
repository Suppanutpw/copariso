public class PDFTextOnlyCompare {

    public static void pdfCompare(PDFFile file1, PDFFile file2) throws Exception, RuntimeException {
        new PDFCompareText(file1, file2).findNotMatchPos();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // you can't share 2 PDFHighlighter.highlight() to each Thread because of there have static attribute
                    PDFHighlighter.highlight(file1, Setting.getTextOldHighlightColor());
                    PDFHighlighter.highlight(file2, Setting.getTextNewHighlightColor());
                    System.out.println("Created : " + file1.getResultPath());
                    System.out.println("Created : " + file2.getResultPath());
                    System.out.println("Inside : " + Thread.currentThread().getName());
                }catch (Exception ex) {
                    System.out.println("PDF text only compare error : " + ex.getMessage());
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }

}
