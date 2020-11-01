import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PDFServer {

    public final static int SOCKET_PORT = 13267;  // you may change this

    public static void main (String [] args ) throws IOException {
        ServerSocket servsock = null;
        Socket sock = null;

        // static dir for server side calculate
        Path path = Paths.get(Paths.get(".").toAbsolutePath().normalize().toString(), "resources");
        System.out.println(path.toString());
        if (!Files.exists(path)) {
            try {
                System.out.println("dir doesn't exists re-crate: " + path.toString());
                Files.createDirectories(path);
            } catch (IOException ex) {
                System.out.println("target dir not found : " + ex.getMessage());
                return;
            }
        }

        try {
            servsock = new ServerSocket(SOCKET_PORT);
            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);

                    // receive file
                    new FileTransfer(sock).receiveFile("/Users/spw/Desktop/result/test1.pdf");
                }
                finally {
                    if (sock != null) sock.close();
                }
            }
        }
        finally {
            if (servsock != null) servsock.close();
        }
    }
}
