import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PDFServer {

    public final static int SOCKET_PORT = 13267;  // you may change this

    public static void main (String [] args ) throws IOException {
        ServerSocket servsock = null;
        Socket sock = null;

        try {
            servsock = new ServerSocket(SOCKET_PORT);
            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    DataInputStream dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                    DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
                    System.out.println("Accepted connection : " + sock);

                    // receive file
                    ArrayList<File> files = new ArrayList<File>(2);
                    files.add(new File ("/Users/spw/Desktop/result/test1.pdf"));
                    files.add(new File ("/Users/spw/Desktop/result/test2.pdf"));
                    new FileTransfer(sock).receiveFile(files, dis);

                    files = new ArrayList<File>(2);
                    files.add(new File ("/Users/spw/Desktop/result/file3.pdf"));
                    files.add(new File ("/Users/spw/Desktop/result/file4.pdf"));
                    new FileTransfer(sock).sendFile(files, dos);

                    dis.close();
                    dos.close();
                }
                finally {
                    if (sock != null) sock.close();
                }
            }
        }
        finally {
            if (servsock != null) servsock.close();
        }

        // static dir for server side calculate
        /* Path path = Paths.get(Paths.get(".").toAbsolutePath().normalize().toString(), "resources");
        System.out.println(path.toString());
        if (!Files.exists(path)) {
            try {
                System.out.println("dir doesn't exists re-crate: " + path.toString());
                Files.createDirectories(path);
            } catch (IOException ex) {
                System.out.println("target dir not found : " + ex.getMessage());
                return;
            }
        } */
    }
}
