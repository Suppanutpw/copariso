import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import java.nio.file.Path;
import java.nio.file.Paths;


public class SettingClient {

    static {
        // ตั้งค่าของที่อยู่ไฟล์ผลลัพธ์
        // modify result path here!!!
        // default now is ./resources
        SettingClient.setDefaultResultPath(Paths.get(Paths.get(".").toAbsolutePath().normalize().toString(), "resources").toString());
    }

    // Setting is the class for config process via GUI
    // DEFAULT_RESULT_PATH => path for save result file ex. /Users/mac/Desktop/
    private static Path DEFAULT_RESULT_FILE_PATH;
    private static String SERVERIP;
    private static final String OS = System.getProperty("os.name").toLowerCase();

    // setter & getter for saved difference file path
    public static String getDefaultResultPath() {
        return DEFAULT_RESULT_FILE_PATH.toString();
    }

    public static void setDefaultResultPath(String defaultResultPath) {
        DEFAULT_RESULT_FILE_PATH = Paths.get(defaultResultPath);
    }

    // setter & getter for socket server dest
    public static String getSERVERIP() {
        return SERVERIP;
    }

    public static void setSERVERIP(String SERVERIP) {
        SettingClient.SERVERIP = SERVERIP;
    }

    public static String getOS() {
        return OS;
    }
}
