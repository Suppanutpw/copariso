import java.util.ArrayList;

/* Dynamic Programming Java implementation of LCS problem */
public class PDFCompareText {

    private ArrayList<Integer> pdfTextPos1, pdfTextPos2;
    private PDFFile file1, file2;

    public PDFCompareText(PDFFile file1, PDFFile file2) {
        this.file1 = file1;
        this.file2 = file2;
    }

    // Function to find LCS of String X[0..m-1] and Y[0..n-1]
    public String LCS(String X, String Y, int m, int n, int[][] T) {
        // return empty string if we have reached the end of
        // either sequence
        if (m == 0 || n == 0) {
            return new String();
        }

        // if last character of X and Y matches
        if (X.charAt(m - 1) == Y.charAt(n - 1))
        {
            // append current character (X[m-1] or Y[n-1]) to LCS of
            // substring X[0..m-2] and Y[0..n-2]
            pdfTextPos1.add(m - 1);
            pdfTextPos2.add(n - 1);
            return LCS(X, Y, m - 1, n - 1, T) + X.charAt(m - 1);
        }

        // else when the last character of X and Y are different

        // if top cell of current cell has more value than the left
        // cell, then drop current character of String X and find LCS
        // of substring X[0..m-2], Y[0..n-1]

        if (T[m - 1][n] > T[m][n - 1]) {
            return LCS(X, Y, m - 1, n, T);
        }
        else {
            // if left cell of current cell has more value than the top
            // cell, then drop current character of String Y and find LCS
            // of substring X[0..m-1], Y[0..n-2]

            return LCS(X, Y, m, n - 1, T);
        }
    }

    // Function to fill lookup table by finding the length of LCS
    // of substring X[0..m-1] and Y[0..n-1]
    public void LCSLength(String X, String Y, int m, int n, int[][] T) {
        // fill the lookup table in bottom-up manner
        for (int i = 1; i <= m; i++)
        {
            for (int j = 1; j <= n; j++)
            {
                // if current character of X and Y matches
                if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                    T[i][j] = T[i - 1][j - 1] + 1;
                }
                // else if current character of X and Y don't match
                else {
                    T[i][j] = Integer.max(T[i - 1][j], T[i][j - 1]);
                }
            }
        }
    }

    public void findNotMatchPos() {
        int m = file1.textLength(), n = file2.textLength();

        // T[i][j] stores the length of LCS of substring
        // X[0..i-1], Y[0..j-1]
        int[][] T = new int[m + 1][n + 1];

        // initialize position of subString
        pdfTextPos1 = new ArrayList();
        pdfTextPos2 = new ArrayList();

        // fill lookup table
        LCSLength(file1.getFileText(), file2.getFileText(), m, n, T);

        // find longest common sequence
        LCS(file1.getFileText(), file2.getFileText(), m, n, T);

        // find range of {not longest common sequence}
        file1.setHighlightPos(findRangeForHighlight(pdfTextPos1, m));
        file2.setHighlightPos(findRangeForHighlight(pdfTextPos2, n));

        // extends {not longest common sequence} range in full word
        findRangeForHighlightWords(file1);
        findRangeForHighlightWords(file2);
    }

    public static ArrayList<PDFHighlightPos> findRangeForHighlight (ArrayList<Integer> pdfTextPos, int textSize) {
        // find position Start - End for highlight word
        int pdfTextPosSize = pdfTextPos.size();
        ArrayList<PDFHighlightPos> pdfHighlightPos =  new ArrayList<PDFHighlightPos>();

        // if {not subsequence words} at first of file
        if (pdfTextPos.get(pdfTextPosSize-1) > 1) {
            pdfHighlightPos.add(new PDFHighlightPos(1, pdfTextPos.get(pdfTextPosSize-1) - 1));
        }

        // find {not subsequence words} position and change it as range for highlight
        for (int i = pdfTextPosSize-2; i >= 0; i--) {
            if (pdfTextPos.get(i) - pdfTextPos.get(i+1) != 1) {
                pdfHighlightPos.add(new PDFHighlightPos(pdfTextPos.get(i+1)+1, pdfTextPos.get(i)-1));
            }
        }

        // if {not subsequence words} at end of file
        if (textSize-1 - pdfTextPos.get(0) > 1) {
            pdfHighlightPos.add(new PDFHighlightPos(pdfTextPos.get(0)+1, textSize-2));
        }

        return pdfHighlightPos;
    }

    public static void findRangeForHighlightWords (PDFFile file) {
        String fileText = file.getFileText();
        ArrayList<PDFHighlightPos> pdfHighlightPos = file.getHighlightPos();
        int textSize = file.textLength(), highlight_length = pdfHighlightPos.size();

        // expends position Start - End for highlight all word (highlight all word that have modified)
        for (PDFHighlightPos highlight: pdfHighlightPos) {
            // find back Start position for {not digit and alphabet}
            for (int i = highlight.posStart; i >= 0; i--) {
                if ( !(Character.isLetter(fileText.charAt(i)) || Character.isDigit(fileText.charAt(i))) ) {
                    highlight.posStart = Math.min(highlight.posStart, i+1);
                    break;
                }
            }

            // find forward Stop position for {not digit and alphabet}
            for (int i = highlight.posStop; i < textSize; i++) {
                if ( !(Character.isLetter(fileText.charAt(i)) || Character.isDigit(fileText.charAt(i))) ) {
                    highlight.posStop = Math.max(highlight.posStop, i-1);
                    break;
                }
            }
        }

        // if first or last word have modify in range of 5 characters (average of english word length)
        if (pdfHighlightPos.get(0).posStart <= 5) {
            pdfHighlightPos.get(0).posStart = 0;
        }
        if (pdfHighlightPos.get(highlight_length-1).posStop >= textSize-5) {
            pdfHighlightPos.get(highlight_length-1).posStop = textSize-2;
        }

        // fix overlap range because of expends range for word
        for (int i = 0; i < pdfHighlightPos.size()-1; i++) {
            // if Stop position of current range overlap with Start of next range
            // or distance between Stop position of current range and with Start of next range <= 5
            if ( Math.abs(pdfHighlightPos.get(i).posStop - Math.abs(pdfHighlightPos.get(i+1).posStart)) <= 5
                    || pdfHighlightPos.get(i+1).posStart < pdfHighlightPos.get(i).posStop) {
                // then combine it together (combine in current range and delete next range)
                pdfHighlightPos.get(i).posStop = pdfHighlightPos.get(i+1).posStop;
                pdfHighlightPos.remove(i+1);
                i--; // if there have overlap re-find a same position
            }
        }

    }

}