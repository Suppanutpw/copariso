import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PDFImagePanel extends JPanel {

    private Image image;
    private int width, height;

    public PDFImagePanel(Image image, int width, int height) {
        //set dimension
        this.image = image;
        this.width = width;
        this.height = height;

        setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        //create image
        graphics.drawImage(image, 0, 0, width, height, null);
    }

}