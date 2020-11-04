import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClient {

    public final static int SOCKET_PORT = 13267;      // you may change this
    public final static String SERVER = "localhost";
    // overwrite the one used by server...

    public static void main(String[] args) throws IOException {
        Socket sock = null;

        try {
            sock = new Socket(SERVER, SOCKET_PORT);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

            System.out.println("Connecting...");

            // send file to server
            ArrayList<File> files = new ArrayList<File>(2);
            files.add(new File("/Users/spw/Desktop/result/file1.pdf"));
            files.add(new File("/Users/spw/Desktop/result/file5.pdf"));
            if (new FileTransfer(sock).sendFile(files, dos)) {
                System.out.println("Socket Client Send File success");
            } else {
                System.out.println("Socket Client Send File Fail");
            }

            // send file from server
            files = new ArrayList<File>(3);
            files.add(new File("/Users/spw/Desktop/result/test3.pdf"));
            files.add(new File("/Users/spw/Desktop/result/test4.pdf"));
            files.add(new File("/Users/spw/Desktop/result/test5.pdf"));
            if (new FileTransfer(sock).receiveFile(files, dis, false)) {
                System.out.println("Socket Server Receive File success");
            } else {
                System.out.println("Socket Server Receive File Fail");
            }

            dis.close();
            dos.close();

        } catch (Exception ex) {
            System.out.println("Connection Error");
        } finally {
            if (sock != null) sock.close();
        }
    }

}
