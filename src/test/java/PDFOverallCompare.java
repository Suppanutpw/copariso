import de.redsix.pdfcompare.PdfComparator;

import java.nio.file.Paths;

public class PDFOverallCompare {

    private static String errorMessage;
    private static String overallFileName;

    public static boolean pdfCompare(PDFFile file1, PDFFile file2) {
        try {
            // get file name add create new result file name
            // ex. overall-oldFileName-newFileName.pdf
            String fileName1 = file1.getTargetFileName().split("[.]")[0];
            String fileName2 = file2.getTargetFileName();

            overallFileName = "overall-" + fileName1 + "-" + fileName2;

            // use thread for share job with text-only compare (they're independent method)
            new PdfComparator(file1.getTargetPath(), file2.getTargetPath()).compare().writeTo(getOverallPath());
            System.out.println("Created : " + getOverallPath());

            // if there no error here so return null
            errorMessage = null;
        } catch (Exception ex) {
            // if there have error you can get cause message
            errorMessage = ex.getMessage();
        }

        // if there have no error message then send true
        return errorMessage == null;
    }

    // setter & getter for overall file attribute
    public static String getOverallFileName() {
        return overallFileName;
    }

    public static void setOverallFileName(String overallFileName) {
        PDFOverallCompare.overallFileName = overallFileName;
    }

    public static String getOverallPath() {
        return Paths.get(Setting.getDefaultResultPath(), overallFileName).toString();
    }

    public static String getErrorMessage() {
        return errorMessage;
    }
}
