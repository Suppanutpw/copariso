import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Example extends Setting {

    // *** UI have to config this itself ***
    // DEFAULT COLOR OLD -> RED, NEW -> GREEN
    // PDFOverallCompare แก้สีไม่ได้!!!
    // ตรงนี้ UI ต้องทำฐานข้อมูลมา database แล้วดึงมา config
    static {
        // ตั้งค่าของที่อยู่ไฟล์ผลลัพธ์
        // modify result path here!!!
        Setting.setDefaultResultPath("/Users/spw/Desktop/result");
        // ตั้งค่าสีไฮไลท์ของไฟล์เก่า/ใหม่
        Setting.setTextOldHighlightColor(1, 0, 0);
        Setting.setTextNewHighlightColor(0, 1, 0);
    }

    // ตัวอย่างเวลาเรียกใช้
    public static void main(String[] args) {
        // เปลี่ยนจาก throw error เป็นเงื่อนไขให้แทน
        // ใน class PDFFile, PDFTextOnlyCompare, PDFOverallCompare สามารถเรียก getErrorMessage() ได้
        // ตั้งค่าของที่อยู่ไฟล์ที่ต้องการเทียบ


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

        // สามารถใช้ thread ได้ กรณีมีหลายงานพร้อมกัน (socket)
        Runnable compare = new PDFCompare(
                new PDFFile("/Users/spw/Desktop/result/file1.pdf"),
                new PDFFile("/Users/spw/Desktop/result/file2.pdf"));
        new Thread(compare).start();

        compare = new PDFCompare(
                new PDFFile("/Users/spw/Desktop/result/file3.pdf"),
                new PDFFile("/Users/spw/Desktop/result/file4.pdf"));
        new Thread(compare).start();
    }
}
