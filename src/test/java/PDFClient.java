import java.io.*;
import java.net.Socket;

public class PDFClient {

    public final static int SOCKET_PORT = 13267;      // you may change this
    public final static String SERVER = "localhost";
    // overwrite the one used by server...

    public static void main (String [] args) throws IOException {
        Socket sock = null;
        try {
            sock = new Socket(SERVER, SOCKET_PORT);
            System.out.println("Connecting...");

            // send file to server
            File pdfFile1 = new File ("/Users/spw/Desktop/result/file1.pdf");
            if ( new FileTransfer(sock).sendFile(pdfFile1)) {
                System.out.println("PDF Server Send File success");
            }else {
                System.out.println("PDF Server Send File Fail");
            }
        } catch (Exception ex) {
            System.out.println("Connection Error");
        } finally {
            if (sock != null) sock.close();
        }
    }

}
