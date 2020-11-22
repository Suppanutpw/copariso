import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CoparisoServer {

    public final static int SOCKET_PORT = 13426;  // you may change this
    private static Socket sock;
    private static ServerSocket servsock;
    private static Path calPath;
    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static Thread serverThread;
    private static boolean isRunning;

    private static Test view;

    public static void main(String[] args) {
        // call GUI here
        view = new Test();
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SettingServer.writeLog();
            }
        });
    }

    public static void connect() {
        // static dir for server side calculate Path
        calPath = Paths.get(SettingServer.getDefaultResultPath());
        if (!Files.exists(calPath)) {
            try {
                SettingServer.addLog("dir doesn't exists re-crate: " + calPath.toString());
                Files.createDirectories(calPath);
            } catch (IOException ex) {
                SettingServer.addLog("target dir not found : " + ex.getMessage());
                return;
            }
        }

        try {
            servsock = new ServerSocket(SOCKET_PORT);

            while (isRunning) {
                SettingServer.addLog("Started Waiting Client Connect...");
                try {
                    sock = servsock.accept();
                    sock.setSoTimeout(10000);

                    dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                    dos = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

                    SettingServer.addLog("Accepted " + sock.getLocalAddress() + " connection : " + sock);

                    // get date now for set unique file name
                    LocalDateTime dateTime = LocalDateTime.now();
                    String dateNow = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));
                    String olderFilePath = Paths.get(calPath.toString(), "older_" + dateNow + ".pdf").toString();
                    String newerFilePath = Paths.get(calPath.toString(), "newer_" + dateNow + ".pdf").toString();

                    // receive 2 file from client that keep in ArrayList (you can use files to continue calculate)
                    ArrayList<File> files = new ArrayList<File>(2);
                    files.add(new File(olderFilePath));
                    files.add(new File(newerFilePath));

                    // receive file files[0] is older and file[1] is newer
                    if (new FileTransfer(sock).receiveFile(files, dis, true)) {
                        SettingServer.addLog("Socket Server Received File from Client");
                    } else {
                        SettingServer.addLog("Socket Server Can't Receive File From Client");
                    }

                    PDFFile file1 = new PDFFile(olderFilePath);
                    PDFFile file2 = new PDFFile(newerFilePath);
                    if (!(file1.open() & file2.open())) {
                        SettingServer.addLog("Client File Can't Open :");
                        SettingServer.addLog(file1.getErrorMessage());
                        SettingServer.addLog(file2.getErrorMessage());
                    }

                    file1.setResultFileName("text-only-old_" + dateNow + ".pdf");
                    file2.setResultFileName("text-only-new_" + dateNow + ".pdf");

                    // เอา Thread ออก เพื่อหา error (อย่าใช้ && นะ!!!) ต้องคำนวณทุกตัว
                    PDFTextOnlyCompare textOnlyCmp = new PDFTextOnlyCompare();
                    PDFOverallCompare overallCmp = new PDFOverallCompare();

                    // file name have no .pdf cause of PdfComparator
                    overallCmp.setOverallFileName("overall_" + dateNow);

                    // compare two file
                    if (overallCmp.pdfCompare(file1, file2) & textOnlyCmp.pdfCompare(file1, file2)) {
                        SettingServer.addLog("Copariso Server PDF Compare success");
                    } else {
                        SettingServer.addLog("Copariso Server PDF Compare Error:");
                        SettingServer.addLog(textOnlyCmp.getErrorMessage());
                        SettingServer.addLog(overallCmp.getErrorMessage());
                        return;
                    }

                    File textOnly1 = new File(file1.getResultPath());
                    File textOnly2 = new File(file2.getResultPath());
                    File overall = new File(overallCmp.getOverallPathPDF());

                    textOnly1.deleteOnExit();
                    textOnly2.deleteOnExit();
                    overall.deleteOnExit();

                    files = new ArrayList<File>(3);
                    files.add(textOnly1);
                    files.add(textOnly2);
                    files.add(overall);

                    // send file
                    if (new FileTransfer(sock).sendFile(files, dos)) {
                        SettingServer.addLog("Socket Server Send File success");
                    } else {
                        SettingServer.addLog("Socket Server Can't Send File to Client");
                    }

                    dis.close();
                    dos.close();
                } catch (Exception ex) {
                    SettingServer.addLog("Terminate Connection!");
                } finally {
                    if (sock != null) sock.close();
                }
            }
        } catch (IOException ex) {
            SettingServer.addLog("Copariso Server Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(view, "server connection failed", "Error Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static boolean getStatus() {
        return isRunning;
    }

    public static void startServer() {
        isRunning = true;
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        });
        serverThread.start();
    }
    public static void stopServer() {
        CoparisoServer.isRunning = false;
        try {
            try {
                if (servsock != null) servsock.close();
            } catch (IOException ex) {
                SettingServer.addLog("Terminate Connection Error : " + ex.getMessage());
            }
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            SettingServer.addLog("Terminate Connection Error : " + ex.getMessage());
        }
    }
}
