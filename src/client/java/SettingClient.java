import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class SettingClient {

    // Setting is the class for config process via GUI
    // DEFAULT_RESULT_PATH => path for save result file ex. /Users/mac/Desktop/
    private static Path DEFAULT_RESULT_FILE_PATH;
    private static String SERVERIP;
    private static ArrayList<CmpHistory> history;

    // for check OS case
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

    public static ArrayList<CmpHistory> getHistory() {
        return history;
    }

    public static void readDB() {

        history = new ArrayList();
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader("clientDB.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            String SERVERIP = (String) jsonObject.get("servIP");

            System.out.println(SERVERIP);

            DEFAULT_RESULT_FILE_PATH = Paths.get((String) jsonObject.get("resultPath"));
            System.out.println(DEFAULT_RESULT_FILE_PATH.toAbsolutePath());

            JSONArray cmp = (JSONArray) jsonObject.get("cmp");

            for (int i = 0; i < cmp.size(); i++) {
                JSONObject current = (JSONObject) cmp.get(i);

                System.out.println((String) current.get("txtold"));
                System.out.println((String) current.get("txtnew"));
                System.out.println((String) current.get("oallcmp"));
            }


        } catch (Exception ex) {
            SERVERIP = "localhost";
            DEFAULT_RESULT_FILE_PATH = Paths.get(Paths.get(".").toAbsolutePath().normalize().toString(), "resources");

            System.out.println("Can't load database!");
            ex.printStackTrace();
        }
    }
}
