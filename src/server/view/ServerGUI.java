import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame {
    private JPanel panel1;
    private JButton STARTButton;
    private JButton STOPButton;
    private JLabel server;
    private JLabel status;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JTextArea textArea1;

    public ServerGUI() {
        //setup frame
        setTitle("Compariso");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 300);
        setLocationRelativeTo(null); //set to middle screen

        textArea1.setEditable(false);

        //add action
        STARTButton.addActionListener(new ServerGUI.Start());
        STOPButton.addActionListener(new ServerGUI.Stop());

        //set status look
        status.setForeground(Color.RED);

        //setup panel
        panel2.setBackground(null);
        panel3.setBackground(null);
        panel4.setBackground(null);
        //main panel
        panel1.setBackground(Color.getHSBColor(26, 70, 91));
        add(panel1);

        //get log
        textArea1.setText(SettingServer.getLog());

        //final frame
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new ServerGUI();
    }

    public JTextArea getLogArea() {
        return textArea1;
    }

    private class Start implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CoparisoServer.startServer();

            //avoid spam running server, avoid port busy
            STARTButton.setEnabled(false);
            STOPButton.setEnabled(true);

            status.setText("Running");
            status.setForeground(Color.GREEN);
        }
    }

    private class Stop implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CoparisoServer.stopServer();

            STARTButton.setEnabled(true);
            STOPButton.setEnabled(false);

            status.setText("OFF");
            status.setForeground(Color.RED);
        }
    }
}
