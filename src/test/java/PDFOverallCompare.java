import de.redsix.pdfcompare.PdfComparator;

import java.nio.file.Paths;

public class PDFOverallCompare {

    private String errorMessage;
    private String overallFileName;

    public boolean pdfCompare(PDFFile file1, PDFFile file2) {
        try {
            // get file name add create new result file name
            // ex. overall-oldFileName-newFileName.pdf
            String fileName1 = file1.getTargetFileName().split("[.]")[0];
            String fileName2 = file2.getTargetFileName();

            overallFileName = "overall-" + fileName1 + "-" + fileName2;
            System.out.println(overallFileName);

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
    public String getOverallFileName() {
        return overallFileName;
    }

    public void setOverallFileName(String overallFileName) {
        this.overallFileName = overallFileName;
    }

    public String getOverallPath() {
        return Paths.get(Setting.getDefaultResultPath(), overallFileName).toString();
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
