package ui;

import use.Constants;
import use.Date;
import use.Format;
import use.NumericDocumentFilter;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Panels {

    // Constants
    protected final Font labelFont = new Font("Sans Serif", Font.BOLD, 12);

    // Usability
    private static boolean panelExists = false;
    private static boolean editable = false;

    // Is panel...
    public static boolean isPanelExists() {
        return panelExists;
    }
    public static boolean isPanelEditable() {
        return editable;
    }

    // Set panel...
    public static void setPanelExist(boolean status) {
        panelExists = status;
    }
    public static void setPanelEditable(boolean status) {
        editable = status;
    }

    // Initialize Dialog window - frame on top of mine frame
    protected JDialog initializeDialog(int x, int y, int width, int height, JFrame mainFrame, String title) {

        setPanelExist(true);
        int xstart, ystart;

        if (x - (width / 2) < 0) xstart = 0;
        else xstart = x - (width / 2);

        if (y - (height / 2) < 0) ystart = 0;
        else ystart = y - (height / 2);

        JDialog frame = new JDialog(mainFrame, title);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setBounds(xstart, ystart, width, height);
        frame.setResizable(false);

        return frame;

    }

    // Create panel
    protected JPanel createPanelExt(int x, int y, int width, int height, Color color, JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setBackground(color);
        panel.setLayout(null);
        if (mainPanel != null) mainPanel.add(panel);
        return panel;
    }

    // Create main panel
    protected JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        return panel;
    }

    // Create button
    protected void createButton(int x, int y, int width, JButton button, JPanel panelParent) {

        button.setBounds(x, y, width, 30);
        button.addActionListener((ActionListener) this);
        button.setFocusable(true);
        panelParent.add(button);

    }

    // Create JTextField - input box with title and formatting
    protected void createTextField(JPanel panel, int x, int y, String title, JTextField inputbox, String inputboxString, boolean editable, int filter, int maxLength, int format) {

        // Get index
        if (inputboxString != null && (!inputboxString.isEmpty())) inputbox.setText(inputboxString);

        // Label
        JLabel label = new JLabel(title);
        label.setFont(labelFont);
            label.setBounds(x, y, 200, 20);
        label.setForeground((editable) ? Color.black : Color.darkGray);

        // Textbox
        inputbox.setBounds(x, y + 20, 237, 25);
        inputbox.setEnabled(editable);
        inputbox.setDisabledTextColor(Color.darkGray);

        // Formatted textbox
        JTextField inputboxformat = new JTextField();
        if (format != -1 && inputboxString != null && (!inputboxString.isEmpty())) {

            inputboxformat.setBounds(x, y + 20, 237, 25);
            inputboxformat.setEnabled(editable);
            inputboxformat.setDisabledTextColor(Color.darkGray);

            if (format == Constants.format.DIGIT_GROUPING) {
                // format.DIGIT_GROUPING - to digit-grouping
                inputboxformat.setText(Format.toDigitGrouping(Integer.parseInt(inputboxString), true));
            } else {
                // NULL
                inputboxformat.setText(inputboxString);
            }

        }

        // Textbox filter
        ((AbstractDocument) inputbox.getDocument()).setDocumentFilter(new NumericDocumentFilter(filter, maxLength));

        panel.add(label);
        if (editable || format == -1) panel.add(inputbox);
        else panel.add(inputboxformat);

    }

    // Create JTextArea - input box with title and formatting
    protected void createTextArea(JPanel panel, int x, int y, int height, String title, JTextArea inputbox, String inputboxString, boolean editable, int maxLength) {

        // Get index and set text if provided
        if (inputboxString != null && !inputboxString.isEmpty()) {
            inputbox.setText(inputboxString);
        }

        // Label
        JLabel label = new JLabel(title);
        label.setBounds(x, y, 200, 20);
        label.setForeground(editable ? Color.black : Color.darkGray);

        // Textbox
        inputbox.setEnabled(editable);
        inputbox.setDisabledTextColor(Color.darkGray);
        inputbox.setLineWrap(true);
        inputbox.setWrapStyleWord(true);

        inputbox.setRows(5);
        inputbox.setColumns(20);

        // Add JScrollPane
        JScrollPane scrollPane = new JScrollPane(inputbox);
        scrollPane.setBounds(x, y + 20, 237, height);  // Set the position and size of the scrollPane

        // Textbox filter
        ((AbstractDocument) inputbox.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_NULL, maxLength));

        panel.add(label);
        panel.add(scrollPane);
    }

    // Create JDateField - date box with title and formatting
    protected void createDateField(JPanel panel, int x, int y, String title, JTextField dateDay, JTextField dateMonth, JTextField dateYear, Date dateLoad, boolean editable) {

        // Set textboxes
        if (dateLoad != null) {

            // Day
            dateDay.setText("" + dateLoad.getDay());

            // Month
            dateMonth.setText("" + dateLoad.getMonth());

            // Year
            dateYear.setText("" + dateLoad.getYear());

        }

        // Label - general
        JLabel label = new JLabel(title);
        label.setFont(labelFont);
        label.setBounds(x, y, 200, 20);

        // Label - DD "/" MM
        JLabel labelDot1 = new JLabel("/");
        labelDot1.setFont(labelFont);
        labelDot1.setBounds(x + 57, y + 20, 20, 25);

        // Label - MM "/" YYYY
        JLabel labelDot2 = new JLabel("/");
        labelDot2.setFont(labelFont);
        labelDot2.setBounds(x + 127, y + 20, 20, 25);

        // Day
        dateDay.setBounds(x, y + 20, 50, 25);
        dateDay.setEnabled(editable);
        dateDay.setFocusable(editable);
        dateDay.setDisabledTextColor(Color.darkGray);
        ((AbstractDocument) dateDay.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_INTEGER, 2));

        // Month
        dateMonth.setBounds(x + 70, y + 20,50, 25);
        dateMonth.setEnabled(editable);
        dateMonth.setFocusable(editable);
        dateMonth.setDisabledTextColor(Color.darkGray);
        ((AbstractDocument) dateMonth.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_INTEGER, 2));

        // Year
        dateYear.setBounds(x + 140, y + 20,97, 25);
        dateYear.setEnabled(editable);
        dateYear.setFocusable(editable);
        dateYear.setDisabledTextColor(Color.darkGray);
        ((AbstractDocument) dateYear.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_INTEGER, -1));

        panel.add(label);
        panel.add(labelDot1);
        panel.add(labelDot2);
        panel.add(dateDay);
        panel.add(dateMonth);
        panel.add(dateYear);

    }

    // Create JComboBox - combo box with title and formatting
    protected void createComboBox(JPanel panel, int x, int y, String title, JComboBox<String> combobox, String selectedItem, boolean editable) {

        // Label
        JLabel label = new JLabel(title);
        label.setFont(labelFont);
        label.setBounds(x, y, 200, 20);

        // Combo box
        combobox.setBounds(x, y + 20, 237, 25);
        combobox.setEnabled(editable);

        // Set index
        if (selectedItem != null && isOptionInComboBox(combobox, selectedItem)) combobox.setSelectedItem(selectedItem);

        panel.add(label);
        panel.add(combobox);
    }

    // Is given item part of a combo box?
    protected static boolean isOptionInComboBox(JComboBox<String> comboBox, String searchString) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(searchString)) {
                return true;
            }
        }

        return false;
    }

}