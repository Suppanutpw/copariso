public class Example extends Setting {

    // *** UI have to config this itself ***
    // DEFAULT COLOR OLD -> RED, NEW -> GREEN
    // ตรงนี้ UI ต้องทำฐานข้อมูลมา แล้วดึงมา config
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
        // โยน error มา อย่าโยนกลับน้า...เค้ากลัว... -_-
        // ตั้งค่าของที่อยู่ไฟล์ที่ต้องการเทียบ
        PDFFile file1 = new PDFFile("/Users/spw/Desktop/result/file2.pdf");
        PDFFile file2 = new PDFFile("/Users/spw/Desktop/result/file1.pdf");

        // เรียกไฟล์ไฟล์ 1 (เก่า) กับ ไฟล์ 2 (ใหม่) (อย่าใช้ && นะ!!!)
        if (!(file1.open() & file2.open())) {
            System.out.println("File Not Found");
            return; // ไม่เจอก็ออกฟังก์ชั่น
        }

        // ถ้าเป็นไฟล์เดียวกัน
        if (file1.getTargetPath().equals(file2.getTargetPath())) {
            System.out.println("You Selected In Same File");
            return; // ไม่เจอก็ออกฟังก์ชั่น
        }

        // เอา Thread ออก เพื่อหา error
        // in PDFTextOnlyCompare divide Job for highlight each file
        if (PDFTextOnlyCompare.pdfCompare(file1, file2) & PDFOverallCompare.pdfCompare(file1, file2)) {
            System.out.println("PDF Compare success");
        } else {
            System.out.println("PDF Compare Error");
        }
    }
}
