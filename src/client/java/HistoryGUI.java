import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HistoryGUI extends JFrame {
    private JTable t;
    private  final String column[] = {"Date", "Old File", "New File"};
    private String[][] data;
    private JLabel l;

    public HistoryGUI(){
        this.setLayout(new BorderLayout());
        this.setTitle("Compare History");
        data = createData(SettingClient.getHistory());
        t = new JTable(data, column);
        t.setBounds(30,40,200,300);
        t.setDefaultEditor(Object.class, null);
        l = new JLabel("Compare History");
        this.add(l, BorderLayout.NORTH);
        this.add(t);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500,150);
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
