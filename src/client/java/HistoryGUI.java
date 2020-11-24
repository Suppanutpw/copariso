import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class HistoryGUI extends JFrame {
    private JTable t;
    private  final String column[] = {"Date", "Old Path", "New Path"};
    private String[][] data;

    public HistoryGUI(){
        this.setLayout(new BorderLayout());
        data = createData(SettingClient.getHistory());
        t = new JTable(data, column);
        t.setBounds(30,40,200,300);
        this.add(t);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private String[][] createData(ArrayList<CmpHistory> history){
        String[][] a = new String[history.size()][3];
        for (int i = 0; i < history.size(); i++) {
            a[i][0] = history.get(i).getDate();
            a[i][1] = history.get(i).getNewPath();
            a[i][2] = history.get(i).getOldPath();
        }
        return a;
    }


//SettingClient.getHistory().get().
}
