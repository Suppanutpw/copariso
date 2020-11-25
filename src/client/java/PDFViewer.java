import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PDFViewer {

    private static String oldTextPath, newTextPath, overallPath;
    private JFrame frame;
    private PDFRenderer renderer, renderer2;
    private JPanel panelSelectedPage;
    private int numberOfPages, numberOfPages2;
    private int currentPageIndex = 0, currentPageIndex2 = 0;
    private int width;
    private int height;
    private JTextField txtPageNumber, txtPageNumber2;
    private JButton btnNextPage, btnNextPage2, btnTCompare;
    private JButton btnPreviousPage, btnPreviousPage2, btnOCompare;

    public PDFViewer(File document, File doc2) throws Exception {
        initialize(document, doc2);
    }

    public PDFViewer(String oldTextPath, String newTextPath, String overallPath) {
        PDFViewer.oldTextPath = oldTextPath;
        PDFViewer.newTextPath = newTextPath;
        PDFViewer.overallPath = overallPath;
    }

    //////////////////////////////////////////////////////////
    public PDFViewer(File document) throws Exception {
        initialize(document);
    }

    public void showResult() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    /* Edit path here!!!  รับ Path ตั้งต้นตรงนี้  */
                    new PDFViewer(new File(oldTextPath), new File(newTextPath));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "result file not found", "Error Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private void selectPage(int pageIndex, int pageIndex2) {
        BufferedImage renderImage = null;
        BufferedImage renderImage2 = null;

        try {
            renderImage = renderer.renderImage(pageIndex, 1);
            renderImage2 = renderer2.renderImage(pageIndex2, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        panelSelectedPage.removeAll(); // Remove children


        PDFImagePanel imagePanel = new PDFImagePanel(renderImage, width / 2, height);
        imagePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        imagePanel.setLayout(new CardLayout(0, 0));
        imagePanel.setPreferredSize(new Dimension(width / 2, height));

        PDFImagePanel imagePanel2 = new PDFImagePanel(renderImage2, width / 2, height);
        imagePanel2.setBorder(new EmptyBorder(0, 0, 0, 0));
        imagePanel2.setLayout(new CardLayout(0, 0));
        imagePanel2.setPreferredSize(new Dimension(width / 2, height));

        panelSelectedPage.add(imagePanel, BorderLayout.WEST);
        panelSelectedPage.add(imagePanel2, BorderLayout.EAST);

        currentPageIndex = pageIndex;
        currentPageIndex2 = pageIndex2;

        String pageText = String.format("Page: %d / %d", pageIndex + 1, numberOfPages);
        txtPageNumber.setText(pageText);

        String pageText2 = String.format("Page: %d / %d", pageIndex2 + 1, numberOfPages2);
        txtPageNumber2.setText(pageText2);


        panelSelectedPage.revalidate();
        panelSelectedPage.repaint();
    }

    private void initialize(File file, File file2) throws Exception {

        PDDocument doc = PDDocument.load(file);
        PDDocument doc2 = PDDocument.load(file2);

        // Getting/calculating screen dimensions...
        Float realWidth = new Float(doc.getPage(0).getMediaBox().getWidth());
        Float realHeight = new Float(doc.getPage(0).getMediaBox().getHeight());
        Float realWidth2 = new Float(doc2.getPage(0).getMediaBox().getWidth());
        Float realHeight2 = new Float(doc2.getPage(0).getMediaBox().getHeight());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Double ratio = 0.8;

        height = (int) (screenSize.getHeight() * ratio);
        width = (int) ((height * (realWidth + realWidth2) / realHeight));


        numberOfPages = doc.getNumberOfPages();
        numberOfPages2 = doc2.getNumberOfPages();
        renderer = new PDFRenderer(doc);
        renderer2 = new PDFRenderer(doc2);

        System.out.println("Old file = " + numberOfPages + " pages");
        System.out.println("New file = " + numberOfPages2 + " pages");
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle(file.getName() + " Compare with " + file2.getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelControls = new JPanel();
        frame.getContentPane().add(panelControls, BorderLayout.SOUTH);
        panelControls.setLayout(new BorderLayout(0, 0));

        Component verticalStrutTop = Box.createVerticalStrut(10);
        panelControls.add(verticalStrutTop, BorderLayout.NORTH);

        Box horizontalBoxControls = Box.createHorizontalBox();
        panelControls.add(horizontalBoxControls);

        Component horizontalStrutLeft = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutLeft);

        Component horizontalStrutLeft_1 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutLeft_1);

        btnPreviousPage = new JButton("Previous Page");
        btnPreviousPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPageIndex > 0) {
                    selectPage(currentPageIndex - 1, currentPageIndex2);
                }
            }
        });
        horizontalBoxControls.add(btnPreviousPage);

        Component horizontalStrutLeft_2 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutLeft_2);

        txtPageNumber = new JTextField();
        horizontalBoxControls.add(txtPageNumber);
        txtPageNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPageNumber.setEditable(false);
        txtPageNumber.setPreferredSize(new Dimension(50, txtPageNumber.getPreferredSize().width));
        txtPageNumber.setColumns(10);

        Component horizontalStrutRight_2 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutRight_2);

        btnNextPage = new JButton("Next Page");
        btnNextPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPageIndex < (numberOfPages - 1)) {
                    selectPage(currentPageIndex + 1, currentPageIndex2);
                }
            }
        });
        horizontalBoxControls.add(btnNextPage);

        Component horizontalStrutLeft_3 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutLeft_3);

        btnOCompare = new JButton("Overall Compare");
        btnOCompare.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                panelSelectedPage.removeAll();
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                try {
                    /* แก้ Path Overall Compare ตรงนี้ !!!!! */
                    new PDFViewer(new File(overallPath));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                panelSelectedPage.revalidate();
                panelSelectedPage.repaint();
            }
        });
        horizontalBoxControls.add(btnOCompare);
        Component horizontalStrutLeft_8 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutLeft_8);

        btnPreviousPage2 = new JButton("Previous Page");
        btnPreviousPage2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPageIndex2 > 0) {
                    selectPage(currentPageIndex, currentPageIndex2 - 1);
                }
            }
        });
        horizontalBoxControls.add(btnPreviousPage2);

        Component horizontalStrutLeft_4 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutLeft_4);

        txtPageNumber2 = new JTextField();
        horizontalBoxControls.add(txtPageNumber2);
        txtPageNumber2.setHorizontalAlignment(SwingConstants.CENTER);
        txtPageNumber2.setEditable(false);
        txtPageNumber2.setPreferredSize(new Dimension(50, txtPageNumber2.getPreferredSize().width));
        txtPageNumber2.setColumns(10);

        Component horizontalStrutRight_4 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutRight_4);

        btnNextPage2 = new JButton("Next Page");
        btnNextPage2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPageIndex2 < (numberOfPages2 - 1)) {
                    selectPage(currentPageIndex, currentPageIndex2 + 1);
                }
            }
        });
        horizontalBoxControls.add(btnNextPage2);

        Component horizontalStrutRight_1 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutRight_1);

        Component verticalStrutBottom = Box.createVerticalStrut(10);
        panelControls.add(verticalStrutBottom, BorderLayout.SOUTH);

        Box verticalBoxView = Box.createVerticalBox();
        frame.getContentPane().add(verticalBoxView, BorderLayout.WEST);

        Component verticalStrutView = Box.createVerticalStrut(10);
        verticalBoxView.add(verticalStrutView);

        Box horizontalBoxView = Box.createHorizontalBox();
        verticalBoxView.add(horizontalBoxView);

        Component horizontalStrutViewLeft = Box.createHorizontalStrut(10);
        horizontalBoxView.add(horizontalStrutViewLeft);

        panelSelectedPage = new JPanel();
        panelSelectedPage.setBackground(Color.LIGHT_GRAY);
        horizontalBoxView.add(panelSelectedPage);
        panelSelectedPage.setPreferredSize(new Dimension(width, height));
        panelSelectedPage.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelSelectedPage.setLayout(new BorderLayout(0, 0));


        Component horizontalStrutViewRight = Box.createHorizontalStrut(10);
        horizontalBoxView.add(horizontalStrutViewRight);

        selectPage(0, 0);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    doc.close();
                    doc2.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.repaint();
    }

    private void selectPage(int pageIndex) {
        BufferedImage renderImage = null;

        try {
            renderImage = renderer.renderImage(pageIndex, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        panelSelectedPage.removeAll(); // Remove children

        PDFImagePanel imagePanel = new PDFImagePanel(renderImage, width, height);
        imagePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        imagePanel.setLayout(new CardLayout(0, 0));
        imagePanel.setPreferredSize(new Dimension(width, height));
        panelSelectedPage.add(imagePanel, BorderLayout.CENTER);
        currentPageIndex = pageIndex;

        String pageText = String.format("Page: %d / %d", pageIndex + 1, numberOfPages);
        txtPageNumber.setText(pageText);


        panelSelectedPage.revalidate();
        panelSelectedPage.repaint();

    }

    private void initialize(File file) throws Exception {

        PDDocument doc = PDDocument.load(file);

        // Getting/calculating screen dimensions...
        Float realWidth = new Float(doc.getPage(0).getMediaBox().getWidth());
        Float realHeight = new Float(doc.getPage(0).getMediaBox().getHeight());


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Double ratio = 0.8;

        height = (int) (screenSize.getHeight() * ratio);
        width = (int) ((height * realWidth) / realHeight);

        numberOfPages = doc.getNumberOfPages();

        renderer = new PDFRenderer(doc);

        System.out.println("Number of pages = " + numberOfPages);

        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Overall Compare");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelControls = new JPanel();
        frame.getContentPane().add(panelControls, BorderLayout.SOUTH);
        panelControls.setLayout(new BorderLayout(0, 0));

        Component verticalStrutTop = Box.createVerticalStrut(10);
        panelControls.add(verticalStrutTop, BorderLayout.NORTH);

        Box horizontalBoxControls = Box.createHorizontalBox();
        panelControls.add(horizontalBoxControls);

        Component horizontalStrutLeft = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutLeft);

        Component horizontalStrutLeft_1 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutLeft_1);

        btnPreviousPage = new JButton("Previous Page");
        btnPreviousPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPageIndex > 0) {
                    selectPage(currentPageIndex - 1);
                }
            }
        });
        horizontalBoxControls.add(btnPreviousPage);

        Component horizontalStrutLeft_2 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutLeft_2);

        txtPageNumber = new JTextField();
        horizontalBoxControls.add(txtPageNumber);
        txtPageNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPageNumber.setEditable(false);
        txtPageNumber.setPreferredSize(new Dimension(50, txtPageNumber.getPreferredSize().width));
        txtPageNumber.setColumns(10);

        Component horizontalStrutRight_2 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutRight_2);

        btnNextPage = new JButton("Next Page");
        btnNextPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPageIndex < (numberOfPages - 1)) {
                    selectPage(currentPageIndex + 1);
                }
            }
        });
        horizontalBoxControls.add(btnNextPage);

        Component horizontalStrutRight_1 = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutRight_1);

        btnTCompare = new JButton("Text Compare");
        btnTCompare.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                panelSelectedPage.removeAll();
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                try {
                    /* แก้ Path ตรงนี้ Text Compare ตรงนี้!!!!! */
                    new PDFViewer(new File(oldTextPath), new File(newTextPath));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                panelSelectedPage.revalidate();
                panelSelectedPage.repaint();
            }
        });
        horizontalBoxControls.add(btnTCompare);

        Component horizontalStrutRight = Box.createHorizontalStrut(10);
        horizontalBoxControls.add(horizontalStrutRight);

        Component verticalStrutBottom = Box.createVerticalStrut(10);
        panelControls.add(verticalStrutBottom, BorderLayout.SOUTH);

        Box verticalBoxView = Box.createVerticalBox();
        frame.getContentPane().add(verticalBoxView, BorderLayout.WEST);

        Component verticalStrutView = Box.createVerticalStrut(10);
        verticalBoxView.add(verticalStrutView);

        Box horizontalBoxView = Box.createHorizontalBox();
        verticalBoxView.add(horizontalBoxView);

        Component horizontalStrutViewLeft = Box.createHorizontalStrut(10);
        horizontalBoxView.add(horizontalStrutViewLeft);

        panelSelectedPage = new JPanel();
        panelSelectedPage.setBackground(Color.LIGHT_GRAY);
        horizontalBoxView.add(panelSelectedPage);
        panelSelectedPage.setPreferredSize(new Dimension(width, height));
        panelSelectedPage.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelSelectedPage.setLayout(new BorderLayout(0, 0));

        Component horizontalStrutViewRight = Box.createHorizontalStrut(10);
        horizontalBoxView.add(horizontalStrutViewRight);

        selectPage(0);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.repaint();
    }
}
