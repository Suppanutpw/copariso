import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class FileTransfer {
    private Socket socket;
    private FileOutputStream fos;
    private int bufferSize;
    public static int count = 0;

    FileTransfer(Socket socket) {
        this.socket = socket;
        fos = null;
        bufferSize = 0;
    }

    public boolean receiveFile(ArrayList<File> files, DataInputStream dis) {
        try {
            // get all content length
            bufferSize = socket.getReceiveBufferSize();
            byte[] bytes = new byte[bufferSize];
            int count; // count byte for read one file

            for (File file : files) {
                // read file size
                long fileSize = dis.readLong();
                // create new file from File Array List
                fos = new FileOutputStream(file);
                System.out.println("Receive File : " + file);
                // read file until a file size length or found end-of-stream condition (EOS)
                while (fileSize > 0 && (count = dis.read(bytes, 0, (int)Math.min(bytes.length, fileSize))) != -1) {
                    fos.write(bytes, 0, count);
                    fileSize -= count;
                }
                fos.close();
            }
            FileTransfer.count++;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean sendFile(ArrayList<File> files, DataOutputStream dos) {
        FileInputStream fis;
        byte[] buffer = new byte[8192];
        try {
            int count; // count byte for send one file

            for (File file : files) {
                // get send file
                fis = new FileInputStream(file);
                // send file size
                dos.writeLong(file.length());
                System.out.println("Sent File : " + file);
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                    dos.flush();
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}