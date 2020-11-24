import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ipGUI extends JFrame implements ActionListener {
    private JTextField tf;
    private JButton b;
    private Label l;

    public ipGUI(){
        tf = new JTextField();
        b = new JButton("Submit");
        b.addActionListener(this);
        l = new Label("Enter ip");
        this.setLayout(new BorderLayout());

        this.add(l, BorderLayout.NORTH);
        this.add(tf, BorderLayout.CENTER);
        this.add(b, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(b)){
            SettingClient.setSERVERIP(tf.getText());
            this.dispose();
        }
    }
}
