import java.nio.file.Path;
import java.nio.file.Paths;

public class CmpHistory {
    private Path oldPath;
    private Path newPath;
    private Path oldTextOnlyPath;
    private Path newTextOnlyPath;
    private Path overallPath;

    public CmpHistory() {
        this("", "", "");
    }

    public CmpHistory(String oldTextOnlyPath, String newTextOnlyPath, String overallPath) {
        this.oldTextOnlyPath = Paths.get(oldTextOnlyPath);
        this.newTextOnlyPath = Paths.get(newTextOnlyPath);
        this.overallPath = Paths.get(overallPath);
    }

    public CmpHistory(Path oldTextOnlyPath, Path newTextOnlyPath, Path overallPath) {
        this.oldTextOnlyPath = oldTextOnlyPath;
        this.newTextOnlyPath = newTextOnlyPath;
        this.overallPath = overallPath;
    }

    public Path getOldTextOnlyPath() {
        return oldTextOnlyPath;
    }

    public void setOldTextOnlyPath(Path oldTextOnlyPath) {
        this.oldTextOnlyPath = oldTextOnlyPath;
    }

    public Path getNewTextOnlyPath() {
        return newTextOnlyPath;
    }

    public void setNewTextOnlyPath(Path newTextOnlyPath) {
        this.newTextOnlyPath = newTextOnlyPath;
    }

    public Path getOverallPath() {
        return overallPath;
    }

    public void setOverallPath(Path overallPath) {
        this.overallPath = overallPath;
    }

    public Path getOldPath() {
        return oldPath;
    }

    public void setOldPath(Path oldPath) {
        this.oldPath = oldPath;
    }

    public Path getNewPath() {
        return newPath;
    }

    public void setNewPath(Path newPath) {
        this.newPath = newPath;
    }
}
