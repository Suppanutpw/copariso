import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SocketServer {

    public final static int SOCKET_PORT = 13267;  // you may change this
    private static Socket sock;
    private static Path calPath;
    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static String errorMessage;

    public static void main(String[] args) throws IOException {
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

        try (ServerSocket servsock = new ServerSocket(SOCKET_PORT)) {
            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                    dos = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

                    System.out.println("Accepted connection : " + sock);

                    // get date now for set unique file name
                    LocalDateTime dateTime = LocalDateTime.now();
                    String dateNow = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                    String olderFilePath = Paths.get(calPath.toString(), "older-" + dateNow + ".pdf").toString();
                    String newerFilePath = Paths.get(calPath.toString(), "newer-" + dateNow + ".pdf").toString();

                    // receive 2 file from client that keep in ArrayList (you can use files to continue calculate)
                    ArrayList<File> files = new ArrayList<File>(2);
                    files.add(new File(olderFilePath));
                    files.add(new File(newerFilePath));

                    // receive file files[0] is older and file[1] is newer
                    if (new FileTransfer(sock).receiveFile(files, dis, true)) {
                        System.out.println("Socket Server Receive File success");
                    } else {
                        System.out.println("Socket Server Receive File Fail");
                    }

                    PDFFile file1 = new PDFFile(olderFilePath);
                    PDFFile file2 = new PDFFile(newerFilePath);
                    if (!(file1.open() & file2.open())) {
                        System.out.println("File Not Found");
                        // System.out.println(file1.getErrorMessage());
                        // System.out.println(file2.getErrorMessage());
                        return;
                    }

                    file1.setResultFileName("text-only-old-" + dateNow + ".pdf");
                    file2.setResultFileName("text-only-new-" + dateNow + ".pdf");

                    // เอา Thread ออก เพื่อหา error (อย่าใช้ && นะ!!!) ต้องคำนวณทุกตัว
                    // in PDFTextOnlyCompare divide Job for highlight each file
                    PDFTextOnlyCompare textOnlyCmp = new PDFTextOnlyCompare();
                    PDFOverallCompare overallCmp = new PDFOverallCompare();

                    overallCmp.setOverallFileName("overall-" + dateNow);

                    if (overallCmp.pdfCompare(file1, file2) & textOnlyCmp.pdfCompare(file1, file2)) {
                        System.out.println("PDF Compare success");
                    } else {
                        System.out.println("PDF Compare Error");
                        // System.out.println(PDFTextOnlyCompare.getErrorMessage());
                        // System.out.println(PDFOverallCompare.getErrorMessage());
                        return;
                    }

                    System.out.println(overallCmp.getOverallPathPDF());

                    files = new ArrayList<File>(3);
                    files.add(new File(file1.getResultPath()));
                    files.add(new File(file2.getResultPath()));
                    files.add(new File(overallCmp.getOverallPathPDF()));

                    // send file
                    if (new FileTransfer(sock).sendFile(files, dos)) {
                        System.out.println("Socket Server Send File success");
                    } else {
                        System.out.println("Socket Server Send File Fail");
                    }

                    new File(file1.getResultPath()).delete();
                    new File(file2.getResultPath()).delete();
                    new File(overallCmp.getOverallPathPDF()).delete();

                    dis.close();
                    dos.close();
                    sock.close();
                } finally {
                    if (sock != null) sock.close();
                }
            }
        }
    }
}
