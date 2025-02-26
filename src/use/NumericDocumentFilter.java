package use;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Document;

/*
 * The NumericDocumentFilter class extends DocumentFilter and is used to validate and filter the input
 * for text fields based on specific filter types (e.g., integer, double, date).
 * It ensures that the input follows the appropriate format and does not exceed a specified length.
 */

public class NumericDocumentFilter extends DocumentFilter {

    private final int filterType;
    private final int filterLength;

    /*
     * Constructor to initialize the filter type and maximum length.
     * @param filterType - The type of the filter (e.g., integer, double, date).
     * @param maxLength - The maximum allowed length for the input.
     */
    public NumericDocumentFilter(int filterType, int maxLength) {
        this.filterType = filterType;
        this.filterLength = maxLength;
    }

    /*
     * Method to insert a string into the document while validating the input and length.
     * @param fb - The FilterBypass object for bypassing the filter.
     * @param offset - The offset position at which the string is being inserted.
     * @param string - The string to be inserted.
     * @param attr - The attribute set for the string.
     * @throws BadLocationException if an invalid location is specified.
     */
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string != null && isValidInput(fb.getDocument(), offset, string) && isValidLength(fb.getDocument(), string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    /*
     * Method to replace a string in the document while validating the input and length.
     * @param fb - The FilterBypass object for bypassing the filter.
     * @param offset - The offset position at which the string is being replaced.
     * @param length - The length of the string to be replaced.
     * @param string - The string to be inserted in place of the original text.
     * @param attr - The attribute set for the string.
     * @throws BadLocationException if an invalid location is specified.
     */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
        if (string != null && isValidInput(fb.getDocument(), offset, string) && isValidLength(fb.getDocument(), string)) {
            super.replace(fb, offset, length, string, attr);
        }
    }

    /*
     * Method to remove text from the document. No validation is applied for removal.
     * @param fb - The FilterBypass object for bypassing the filter.
     * @param offset - The offset position from which the text is being removed.
     * @param length - The length of the text to be removed.
     * @throws BadLocationException if an invalid location is specified.
     */
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    /*
     * Validates the input string based on the filter type.
     * @param doc - The document being modified.
     * @param offset - The position at which the string is inserted.
     * @param string - The string being inserted.
     * @return true if the input is valid, false otherwise.
     * @throws BadLocationException if an invalid location is encountered.
     */
    private boolean isValidInput(Document doc, int offset, String string) throws BadLocationException {
        String currentText = doc.getText(0, doc.getLength());
        StringBuilder prospectiveText = new StringBuilder(currentText);
        prospectiveText.insert(offset, string);

        switch (filterType) {
            case Constants.filter.FILTER_NULL:
                return true; // No filtering, all input is valid

            case Constants.filter.FILTER_INTEGER:
                return prospectiveText.toString().matches("\\d*"); // Integer only

            case Constants.filter.FILTER_DOUBLE:
                return prospectiveText.toString().matches("\\d*|\\d+\\.\\d*|\\.\\d+"); // Double numbers

            case Constants.filter.FILTER_DATE:
                // Matches "  .  .    ", "DD.MM.YYYY" with optional spaces
                return prospectiveText.toString().matches("\\s*\\d{0,2}\\.?\\d{0,2}\\.?\\d{0,4}\\s*");

            default:
                return false; // Invalid filter type
        }
    }

    /*
     * Validates the length of the input string based on the filter type and maximum allowed length.
     * @param doc - The document being modified.
     * @param string - The string being inserted or replaced.
     * @return true if the new length is within the allowed maximum length, false otherwise.
     */
    private boolean isValidLength(Document doc, String string) {
        int maxLength;
        if (filterType == Constants.filter.FILTER_DATE) {
            if (filterLength > -1) {
                if (filterLength > 10) maxLength = 10;
                else maxLength = filterLength;
            } else maxLength = 10;
        } else if (filterType == Constants.filter.FILTER_INTEGER || filterType == Constants.filter.FILTER_DOUBLE) {
            if (filterLength > -1) {
                if (filterLength > 9) maxLength = 9;
                else maxLength = filterLength;
            } else maxLength = 9;
        } else {
            if (filterLength > -1) maxLength = filterLength;
            else maxLength = 24000;
        }

        int currentLength = doc.getLength();
        int newLength = currentLength + string.length();
        return newLength <= maxLength;
    }

}