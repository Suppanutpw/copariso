import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SettingServer {

    private static final String OS = System.getProperty("os.name").toLowerCase();
    // Setting is the class for config process via GUI
    // DEFAULT_RESULT_PATH => path for save result file ex. /Users/mac/Desktop/
    private static Path DEFAULT_RESULT_FILE_PATH;
    private static PDColor OLD_DIF_COLOR;
    private static PDColor NEW_DIF_COLOR;
    private static String log = "================================================== open copariso server ==================================================\n";

    static {
        // ตั้งค่าของที่อยู่ไฟล์ผลลัพธ์
        // modify result path here!!!
        // default now is ./resources
        SettingServer.setDefaultResultPath(Paths.get(Paths.get(".").toAbsolutePath().normalize().toString(), "resources").toString());
        // ตั้งค่าสีไฮไลท์ของไฟล์เก่า/ใหม่
        SettingServer.setTextOldHighlightColor(255, 0, 0);
        SettingServer.setTextNewHighlightColor(0, 255, 0);
    }

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

    public static String getLog() {
        return log;
    }

    public static void addLog(String log) {
        // add Date for check error log message
        LocalDateTime dateTime = LocalDateTime.now();
        String dateNow = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        SettingServer.log += dateNow + " - " + log + "\n";
        System.out.println(dateNow + " - " + log);
    }

    public static void setReadLog(ServerGUI gui) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    gui.getLogArea().setText(log);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        addLog("log reader Interrupted : " + ex.getMessage());
                    }
                }
            }
        }).start();
    }

    public static void writeLog() {
        // write Server log file
        String fileLog = "";
        try (Reader reader = new FileReader("serverLog.log")) {
            int ch = 0;
            while ((ch = reader.read()) != -1) {
                fileLog += (char) ch;
            }
        } catch (IOException ex) {
            addLog("log doesn't exists re-crate : " + ex.getMessage());
        }

        try (FileWriter writeFile = new FileWriter("serverLog.log")) {
            // Constructs a FileWriter given a file name, using the platform's default charset
            if (!fileLog.equals("null")) {
                log = fileLog + log;
            }
            writeFile.write(log);
        } catch (IOException ex) {
            System.out.println("write serverLog error : " + ex.getMessage());
        }
    }
}
