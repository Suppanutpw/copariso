import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame {
    private JButton start, stop;

    // เปลี่ยนใหม่หมดได้ (ที่เราทำแค่ทดสอบ)
    public ServerGUI() {
        start = new JButton("Start");
        start.addActionListener(new Start());

        stop = new JButton("Stop");
        stop.addActionListener(new Stop());

        this.add(start, BorderLayout.SOUTH);
        this.add(stop);

        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private class Start implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CoparisoServer.startServer();
        }
    }

    private class Stop implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CoparisoServer.stopServer();
        }
    }
}
