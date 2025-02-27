package ui;

import use.Constants;
import use.Date;
import use.Format;
import use.NumericDocumentFilter;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionListener;

/*
 * The Panels class provides utility methods to create and manage GUI panels, frames, dialogs,
 * text fields, buttons, date pickers, combo boxes, and other components used in the application.
 * It also provides helper methods for handling panel states and setting properties for components.
 */

public abstract class Panels {

    // Constants
    // Font used for labels in the panels
    protected final Font labelFont = new Font("Sans Serif", Font.BOLD, 12);

    // Screen size of the current device
    protected final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // Center positions on the screen
    // xStart and yStart are calculated as the center of the screen
    protected final int xStart = screenSize.width / 2;  // x-coordinate of screen center
    protected final int yStart = screenSize.height / 2; // y-coordinate of screen center

    // Usability flags to track panel status
    private static boolean panelExists = false; // Whether the panel exists
    private static boolean editable = false;    // Whether the panel is editable

    // Static getter methods to check if a panel exists or is editable
    public static boolean isPanelExists() {
        return panelExists;
    }

    public static boolean isPanelEditable() {
        return editable;
    }

    // Static setter methods to set panel existence and editable status
    public static void setPanelExist(boolean status) {
        panelExists = status;
    }

    public static void setPanelEditable(boolean status) {
        editable = status;
    }

    /*
     * Method to initialize a JFrame (main window).
     * @param x, y - the screen position where the frame should appear. If -1, it is centered.
     * @param width, height - the size of the frame.
     * @param title - the title of the frame.
     * @return a JFrame object.
     */
    public JFrame initializeFrame(int x, int y, int width, int height, String title) {

        // Default the x and y to center the frame if values are -1
        if (x == -1) x = (screenSize.width / 2);
        if (y == -1) y = (screenSize.height / 2);

        int xstart, ystart;

        // Adjust the x and y so the frame is centered on the screen
        if (x - (width / 2) < 0) xstart = 0;
        else xstart = x - (width / 2);

        if (y - (height / 2) < 0) ystart = 0;
        else ystart = y - (height / 2);

        // Create and configure the JFrame
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setBounds(xstart, ystart, width, height);
        frame.setResizable(false);

        return frame;

    }

    /*
     * Method to initialize a JDialog (secondary window on top of the main frame).
     * @param x, y - the screen position where the dialog should appear. If -1, it is centered.
     * @param width, height - the size of the dialog.
     * @param mainFrame - the JFrame to set this dialog on top of.
     * @param title - the title of the dialog.
     * @return a JDialog object.
     */
    protected JDialog initializeDialog(int x, int y, int width, int height, JFrame mainFrame, String title) {

        // Default the x and y to center the dialog if values are -1
        if (x == -1) x = (screenSize.width / 2);
        if (y == -1) y = (screenSize.height / 2);

        setPanelExist(true);
        int xstart, ystart;

        // Adjust the x and y so the dialog is centered on the screen
        if (x - (width / 2) < 0) xstart = 0;
        else xstart = x - (width / 2);

        if (y - (height / 2) < 0) ystart = 0;
        else ystart = y - (height / 2);

        // Create and configure the JDialog
        JDialog frame = new JDialog(mainFrame, title);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setBounds(xstart, ystart, width, height);
        frame.setResizable(false);

        return frame;

    }

    /*
     * Method to create a basic JPanel with a white background and no layout manager.
     * @return a JPanel object.
     */
    protected JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        return panel;
    }

    /*
     * Method to create a JPanel with specific properties such as position, size, color, and layout.
     * @param x, y - position on the parent panel.
     * @param width, height - size of the panel.
     * @param color - background color of the panel.
     * @param mainPanel - parent panel to add the newly created panel to.
     * @return a JPanel object.
     */
    protected JPanel createPanelExt(int x, int y, int width, int height, Color color, JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setBackground(color);
        panel.setLayout(null);
        if (mainPanel != null) mainPanel.add(panel);
        return panel;
    }

    /*
     * Method to create a button and add it to the specified parent panel.
     * @param x, y - position of the button.
     * @param width - width of the button.
     * @param button - the JButton to be created.
     * @param panelParent - the parent JPanel where the button should be added.
     */
    protected JButton createButton(int x, int y, int width, String name, JPanel panelParent) {
        JButton button = new JButton(name);
        button.setBounds(x, y, width, 30);
        button.addActionListener((ActionListener) this);
        button.setFocusable(true);
        if (panelParent != null) panelParent.add(button);
        return button;
    }

    /*
     * Method to create a JTextField with a label and formatting. Also applies a document filter for validation.
     * @param panel - the parent panel to add the text field to.
     * @param x, y - position of the label and text field.
     * @param title - the label text.
     * @param inputbox - the JTextField object.
     * @param inputboxString - initial text for the input field.
     * @param editable - whether the input field is editable or not.
     * @param filter - type of filter to apply (numeric, etc.).
     * @param maxLength - the maximum length of input allowed.
     * @param format - formatting style for the text.
     */
    protected void createTextField(JPanel panel, int x, int y, String title, JTextField inputbox, String inputboxString, boolean editable, int filter, int maxLength, int format) {

        // Set the initial text if provided
        if (inputboxString != null && (!inputboxString.isEmpty())) inputbox.setText(inputboxString);

        // Label creation and positioning
        JLabel label = new JLabel(title);
        label.setFont(labelFont);
        label.setBounds(x, y, 200, 20);
        label.setForeground((editable) ? Color.black : Color.darkGray);

        // Text field creation and configuration
        inputbox.setBounds(x, y + 20, 237, 25);
        inputbox.setEnabled(editable);
        inputbox.setDisabledTextColor(Color.darkGray);

        // Formatted text field if formatting is applied
        JTextField inputboxformat = new JTextField();
        if (format != -1 && inputboxString != null && (!inputboxString.isEmpty())) {
            inputboxformat.setBounds(x, y + 20, 237, 25);
            inputboxformat.setEnabled(editable);
            inputboxformat.setDisabledTextColor(Color.darkGray);

            if (format == Constants.format.DIGIT_GROUPING) {
                // Apply digit grouping format
                inputboxformat.setText(Format.toDigitGrouping(Integer.parseInt(inputboxString), true));
            } else {
                // Use plain input box text
                inputboxformat.setText(inputboxString);
            }
        }

        // Apply document filter based on the input type
        ((AbstractDocument) inputbox.getDocument()).setDocumentFilter(new NumericDocumentFilter(filter, maxLength));

        // Add the label and the appropriate input box to the panel
        panel.add(label);
        if (editable || format == -1) panel.add(inputbox);
        else panel.add(inputboxformat);
    }

    /*
     * Method to create a JTextArea with a label and scrollable area.
     * @param panel - the parent panel to add the text area to.
     * @param x, y - position of the label and text area.
     * @param height - height of the scrollable area.
     * @param title - the label text.
     * @param inputbox - the JTextArea object.
     * @param inputboxString - initial text for the input area.
     * @param editable - whether the input area is editable or not.
     * @param maxLength - the maximum length of input allowed.
     */
    protected void createTextArea(JPanel panel, int x, int y, int height, String title, JTextArea inputbox, String inputboxString, boolean editable, int maxLength) {

        // Set the initial text if provided
        if (inputboxString != null && !inputboxString.isEmpty()) {
            inputbox.setText(inputboxString);
        }

        // Label creation
        JLabel label = new JLabel(title);
        label.setBounds(x, y, 200, 20);
        label.setForeground(editable ? Color.black : Color.darkGray);

        // JTextArea configuration
        inputbox.setEnabled(editable);
        inputbox.setDisabledTextColor(Color.darkGray);
        inputbox.setLineWrap(true);
        inputbox.setWrapStyleWord(true);
        inputbox.setRows(5);
        inputbox.setColumns(20);

        // Create and add a JScrollPane for the text area
        JScrollPane scrollPane = new JScrollPane(inputbox);
        scrollPane.setBounds(x, y + 20, 237, height);

        // Apply document filter for input validation
        ((AbstractDocument) inputbox.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_NULL, maxLength));

        // Add components to the panel
        panel.add(label);
        panel.add(scrollPane);
    }

    /*
     * Method to create a date input field with day, month, and year text fields and labels.
     * @param panel - the parent panel to add the date fields to.
     * @param x, y - position of the labels and fields.
     * @param title - the label for the date field.
     * @param dateDay, dateMonth, dateYear - JTextField components for day, month, and year.
     * @param dateLoad - a Date object to pre-fill the date fields if provided.
     * @param editable - whether the date fields are editable or not.
     */
    protected void createDateField(JPanel panel, int x, int y, String title, JTextField dateDay, JTextField dateMonth, JTextField dateYear, Date dateLoad, boolean editable) {

        // Set date fields with the provided date values
        if (dateLoad != null) {
            dateDay.setText("" + dateLoad.getDay());
            dateMonth.setText("" + dateLoad.getMonth());
            dateYear.setText("" + dateLoad.getYear());
        }

        // General label for the date
        JLabel label = new JLabel(title);
        label.setFont(labelFont);
        label.setBounds(x, y, 200, 20);

        // Create labels for separators ("/")
        JLabel labelDot1 = new JLabel("/");
        labelDot1.setFont(labelFont);
        labelDot1.setBounds(x + 57, y + 20, 20, 25);

        JLabel labelDot2 = new JLabel("/");
        labelDot2.setFont(labelFont);
        labelDot2.setBounds(x + 127, y + 20, 20, 25);

        // Date text fields configuration
        dateDay.setBounds(x, y + 20, 50, 25);
        dateDay.setEnabled(editable);
        dateDay.setFocusable(editable);
        dateDay.setDisabledTextColor(Color.darkGray);
        ((AbstractDocument) dateDay.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_INTEGER, 2));

        dateMonth.setBounds(x + 70, y + 20,50, 25);
        dateMonth.setEnabled(editable);
        dateMonth.setFocusable(editable);
        dateMonth.setDisabledTextColor(Color.darkGray);
        ((AbstractDocument) dateMonth.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_INTEGER, 2));

        dateYear.setBounds(x + 140, y + 20,97, 25);
        dateYear.setEnabled(editable);
        dateYear.setFocusable(editable);
        dateYear.setDisabledTextColor(Color.darkGray);
        ((AbstractDocument) dateYear.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_INTEGER, -1));

        // Add components to the panel
        panel.add(label);
        panel.add(labelDot1);
        panel.add(labelDot2);
        panel.add(dateDay);
        panel.add(dateMonth);
        panel.add(dateYear);
    }

    /*
     * Method to create a JComboBox (drop-down) with a label and formatting.
     * @param panel - the parent panel to add the combo box to.
     * @param x, y - position of the label and combo box.
     * @param title - the label text.
     * @param combobox - the JComboBox object.
     * @param selectedItem - the pre-selected item in the combo box.
     * @param editable - whether the combo box is editable or not.
     */
    protected void createComboBox(JPanel panel, int x, int y, String title, JComboBox<String> combobox, String selectedItem, boolean editable) {

        // Label creation
        JLabel label = new JLabel(title);
        label.setFont(labelFont);
        label.setBounds(x, y, 200, 20);

        // JComboBox configuration
        combobox.setBounds(x, y + 20, 237, 25);
        combobox.setEnabled(editable);

        // Set the selected item in the combo box if it exists
        if (selectedItem != null && isOptionInComboBox(combobox, selectedItem)) combobox.setSelectedItem(selectedItem);

        // Add components to the panel
        panel.add(label);
        panel.add(combobox);
    }

    /*
     * Utility method to check if an item exists in the combo box.
     * @param comboBox - the JComboBox object to check.
     * @param searchString - the item to search for.
     * @return true if the item exists, false otherwise.
     */
    protected static boolean isOptionInComboBox(JComboBox<String> comboBox, String searchString) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(searchString)) {
                return true;
            }
        }

        return false;
    }

}