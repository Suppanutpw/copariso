import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;

public class PDFHighlighter extends PDFTextStripper {
    private static List<double[]> coordinates;
    private static ArrayList tokenStream;

    private static int wordCounter, pdfPosCounter;

    // check that is the highlight pen put on paper or not
    private static boolean isHighlight;

    // My Data for find specific words for highlighter
    private static ArrayList<PDFHighlightPos> highlightPos;
    private static String fileText;

    public PDFHighlighter() throws IOException {
        //data structured containing coordinates information for each token
        coordinates = new ArrayList<>();

        //List of words extracted from text (considering a whitespace-based tokenization)
        tokenStream = new ArrayList();

        // list of words order for highlight {start - end}
        highlightPos = new ArrayList<PDFHighlightPos>();

    }

    public static void highlight(PDFFile pdfFile, PDColor textColor) throws IOException {

        try {
            //Loading an existing document
            File file = new File(pdfFile.getTargetPath());
            PDDocument document = PDDocument.load(file);

            //extended PDFTextStripper class
            PDFTextStripper stripper = new PDFHighlighter();

            //Get number of pages
            int number_of_pages = document.getDocumentCatalog().getPages().getCount();

            // Before call writeText that call writeString i config value for calculate
            PDFHighlighter.highlightPos = pdfFile.getHighlightPos();
            PDFHighlighter.fileText = pdfFile.getFileText();
            wordCounter = pdfPosCounter = 0;
            isHighlight = false;

            // The method writeText will invoke an override version of writeString
            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);

            // Print collected information
            /*
            System.out.println("========== HIGHLIGHTER RESULT " + pdfFile.getTargetFileName() + " ==========");
            System.out.println(tokenStream);
            System.out.println(tokenStream.size());
            System.out.println(coordinates.size());
            */

            double page_height;
            double page_width;
            double width, height, minx, maxx, miny, maxy;
            int rotation;

            //scan each page and highlight all the words inside them
            for (int page_index = 0; page_index < number_of_pages; page_index++) {
                //get current page
                PDPage page = document.getPage(page_index);

                //Get annotations for the selected page
                List<PDAnnotation> annotations = page.getAnnotations();

                //Page height and width
                page_height = page.getMediaBox().getHeight();
                page_width = page.getMediaBox().getWidth();

                //Scan collected coordinates
                for (int i = 0; i < coordinates.size(); i++) {
                    //if the current coordinates are not related to the current
                    //page, ignore them
                    if ((int) coordinates.get(i)[4] != (page_index + 1))
                        continue;
                    else {
                        //get rotation of the page...portrait..landscape..
                        rotation = (int) coordinates.get(i)[7];

                        //page rotated of 90degrees
                        if (rotation == 90) {
                            height = coordinates.get(i)[5];
                            width = coordinates.get(i)[6];
                            width = (page_height * width) / page_width;

                            //define coordinates of a rectangle
                            maxx = coordinates.get(i)[1];
                            minx = coordinates.get(i)[1] - height;
                            miny = coordinates.get(i)[0];
                            maxy = coordinates.get(i)[0] + width;
                        } else //i should add here the cases -90/-180 degrees
                        {
                            height = coordinates.get(i)[5];
                            minx = coordinates.get(i)[0];
                            maxx = coordinates.get(i)[2];
                            miny = page_height - coordinates.get(i)[1];
                            maxy = page_height - coordinates.get(i)[3] + height;
                        }

                        //Add an annotation for each scanned word
                        PDAnnotationTextMarkup txtMark = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
                        txtMark.setColor(textColor);
                        txtMark.setConstantOpacity((float) 0.3); // 30% transparent
                        PDRectangle position = new PDRectangle();
                        position.setLowerLeftX((float) minx);
                        position.setLowerLeftY((float) miny);
                        position.setUpperRightX((float) maxx);
                        position.setUpperRightY((float) ((float) maxy + height));
                        txtMark.setRectangle(position);

                        float[] quads = new float[8];
                        quads[0] = position.getLowerLeftX();  // x1
                        quads[1] = position.getUpperRightY() - 2; // y1
                        quads[2] = position.getUpperRightX(); // x2
                        quads[3] = quads[1]; // y2
                        quads[4] = quads[0];  // x3
                        quads[5] = position.getLowerLeftY() - 2; // y3
                        quads[6] = quads[2]; // x4
                        quads[7] = quads[5]; // y5
                        txtMark.setQuadPoints(quads);
                        txtMark.setContents(tokenStream.get(i).toString());
                        annotations.add(txtMark);
                    }
                }
            }

            //Saving the document in a new file
            File highlighted_doc = new File(pdfFile.getResultPath());
            document.save(highlighted_doc);

            document.close();
        } catch (IOException e) {
            System.out.println(e);
            throw e;
        }

    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        int token_length = textPositions.size();
        int highlight_length = highlightPos.size();
        double minx = 0, maxx = 0, miny = 0, maxy = 0;
        double height = 0;
        double width = 0;
        int rotation = 0;
        int start_line_word = wordCounter;

        for (TextPosition text : textPositions) {
            rotation = text.getRotation();

            if (text.getHeight() > height)
                height = text.getHeight();

            if (text.getWidth() > width)
                width = text.getWidth();
        }

        // if highlight put on paper when start it mean highlight continue from last line
        if (isHighlight) {
            minx = textPositions.get(0).getX();
            miny = textPositions.get(0).getY();
        }
        for (int i = 0; pdfPosCounter < highlight_length && i < token_length; i++, wordCounter++) {
            if (!isHighlight) {
                // if highlight pen {not} put on paper
                if (wordCounter == highlightPos.get(pdfPosCounter).posStart) {
                    // find position to put pen on paper with highlightPos range
                    minx = textPositions.get(i).getX();
                    miny = textPositions.get(i).getY();
                    isHighlight = true;

                    // re-find the same position for {Stop in a same position case}
                    i--;
                    wordCounter--;
                }
            } else {
                // if highlight have put on paper
                if (wordCounter == highlightPos.get(pdfPosCounter).posStop) {
                    maxx = textPositions.get(i).getEndX();
                    maxy = textPositions.get(i).getY();

                    double word_coordinates[] = {minx, miny, maxx, maxy, this.getCurrentPageNo(), height, width, rotation};
                    coordinates.add(word_coordinates);
                    tokenStream.add(fileText.substring(Math.max(highlightPos.get(pdfPosCounter).posStart, start_line_word), Math.min(highlightPos.get(pdfPosCounter).posStop, wordCounter) + 1));

                    pdfPosCounter++;
                    isHighlight = false;
                }
            }
        }

        // if pen put on paper but not found Stop then highlight till end line
        if (isHighlight) {
            maxx = textPositions.get(token_length - 1).getEndX();
            maxy = textPositions.get(token_length - 1).getY();

            double word_coordinates[] = {minx, miny, maxx, maxy, this.getCurrentPageNo(), height, width, rotation};
            coordinates.add(word_coordinates);
            tokenStream.add(fileText.substring(Math.max(highlightPos.get(pdfPosCounter).posStart, start_line_word), Math.min(highlightPos.get(pdfPosCounter).posStop, wordCounter) + 1));
        }
        // if end line is the same position of Stop range (can't compare end line position in loop)
        if (pdfPosCounter < highlight_length && wordCounter == highlightPos.get(pdfPosCounter).posStop) {
            pdfPosCounter++;
            isHighlight = false;
        }
        // plus end line word +1
        wordCounter++;
    }
}