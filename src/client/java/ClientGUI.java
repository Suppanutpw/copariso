import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame {
    private JFrame fr;
    private JPanel background, top, bottom;
    private JTextField pdf1, pdf2, result;
    private JButton compare;

    // เปลี่ยนใหม่หมดได้ (ที่เราทำแค่ทดสอบ)
    public ClientGUI() {
        fr = new JFrame("Compariso");
        background = new JPanel();
        background.setLayout(new BorderLayout());
        pdf1 = new JTextField();
        pdf2 = new JTextField();
        result = new JTextField();
        top = new JPanel();
        top.setLayout(new GridLayout(1,3));
        bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        compare = new JButton("Compare");

        top.add(pdf1);
        top.add(pdf2);
        top.add(result);
        bottom.add(compare);
        background.add(top, BorderLayout.CENTER);
        background.add(bottom, BorderLayout.SOUTH);
        fr.add(background);

        fr.setSize(1024, 700);
        fr.setDefaultCloseOperation(EXIT_ON_CLOSE);
        fr.setVisible(true);
    }

    private class Compare implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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

            // เอาที่อยู่ไฟล์พวกนี้เก็บใน database ได้ สำหรับเรียกใช้ในประวัติ
            System.out.println("Create: " + CoparisoClient.getOldTextOnlyFilePath());
            System.out.println("Create: " + CoparisoClient.getNewTextOnlyFilePath());
            System.out.println("Create: " + CoparisoClient.getOverallFilePath());
        }
    }
}
