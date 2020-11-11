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

    public static void main(String[] args) {
        // call GUI here
        new ServerGUI();
    }

    public static void connect() {
        // static dir for server side calculate Path
        calPath = Paths.get(SettingClient.getDefaultResultPath());
        if (!Files.exists(calPath)) {
            try {
                System.out.println("dir doesn't exists re-crate: " + calPath.toString());
                Files.createDirectories(calPath);
            } catch (IOException ex) {
                System.out.println("target dir not found : " + ex.getMessage());
                return;
            }
        }

        // ++++ put new GUT here ++++
        // new ServerGUI();

        try {
            servsock = new ServerSocket(SOCKET_PORT);

            while (isRunning) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    sock.setSoTimeout(10000);
                    System.out.println(sock.getLocalAddress());
                    dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                    dos = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

                    System.out.println("Accepted connection : " + sock);

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
                        System.out.println("Socket Serve Receive File success");
                    } else {
                        System.out.println("Socket Server Can't Receive File From Client");
                    }

                    PDFFile file1 = new PDFFile(olderFilePath);
                    PDFFile file2 = new PDFFile(newerFilePath);
                    if (!(file1.open() & file2.open())) {
                        System.out.println("File Not Found:");
                        System.out.println(file1.getErrorMessage());
                        System.out.println(file2.getErrorMessage());
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
                        System.out.println("PDF Compare success.");
                    } else {
                        System.out.println("PDF Compare Error:");
                        System.out.println(textOnlyCmp.getErrorMessage());
                        System.out.println(overallCmp.getErrorMessage());
                        return;
                    }

                    File textOnly1 = new File(file1.getResultPath());
                    File textOnly2 = new File(file2.getResultPath());
                    File overall = new File(overallCmp.getOverallPathPDF());

                    files = new ArrayList<File>(3);
                    files.add(textOnly1);
                    files.add(textOnly2);
                    files.add(overall);

                    // send file
                    if (new FileTransfer(sock).sendFile(files, dos)) {
                        System.out.println("Socket Server Send File success");
                    } else {
                        System.out.println("Socket Server Can't Send File to Client");
                    }

                    textOnly1.deleteOnExit();
                    textOnly2.deleteOnExit();
                    overall.deleteOnExit();

                    dis.close();
                    dos.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    System.out.println("Copariso Server Terminate!");
                    if (sock != null) sock.close();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static boolean getStatus() {
        return isRunning;
    }

    public static void startServer() {
        CoparisoServer.isRunning = true;
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
                System.out.println(ex.getMessage());
            }
            serverThread.sleep(100);
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
