import java.io.IOException;

public class Example extends Setting {

    // *** UI have to config this itself ***
    // DEFAULT COLOR OLD -> RED, NEW -> GREEN
    // ตรงนี้ UI ต้องทำฐานข้อมูลมา แล้วดึงมา config
    static {
        // ตั้งค่าของที่อยู่ไฟล์ผลลัพธ์
        // modify result path here!!!
        Setting.setDefaultResultPath("/Users/spw/Desktop/result");
        // ขื่อไฟล์ overall compare
        Setting.setDefaultOverallFileName("overall.pdf");
        // ตั้งค่าสีไฮไลท์ของไฟล์เก่า/ใหม่
        Setting.setTextOldHighlightColor(1, 0, 0);
        Setting.setTextNewHighlightColor(0, 1, 0);
    }

    // ตัวอย่างเวลาเรียกใช้
    public static void main(String[] args) {
        // โยน error มา อย่าโยนกลับน้า...เค้ากลัว... -_-
        try {
            // ตั้งค่าของที่อยู่ไฟล์ที่ต้องการเทียบ
            PDFFile file1 = new PDFFile(
                    "/Users/spw/Desktop/result/file1.pdf"
            );
            PDFFile file2 = new PDFFile(
                    "/Users/spw/Desktop/result/file2.pdf"
            );

            // use Thread to 2 compare in same Job
            // in PDFTextOnlyCompare divide Job for highlight each file
            PDFOverallCompare.pdfCompare(file1, file2);
            PDFTextOnlyCompare.pdfCompare(file1, file2);
        }catch (IOException | RuntimeException ex) {
            // แสดง GUI popup ว่า
            System.out.println("PDFFile not found : \n" + ex.getMessage());
        }catch (Exception ex) {
            // แสดง GUI popup ว่า
            System.out.println("PDFFile Compare error : \n" + ex.getMessage());
        }
    }
}
