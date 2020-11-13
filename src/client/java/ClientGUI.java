import PDF_viewer.PdfViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ClientGUI extends JFrame implements ActionListener{
    private JPanel background, top, bottom;
    private JTextField pdf1, pdf2, result;
    private JButton compare;

    // เปลี่ยนใหม่หมดได้ (ที่เราทำแค่ทดสอบ)
    public ClientGUI() {
        this.setTitle("Compariso");
        background = new JPanel(new BorderLayout());

        pdf1 = new JTextField();
        pdf2 = new JTextField();
        result = new JTextField();

        top = new JPanel(new GridLayout(1,3));
        bottom = new JPanel(new FlowLayout());

        compare = new JButton("Compare");
        compare.addActionListener(this);

        top.add(pdf1);
        top.add(pdf2);
        top.add(result);

        bottom.add(compare);

        background.add(top, BorderLayout.CENTER);
        background.add(bottom, BorderLayout.SOUTH);

        this.add(background);

        this.setSize(1024, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(compare)){
            // ต้องรับจาก ip ของ server user มาก่อน (จากตั้งค่าก็ได้ไปเพิ่มใน SettingClient)
            new CoparisoClient(SettingClient.getSERVERIP());
            // check ว่า file มีตัวตนอยู่ไหมด้วย และต้องเป็น pdf อย่างเดียวนะเออ
            // if (...)
            // อาจจะตั้งค่าให้ user save ไว่ที่ไหนก็ว่าไป {ที่อยู่ของไฟล์ผลลัพธ์ที่ต้องการจะ save}
            SettingClient.setDefaultResultPath(result.getText());

            // set file เฉยๆ ยังไม่เชื่อม
            CoparisoClient.compare(pdf1.getText(), pdf2.getText());

            // เริ่มการเชื่อมต่อ และส่งไฟล์ให้ server ไปเทียบและรับกลับมา
            if (CoparisoClient.connect()) {
                System.out.println("Compare Success!");
            }else {
                System.out.println(CoparisoClient.getErrorMessage());
                return;
            }
            
            // เพิ่ม text-only compare
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        /* แสดง text-only compare */
                        new PdfViewer(new File(CoparisoClient.getOldTextOnlyFilePath()), new File(CoparisoClient.getNewTextOnlyFilePath()));

                    } catch (Exception e) {
                        // ทำเป็น popup error ว่าแสดง pdf error
                        e.printStackTrace();
                    }
                }
            });

            // เอาที่อยู่ไฟล์พวกนี้เก็บใน database ได้ สำหรับเรียกใช้ในประวัติ
            System.out.println("Create: " + CoparisoClient.getOldTextOnlyFilePath());
            System.out.println("Create: " + CoparisoClient.getNewTextOnlyFilePath());
            System.out.println("Create: " + CoparisoClient.getOverallFilePath());
        }
    }
}
