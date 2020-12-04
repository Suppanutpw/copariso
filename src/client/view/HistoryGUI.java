import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class HistoryGUI extends JFrame implements ListSelectionListener {
    
    private final String column[] = {"Date", "Old File", "New File"};
    private JTable t;
    private String[][] data;
    private JLabel l;
    private PDFViewer viewer;

    public HistoryGUI() {
        this.setLayout(new BorderLayout());
        this.setTitle("Compare History");

        data = createData(SettingClient.getHistory());
        l = new JLabel("Compare History");

        t = new JTable(data, column);
        t.setBounds(30, 40, 200, 300);
        t.setDefaultEditor(Object.class, null);
        t.getSelectionModel().addListSelectionListener(this);

        this.add(l, BorderLayout.NORTH);
        this.add(t);

        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(500, 150);
        this.setVisible(true);
    }

    private String[][] createData(ArrayList<CmpHistory> history) {
        String[][] a = new String[history.size()][3];
        for (int i = 0; i < history.size(); i++) {
            a[i][0] = history.get(i).getDate();
            a[i][1] = history.get(i).getNewPath();
            a[i][2] = history.get(i).getOldPath();
        }
        return a;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            // show compare result history
            this.viewer = new PDFViewer(
                    SettingClient.getHistory().get(t.getSelectedRow()).getOldTextOnlyPath(),
                    SettingClient.getHistory().get(t.getSelectedRow()).getNewTextOnlyPath(),
                    SettingClient.getHistory().get(t.getSelectedRow()).getOverallPath()
            );
            this.viewer.showResult();
        }
    }
}
