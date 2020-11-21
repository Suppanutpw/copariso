import java.nio.file.Path;
import java.nio.file.Paths;

public class CmpHistory {
    private Path oldPath;
    private Path newPath;
    private Path oldTextOnlyPath;
    private Path newTextOnlyPath;
    private Path overallPath;
    private String date;

    public CmpHistory() {
        this("", "", "", "", "", "");
    }

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

    public void setOldTextOnlyPath(Path oldTextOnlyPath) {
        this.oldTextOnlyPath = oldTextOnlyPath;
    }

    public String getNewTextOnlyPath() {
        return newTextOnlyPath.toString();
    }

    public void setNewTextOnlyPath(Path newTextOnlyPath) {
        this.newTextOnlyPath = newTextOnlyPath;
    }

    public String getOverallPath() {
        return overallPath.toString();
    }

    public void setOverallPath(Path overallPath) {
        this.overallPath = overallPath;
    }

    public String getOldPath() {
        return oldPath.toString();
    }

    public void setOldPath(Path oldPath) {
        this.oldPath = oldPath;
    }

    public String getNewPath() {
        return newPath.toString();
    }

    public void setNewPath(Path newPath) {
        this.newPath = newPath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
