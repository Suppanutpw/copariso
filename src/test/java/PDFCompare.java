public class PDFCompare implements Runnable {

    private PDFFile file1, file2;
    public PDFCompare(PDFFile file1, PDFFile file2) {
        this.file1 = file1;
        this.file2 = file2;
    }

    @Override
    public void run() {
        // เปลี่ยนจาก throw error เป็นเงื่อนไขให้แทน
        // ใน class PDFFile, PDFTextOnlyCompare, PDFOverallCompare สามารถเรียก getErrorMessage() ได้
        // ตั้งค่าของที่อยู่ไฟล์ที่ต้องการเทียบ

        // เรียกไฟล์ไฟล์ 1 (เก่า) กับ ไฟล์ 2 (ใหม่) (อย่าใช้ && นะ!!!) ต้องคำนวณทุกตัว
        if (!(file1.open() & file2.open())) {
            System.out.println("File Not Found");
            // System.out.println(file1.getErrorMessage());
            // System.out.println(file2.getErrorMessage());
            return; // ไม่เจอก็ออกฟังก์ชั่น
        }

        // ถ้าเป็นไฟล์เดียวกัน
        if (file1.getTargetPath().equals(file2.getTargetPath())) {
            System.out.println("You Selected In Same File");
            return; // ไม่เจอก็ออกฟังก์ชั่น
        }

        // เอา Thread ออก เพื่อหา error (อย่าใช้ && นะ!!!) ต้องคำนวณทุกตัว
        // in PDFTextOnlyCompare divide Job for highlight each file
        if (new PDFTextOnlyCompare().pdfCompare(file1, file2) & new PDFOverallCompare().pdfCompare(file1, file2)) {
            System.out.println("PDF Compare success");
        } else {
            System.out.println("PDF Compare Error");
            // System.out.println(PDFTextOnlyCompare.getErrorMessage());
            // System.out.println(PDFOverallCompare.getErrorMessage());
        }
    }

}
