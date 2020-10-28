import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFFile {
    private Path targetPath;
    private Path resultPath;
    private String fileText;
    private ArrayList<PDFHighlightPos> highlightPos;

    public PDFFile(String targetPath) throws IOException {
        // set result file name
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss");

        // set selected file path
        this.targetPath = Paths.get(targetPath);

        // if you want to change result file name as date remove comment
        this.resultPath = Paths.get(Setting.getDefaultResultPath(), /* myDateObj.format(myFormatObj) */ "result-" + getTargetFileName());

        // set highlightPos
        highlightPos = new ArrayList<PDFHighlightPos>();
        // get text from PDFReadText
        fileText = new PDFReadText(targetPath).toText();
    }

    // if you want to change selected file
    public void setTargetPath(String targetPath) {
        this.targetPath = Paths.get(targetPath);
    }

    public String getTargetPath() {
        return targetPath.toString();
    }

    public String getTargetFileName() {
        return targetPath.getFileName().toString();
    }

    public String getResultPath() { return resultPath.toString(); }

    public String getResultFileName() { return resultPath.getFileName().toString(); }

    public String getFileText() {
        return fileText;
    }

    public ArrayList<PDFHighlightPos> getHighlightPos() {
        return highlightPos;
    }

    public void setHighlightPos(ArrayList<PDFHighlightPos> highlightPos) {
        this.highlightPos = highlightPos;
    }

    public int textLength() {
        return fileText.length();
    }
}