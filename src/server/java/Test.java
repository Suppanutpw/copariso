import javax.swing.*;
import java.awt.*;

public class Test {

    private JFrame frame;
    private JPanel panel1;
    private JLabel label;
    private JButton btn1, btn2;


    public Test(){
        frame = new JFrame("Compariso");
        frame.getContentPane().setForeground(Color.DARK_GRAY);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setSize(500,600);
        //main layout
        frame.setLayout(new BorderLayout());
        //set middle window
        frame.setLocationRelativeTo(null);


        label = new JLabel("Compariso");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Serif", Font.PLAIN, 30));
        panel1 = new JPanel();
        panel1.setBackground(Color.DARK_GRAY);
        panel1.setLayout(new GridBagLayout());
        panel1.add(label);
        frame.add(panel1, BorderLayout.NORTH);



        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new Test();
    }
}
