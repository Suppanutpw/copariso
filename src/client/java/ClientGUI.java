import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame {
    private JButton btn;
    private JTextArea file1, file2, result;
    private JPanel filePanel;

    // เปลี่ยนใหม่หมดได้ (ที่เราทำแค่ทดสอบ)
    public ClientGUI() {
        btn = new JButton("Compare!");
        btn.addActionListener(new Compare());

        filePanel = new JPanel(new GridLayout(3, 1));

        file1 = new JTextArea("/Users/spw/Desktop/result/file1.pdf");
        file2 = new JTextArea("/Users/spw/Desktop/result/file2.pdf");
        result = new JTextArea("/Users/spw/Desktop/result");

        filePanel.add(file1);
        filePanel.add(file2);
        filePanel.add(result);

        this.add(filePanel);
        this.add(btn, BorderLayout.SOUTH);

        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private class Compare implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // ต้องรับจาก ip ของ server user มาก่อน (จากตั้งค่าก็ได้ไปเพิ่มใน SettingClient)
            new CoparisoClient("127.0.0.1");

            // check ว่า file มีตัวตนอยู่ไหมด้วย และต้องเป็น pdf อย่างเดียวนะเออ
            // if (...)

            // อาจจะตั้งค่าให้ user save ไว่ที่ไหนก็ว่าไป {ที่อยู่ของไฟล์ผลลัพธ์ที่ต้องการจะ save}
            SettingClient.setDefaultResultPath(result.getText());

            // set file เฉยๆ ยังไม่เชื่อม
            CoparisoClient.compare(file1.getText(), file2.getText());

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
