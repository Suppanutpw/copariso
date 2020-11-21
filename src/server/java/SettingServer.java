import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingServer {

    static {
        // ตั้งค่าของที่อยู่ไฟล์ผลลัพธ์
        // modify result path here!!!
        // default now is ./resources
        SettingServer.setDefaultResultPath(Paths.get(Paths.get(".").toAbsolutePath().normalize().toString(), "resources").toString());
        // ตั้งค่าสีไฮไลท์ของไฟล์เก่า/ใหม่
        SettingServer.setTextOldHighlightColor(255, 0, 0);
        SettingServer.setTextNewHighlightColor(0, 255, 0);
    }

    // Setting is the class for config process via GUI
    // DEFAULT_RESULT_PATH => path for save result file ex. /Users/mac/Desktop/
    private static Path DEFAULT_RESULT_FILE_PATH;
    private static PDColor OLD_DIF_COLOR;
    private static PDColor NEW_DIF_COLOR;
    private static final String OS = System.getProperty("os.name").toLowerCase();

    // setter & getter for saved difference file path
    public static String getDefaultResultPath() {
        return DEFAULT_RESULT_FILE_PATH.toString();
    }

    public static void setDefaultResultPath(String defaultResultPath) {
        DEFAULT_RESULT_FILE_PATH = Paths.get(defaultResultPath);
    }

    // setter & getter for difference color
    // input color intensity is in range 0-1
    public static PDColor getOldDifColor() {
        return OLD_DIF_COLOR;
    }

    public static PDColor getNewDifColor() {
        return NEW_DIF_COLOR;
    }

    public static void setTextOldHighlightColor(float red, float green, float blue) {
        OLD_DIF_COLOR = new PDColor(new float[]{red, green, blue}, PDDeviceRGB.INSTANCE);
    }

    public static void setTextNewHighlightColor(float red, float green, float blue) {
        NEW_DIF_COLOR = new PDColor(new float[]{red, green, blue}, PDDeviceRGB.INSTANCE);
    }

    public static String getOS() {
        return OS;
    }
}
