import java.io.*;
import java.net.Socket;

public class FileTransfer {
    private Socket socket;
    private InputStream is;
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private int bufferSize;
    public static int count = 0;

    FileTransfer(Socket socket) {
        this.socket = socket;
        is = null;
        fos = null;
        bos = null;
        bufferSize = 0;
    }

    public boolean receiveFile(String fileName) {
        try {
            is = socket.getInputStream();
            bufferSize = socket.getReceiveBufferSize();
            System.out.println("Buffer size: " + bufferSize);
            fos = new FileOutputStream(fileName);
            bos = new BufferedOutputStream(fos);
            byte[] bytes = new byte[bufferSize];
            int count;
            while ((count = is.read(bytes)) >= 0) {
                bos.write(bytes, 0, count);
            }
            FileTransfer.count++;
            bos.close();
            is.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean sendFile(File file) {
        FileInputStream fis;
        BufferedInputStream bis;
        BufferedOutputStream out;
        byte[] buffer = new byte[8192];
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            out = new BufferedOutputStream(socket.getOutputStream());
            int count;
            while ((count = bis.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            fis.close();
            bis.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}