import javax.swing.*;

public class Test2 extends JFrame{
    private JPanel panel1;
    private JButton button1;
    private JButton button2;

    public Test2(){
        setTitle("Compariso");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 300);

        add(panel1);

        setVisible(true);
    }

    public static void main(String[] args){
        new Test2();
    }
}
