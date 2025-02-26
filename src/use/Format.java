package use;

/*
 * The Format class provides static methods for formatting numbers.
 * These methods include formatting numbers with digit grouping and abbreviating large numbers.
 */

public class Format {

    /*
     * Formats an integer with digit grouping, either in full format with spaces or in a short format with an abbreviation.
     * @param index - The number to format.
     * @param full - If true, formats the number in full with spaces; if false, formats with an abbreviation.
     * @return the formatted number as a string.
     */
    public static String toDigitGrouping(int index, boolean full) {

        if (full) {

            //region Full

            // Full currency format - "3260" -> "3 260"
            String cashString = String.valueOf(index);
            StringBuilder formattedCash = new StringBuilder(cashString);
            int length = formattedCash.length();

            // Insert space every 3 digits
            for (int i = length - 3; i > 0; i -= 3) {
                formattedCash.insert(i, ' ');
            }

            return formattedCash.toString();

            //endregion

        } else {

            //region Short

            // Short currency format - "3260" -> "3,26"
            String cashEnd = "";
            String cashStr;

            if (index < Math.pow(10, 3)) {
                // No abbreviation needed
                cashStr = String.valueOf(index);
            } else if (index < Math.pow(10, 6)) {
                // Thousands (k)
                cashEnd = "k";
                cashStr = String.format("%.2f", index / Math.pow(10, 3));
            } else if (index < Math.pow(10, 9)) {
                // Millions (m)
                cashEnd = "m";
                cashStr = String.format("%.2f", index / Math.pow(10, 6));
            } else if (index < Math.pow(10, 12)) {
                // Billions (b)
                cashEnd = "b";
                cashStr = String.format("%.2f", index / Math.pow(10, 9));
            } else if (index < Math.pow(10, 15)) {
                // Trillions (tr)
                cashEnd = "tr";
                cashStr = String.format("%.2f", index / Math.pow(10, 12));
            } else if (index < Math.pow(10, 18)) {
                // Quadrillions (qdtr)
                cashEnd = "qdtr";
                cashStr = String.format("%.2f", index / Math.pow(10, 15));
            } else if (index < Math.pow(10, 21)) {
                // Quintillions (qntr)
                cashEnd = "qntr";
                cashStr = String.format("%.2f", index / Math.pow(10, 18));
            } else {
                // Out of bounds message
                cashEnd = "";
                cashStr = "OUT_OF_BOUNDS";
            }

            // Replace the period with a comma for the short format
            cashStr = cashStr.replace(".", ",");

            // Remove unnecessary trailing zeros after the decimal point
            if (cashStr.length() >= 6) {
                cashStr = cashStr.substring(0, cashStr.length() - 3);
            }

            return cashStr + cashEnd;

            //endregion

        }

    }
}