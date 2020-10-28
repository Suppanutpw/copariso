import de.redsix.pdfcompare.PdfComparator;

public class PDFOverallCompare {

    public static void pdfCompare(PDFFile file1, PDFFile file2) throws RuntimeException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // use thread for share job with text-only compare (they're independent method)
                    new PdfComparator(file1.getTargetPath(), file2.getTargetPath()).compare().writeTo(Setting.getOverallPath());
                    System.out.println("Created : " + Setting.getOverallPath());
                    System.out.println("Inside : " + Thread.currentThread().getName());
                }catch (Exception ex) {
                    System.out.println("PDF overall compare error : \n" + ex.getMessage());
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }
}
