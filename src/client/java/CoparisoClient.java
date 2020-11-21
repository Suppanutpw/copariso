import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CoparisoClient {

    public final static int SOCKET_PORT = 13426;      // you may change this
    public static String serverIp;
    public static String errorMessage;
    public static Path olderFilePath, newerFilePath;
    public static String oldTextOnlyFileName, newTextOnlyFileName2, overallFileName;
    private static ClientGUI view;
    // overwrite the one used by server...

    public CoparisoClient(String serverIp) {
        this.serverIp = serverIp;
    }

    public static void main(String[] args) {
        view = new ClientGUI();
        init();
    }

    private static void init() {
        SettingClient.readDB();
    }

    public static boolean connect() {
        Socket sock = null;

        try {
            // send file to server
            ArrayList<File> files = new ArrayList<File>(2);
            files.add(new File(olderFilePath.toString()));
            files.add(new File(newerFilePath.toString()));

            // if there have no file there
            if (!(files.get(0).exists() && files.get(1).exists())) {
                errorMessage = "File Not Found!";
                return false;
            }

            sock = new Socket(serverIp, SOCKET_PORT);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

            System.out.println("Connecting...");

            if (new FileTransfer(sock).sendFile(files, dos)) {
                System.out.println("Socket Client Send Compare Request To Server...");
            } else {
                errorMessage = "Socket Client Can't Send File To Server";
            }

            // send file from server
            files = new ArrayList<File>(3);
            files.add(new File(getOldTextOnlyFilePath()));
            files.add(new File(getNewTextOnlyFilePath()));
            files.add(new File(getOverallFilePath()));
            if (new FileTransfer(sock).receiveFile(files, dis, false)) {
                System.out.println("Compare Success!");
            } else {
                errorMessage = "Socket Client Can't Receive File From Server";
            }

            dis.close();
            dos.close();
            errorMessage = null;

        } catch (IOException ex) {
            errorMessage = "Connection Error";
        } finally {
            try {
                if (sock != null) sock.close();
            } catch (IOException ex) {
                errorMessage = "Can't Close Socket Connection";
            }
            return errorMessage == null;
        }
    }

    public static void compare(Path olderFilePath, Path newerFilePath) {
        CoparisoClient.olderFilePath = olderFilePath;
        CoparisoClient.newerFilePath = newerFilePath;

        // get file name only
        String oldName = olderFilePath.getFileName().toString().split("[.]")[0];
        String newName = newerFilePath.getFileName().toString().split("[.]")[0];

        // get date now for set unique file name
        LocalDateTime dateTime = LocalDateTime.now();
        String dateNow = dateTime.format(DateTimeFormatter.ofPattern("_dd-MM-yyyy_HH-mm-ss_"));

        oldTextOnlyFileName = "older_" + oldName + dateNow + newName + ".pdf";
        newTextOnlyFileName2 = "newer_" + oldName + dateNow + newName + ".pdf";
        overallFileName = "overall_" + oldName + dateNow + newName + ".pdf";
    }
    public static void compare(String olderFilePath, String newerFilePath) {
        compare(Paths.get(olderFilePath), Paths.get(newerFilePath));
    }

    public static String getErrorMessage() {
        return errorMessage;
    }

    public static String getOldTextOnlyFilePath() {
        return Paths.get(SettingClient.getDefaultResultPath(), oldTextOnlyFileName).toString();
    }

    public static String getNewTextOnlyFilePath() {
        return Paths.get(SettingClient.getDefaultResultPath(), newTextOnlyFileName2).toString();
    }

    public static String getOverallFilePath() {
        return Paths.get(SettingClient.getDefaultResultPath(), overallFileName).toString();
    }
}
