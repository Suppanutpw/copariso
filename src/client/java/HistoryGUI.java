import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class HistoryGUI extends JFrame implements WindowListener {
    private JPanel bg;
    private JScrollPane sp;
    private JTable t;
    private  final String head[] = {"Date", "Old Path", "New Path"};

    public HistoryGUI(){
        bg = new JPanel(new FlowLayout());
        this.add(bg);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.addWindowListener(this);
    }


    @Override
    public void windowOpened(WindowEvent e) {
        t = new JTable();
        sp = new JScrollPane();
        sp.add(t);
        bg.add(sp);

    }
//SettingClient.getHistory().get().
    @Override
    public void windowClosing(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
