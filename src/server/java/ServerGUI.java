import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame{
    private JPanel panel1;
    private JButton STARTButton;
    private JButton STOPButton;
    private JTextArea line;
    private JLabel server;
    private JLabel status;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;

    public ServerGUI(){
        JTextArea line = new JTextArea();
        setTitle("Compariso");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 300);

        STARTButton.addActionListener(new ServerGUI.Start());
        STOPButton.addActionListener(new ServerGUI.Stop());

        status.setForeground(Color.RED);

        panel2.setBackground(null);
        panel3.setBackground(null);
        panel4.setBackground(null);

        panel1.setBackground(Color.getHSBColor(26,70,91));
        add(panel1);

        setVisible(true);
    }

    public static void main(String[] args){
        new ServerGUI();
    }

    public void setLine(String newText) {
        line.append(newText);
    }


    private class Start implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            status.setText("Running");
            status.setForeground(Color.GREEN);
            CoparisoServer.startServer();
        }
    }

    private class Stop implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            status.setText("OFF");
            status.setForeground(Color.RED);
            CoparisoServer.stopServer();
        }
    }

}
