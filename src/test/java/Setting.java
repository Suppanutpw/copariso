import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Setting {

    // Setting is the class for config process via GUI
    // DEFAULT_RESULT_PATH => path for save result file ex. /Users/mac/Desktop/
    private static Path DEFAULT_RESULT_FILE_PATH;
    private static String DEFAULT_OVERALL_FILE_NAME;
    private static PDColor TEXT_OLD_HIGHLIGHT_COLOR;
    private static PDColor TEXT_NEW_HIGHLIGHT_COLOR;

    public static String getDefaultResultPath() {
        return DEFAULT_RESULT_FILE_PATH.toString();
    }

    public static void setDefaultResultPath(String defaultResultPath) {
        DEFAULT_RESULT_FILE_PATH = Paths.get(defaultResultPath);
    }

    public static String getDefaultOverallFileName() {
        return DEFAULT_OVERALL_FILE_NAME;
    }

    public static void setDefaultOverallFileName(String defaultOverallFileName) {
        DEFAULT_OVERALL_FILE_NAME = defaultOverallFileName;
    }

    // input color intensity is in range 0-1
    public static PDColor getTextOldHighlightColor() {
        return TEXT_OLD_HIGHLIGHT_COLOR;
    }

    public static void setTextOldHighlightColor(float red, float green, float blue) {
        TEXT_OLD_HIGHLIGHT_COLOR = new PDColor(new float[] { red, green, blue }, PDDeviceRGB.INSTANCE);
    }

    public static PDColor getTextNewHighlightColor() {
        return TEXT_NEW_HIGHLIGHT_COLOR;
    }

    public static void setTextNewHighlightColor(float red, float green, float blue) {
        TEXT_NEW_HIGHLIGHT_COLOR = new PDColor(new float[] { red, green, blue }, PDDeviceRGB.INSTANCE);
    }

    public static String getOverallPath() {
        return Paths.get(DEFAULT_RESULT_FILE_PATH.toString(), DEFAULT_OVERALL_FILE_NAME).toString();
    }
}
