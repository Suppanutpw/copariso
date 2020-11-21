import PDF_viewer.PdfViewer;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.FileChooserUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ClientGUI extends JFrame implements ActionListener{
    private JPanel background, top, bottom, topbtn1, topbtn2, topbtn3;
    private JButton compare,pdf1, pdf2, result;
    private JLabel txt,pathPdf1, pathPdf2, pathResult;
    private JMenuBar bar;
    private JMenu setting, history;

    public ClientGUI() {
        this.setTitle("Compariso");
        background = new JPanel(new BorderLayout());
        txt = new JLabel("Compariso", SwingConstants.CENTER);
        topbtn1 = new JPanel(new FlowLayout());
        topbtn2 = new JPanel(new FlowLayout());
        topbtn3 = new JPanel(new FlowLayout());
        txt.setFont(new Font("Courier", Font.BOLD,50));
        pathPdf1 = new JLabel("No file selected", SwingConstants.CENTER);
        pathPdf2 = new JLabel("No file selected", SwingConstants.CENTER);
        pathResult = new JLabel("No directory selected", SwingConstants.CENTER);
        pdf1 = new JButton("Open");
        pdf2 = new JButton("Open");
        result = new JButton("Open");
        pdf1.addActionListener(this);
        pdf2.addActionListener(this);
        result.addActionListener(this);
        top = new JPanel(new GridLayout(2,3));
        bottom = new JPanel(new FlowLayout());
        compare = new JButton("Compare");
        compare.addActionListener(this);
        bar = new JMenuBar();
        setting = new JMenu("Setting");
        history = new JMenu("History");

        //add component
        top.add(pathPdf1);
        top.add(pathPdf2);
        top.add(pathResult);
        topbtn1.add(pdf1);
        topbtn2.add(pdf2);
        topbtn3.add(result);
        top.add(topbtn1);
        top.add(topbtn2);
        top.add(topbtn3);
        bottom.add(compare);
        background.add(txt, BorderLayout.NORTH);
        background.add(top, BorderLayout.CENTER);
        background.add(bottom, BorderLayout.SOUTH);
        bar.add(setting);
        bar.add(history);
        topbtn1.setBackground(Color.getHSBColor(26,70,91));
        topbtn2.setBackground(Color.getHSBColor(26,70,91));
        topbtn3.setBackground(Color.getHSBColor(26,70,91));
        background.setBackground(Color.getHSBColor(26,70,91));
        top.setBackground(Color.getHSBColor(26,70,91));
        bottom.setBackground(Color.getHSBColor(26,70,91));

        this.setJMenuBar(bar);
        this.add(background);
        this.setSize(800,250);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(pdf1)){
            JFileChooser fileChooser = new JFileChooser ();
            fileChooser.setDialogTitle("Choose you PDF");
            int selectedButton = fileChooser.showDialog ( null, "Open" );
            if ( selectedButton == JFileChooser.APPROVE_OPTION ){
                File selectedFile = fileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                if (path.toLowerCase().endsWith(".pdf") && (path.charAt(path.length() - "pdf".length() - 1)) == '.'){
                    pathPdf1.setText(path);
                }
                else {
                    pathPdf1.setText("PDFs only!111!!!1");
                }

            }
        }
        else if (e.getSource().equals(pdf2)){
            JFileChooser fileChooser = new JFileChooser ();
            fileChooser.setDialogTitle("Choose you PDF");
            int selectedButton = fileChooser.showDialog ( null, "Open" );
            if ( selectedButton == JFileChooser.APPROVE_OPTION ){
                File selectedFile = fileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                if (path.toLowerCase().endsWith(".pdf") && (path.charAt(path.length() - "pdf".length() - 1)) == '.'){
                    pathPdf2.setText(path);
                }
                else {
                    pathPdf2.setText("PDFs only!111!!!1");
                }
            }
        }
        else if (e.getSource().equals(result)){
            JFileChooser fileChooser = new JFileChooser (FileSystemView.getFileSystemView().getHomeDirectory());
            fileChooser.setDialogTitle("Choose you folder for saving");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int selectedButton = fileChooser.showDialog ( null, "Open" );
            if ( selectedButton == JFileChooser.APPROVE_OPTION ){
                if (SettingClient.getOS().indexOf("win") >= 0) {
                    // if client os is window
                    pathResult.setText(fileChooser.getSelectedFile().getPath());
                }else {
                    // if client os is mac
                    pathResult.setText(fileChooser.getSelectedFile().getParent());
                }
            }
        }
        else if (e.getSource().equals(compare)){
            // ต้องรับจาก ip ของ server user มาก่อน (จากตั้งค่าก็ได้ไปเพิ่มใน SettingClient)
            new CoparisoClient(SettingClient.getSERVERIP());
            // check ว่า file มีตัวตนอยู่ไหมด้วย และต้องเป็น pdf อย่างเดียวนะเออ
            // if (...)
            // อาจจะตั้งค่าให้ user save ไว่ที่ไหนก็ว่าไป {ที่อยู่ของไฟล์ผลลัพธ์ที่ต้องการจะ save}
            SettingClient.setDefaultResultPath(pathResult.getText());

            // set file เฉยๆ ยังไม่เชื่อม
            CoparisoClient.compare(pathPdf1.getText(), pathPdf2.getText());

            // เริ่มการเชื่อมต่อ และส่งไฟล์ให้ server ไปเทียบและรับกลับมา
            if (CoparisoClient.connect()) {
                System.out.println("Compare Success!");
            }else {
                return;
            }
            
            // เพิ่ม text-only compare
            /* EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        // แสดง text-only compare
                        new PdfViewer(new File(CoparisoClient.getOldTextOnlyFilePath()), new File(CoparisoClient.getNewTextOnlyFilePath()));
                    } catch (Exception e) {
                        // ทำเป็น popup error ว่าแสดง pdf error
                        e.printStackTrace();
                    }
                }
            }); */

            // เอาที่อยู่ไฟล์พวกนี้เก็บใน database ได้ สำหรับเรียกใช้ในประวัติ
            System.out.println("Create: " + CoparisoClient.getOldTextOnlyFilePath());
            System.out.println("Create: " + CoparisoClient.getNewTextOnlyFilePath());
            System.out.println("Create: " + CoparisoClient.getOverallFilePath());
        }
    }
}
