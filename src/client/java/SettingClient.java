import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class SettingClient {

    static {
        // ตั้งค่าของที่อยู่ไฟล์ผลลัพธ์
        // modify result path here!!!
        // default is ./resources if DB can't read
        SERVERIP = "localhost";
        SettingClient.setDefaultResultPath(Paths.get(Paths.get(".").toAbsolutePath().normalize().toString(), "resources").toString());
    }

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

        // read JSON form clientDB.json
        try (Reader reader = new FileReader("clientDB.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            // System.out.println(jsonObject.toJSONString());

            SERVERIP = (String) jsonObject.get("servIP");
            DEFAULT_RESULT_FILE_PATH = Paths.get((String) jsonObject.get("resultPath"));

            // fetch Compare History Array in JSONArray and then add to jsonObject
            JSONArray cmp = (JSONArray) jsonObject.get("cmp");
            for (int i = 0; i < cmp.size(); i++) {
                JSONObject current = (JSONObject) cmp.get(i);

                history.add(new CmpHistory(
                        (String) current.get("date"),
                        (String) current.get("old"),
                        (String) current.get("new"),
                        (String) current.get("txtold"),
                        (String) current.get("txtnew"),
                        (String) current.get("oallcmp")
                ));
            }
        } catch (Exception ex) {
            System.out.println("database file not found!");
        }
    }

    public static void writeDB() {
        JSONObject jsonObject = new JSONObject();

        // add static setting in object
        jsonObject.put("servIP", SERVERIP);
        jsonObject.put("resultPath", DEFAULT_RESULT_FILE_PATH.toString());

        // add Compare History Array in JSONArray and then add to dbObj
        JSONArray cmp = new JSONArray();
        history.forEach((history -> {
            JSONObject current = new JSONObject();

            current.put("date", history.getDate());
            current.put("old", history.getOldPath().toString());
            current.put("new", history.getNewPath().toString());
            current.put("txtold", history.getOldTextOnlyPath().toString());
            current.put("txtnew", history.getNewTextOnlyPath().toString());
            current.put("oallcmp", history.getOverallPath().toString());
            cmp.add(current);
        }));
        jsonObject.put("cmp", cmp);

        // write JSON to database file
        try (FileWriter file = new FileWriter("clientDB.json")) {
            // Constructs a FileWriter given a file name, using the platform's default charset
            file.write(jsonObject.toJSONString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
