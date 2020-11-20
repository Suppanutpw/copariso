import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test {

    private JFrame frame;
    private JPanel panel1, panel2, blank, panel3, main;
    private JLabel label, status, server;
    private JButton btn1, btn2;


    public Test(){
        frame = new JFrame("Compariso");
        frame.getContentPane().setForeground(Color.DARK_GRAY);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setSize(500,300);
        //main layout
        frame.setLayout(new BorderLayout());
        //set middle window
        frame.setLocationRelativeTo(null);

        //top
        label = new JLabel("Compariso");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Serif", Font.PLAIN, 50));
        panel1 = new JPanel();
        panel1.setBackground(null);
        panel1.setLayout(new GridBagLayout());
        panel1.add(label);
        frame.add(panel1, BorderLayout.NORTH);

        //center
        panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        panel2.setBackground(Color.DARK_GRAY);

        blank = new JPanel();
        blank.setBackground(null);

        btn1 = new JButton("Start");
        btn1.setFont(new Font("Serif", Font.PLAIN, 30));
        btn1.setForeground(Color.WHITE);
        btn1.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        btn1.setBackground(Color.DARK_GRAY);
        btn1.addActionListener(new Test.Start());

        btn2 = new JButton("Stop");
        btn2.setFont(new Font("Serif", Font.PLAIN, 30));
        btn2.setForeground(Color.WHITE);
        btn2.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        btn2.setBackground(Color.DARK_GRAY);
        btn2.addActionListener(new Test.Stop());

        panel2.add(btn1);
        panel2.add(blank);
        panel2.add(btn2);

        //frame.add(panel2, BorderLayout.CENTER);

        //server status
        status = new JLabel("Server Status : ", SwingConstants.CENTER);
        server = new JLabel("OFF");
        status.setForeground(Color.WHITE);
        server.setForeground(Color.RED);
        status.setFont(new Font("Serif", Font.PLAIN, 30));
        server.setFont(new Font("Serif", Font.PLAIN, 30));
        panel3 = new JPanel();
        panel3.setLayout(new GridLayout(0,2));
        panel3.setBackground(null);
        panel3.add(status);
        panel3.add(server);

        main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBackground(null);
        main.add(panel2, BorderLayout.CENTER);
        main.add(panel3, BorderLayout.SOUTH);

        frame.add(main, BorderLayout.CENTER);


        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);



    }
    private class Start implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            server.setText("Running");
            server.setForeground(Color.GREEN);
            CoparisoServer.startServer();
        }
    }

    private class Stop implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            server.setText("OFF");
            server.setForeground(Color.RED);
            CoparisoServer.stopServer();
        }
    }

    public static void main(String[] args){
        new Test();
    }
}



