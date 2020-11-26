import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class IpGUI extends JFrame implements ActionListener {

    private JTextField tf;
    private JButton b;
    private Label l;

    public IpGUI() {
        tf = new JTextField(SettingClient.getSERVERIP(), 20);

        b = new JButton("Submit");
        b.addActionListener(this);

        l = new Label("Set Host IP");
        this.setLayout(new BorderLayout());

        this.add(l, BorderLayout.NORTH);
        this.add(tf, BorderLayout.CENTER);
        this.add(b, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    public static boolean isValidInet4Address(String ip) {
        String[] groups = ip.split("\\.");

        if (groups.length != 4)
            return false;

        try {
            return Arrays.stream(groups)
                    .filter(s -> s.length() >= 1)
                    .map(Integer::parseInt)
                    .filter(i -> (i >= 0 && i <= 255))
                    .count() == 4;
        } catch (NumberFormatException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(b)) {
            if (isValidInet4Address(tf.getText())) {
                SettingClient.setSERVERIP(tf.getText());
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "The ip address isn't valid", "Error Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
