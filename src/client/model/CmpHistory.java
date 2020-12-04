import java.nio.file.Path;
import java.nio.file.Paths;

public class CmpHistory {

    private Path oldPath;
    private Path newPath;
    private Path oldTextOnlyPath;
    private Path newTextOnlyPath;
    private Path overallPath;
    private String date;

    public CmpHistory(String date, String oldPath, String newPath, String oldTextOnlyPath, String newTextOnlyPath, String overallPath) {
        this.date = date;
        this.oldPath = Paths.get(oldPath);
        this.newPath = Paths.get(newPath);
        this.oldTextOnlyPath = Paths.get(oldTextOnlyPath);
        this.newTextOnlyPath = Paths.get(newTextOnlyPath);
        this.overallPath = Paths.get(overallPath);
    }

    public String getOldTextOnlyPath() {
        return oldTextOnlyPath.toString();
    }

    public String getNewTextOnlyPath() {
        return newTextOnlyPath.toString();
    }

    public String getOverallPath() {
        return overallPath.toString();
    }

    public String getOldPath() {
        return oldPath.toString();
    }

    public String getNewPath() {
        return newPath.toString();
    }

    public String getDate() {
        return date;
    }
}
