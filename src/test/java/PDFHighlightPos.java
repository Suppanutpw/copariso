public class PDFHighlightPos {

    // this is innerClass in PDFFile that collect highlighter position range
    public int posStart;
    public int posStop;
    public String words;

    public PDFHighlightPos(int posStart, int posStop, String words) {
        this.posStart = posStart;
        this.posStop = posStop;
        this.words = words;
    }

    public PDFHighlightPos(int posStart, int posStop) {
        this(posStart, posStop, "");
    }

}