package ui.frame;

import components.Car;
import ui.Panels;
import use.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VehicleSearch extends Panels implements ActionListener {

    private boolean isTableCreated = false;

    JDialog frame;
    JButton btnSearch = new JButton("Търси");
    JButton btnClear = new JButton("Изчисти търсенето");
    JButton btnView = new JButton("Отвори");
    JTextField txSearchByRegNum = new JTextField();
    JCheckBox chSearchByRegNum = new JCheckBox();
    JTextField txSearchByBrand = new JTextField();
    JCheckBox chSearchByBrand = new JCheckBox();
    JTextField txSearchByModel = new JTextField();
    JCheckBox chSearchByModel = new JCheckBox();
    JTextField txSearchByInsuranceDay = new JTextField();
    JTextField txSearchByInsuranceMonth = new JTextField();
    JTextField txSearchByInsuranceYear = new JTextField();
    JCheckBox chSearchByInsurance = new JCheckBox();
    JTextField txSearchByRegistration = new JTextField();
    JCheckBox chSearchByRegistration = new JCheckBox();
    JComboBox<String> cbTypeVehicle;
    JTable tableSearch;
    JPanel panelTable;

    public VehicleSearch(int x, int y) {

        // Create frame
        frame = initializeDialog(x, y, 830, 400, Mine.frame, "Търсене на МПС-та");

        // Panel
        JPanel panel = createPanel();

        // Panel - search attributes
        JPanel panelSearch = createPanelExt(0, 0, frame.getWidth(), 60, Color.lightGray, panel);

        // Panel - bottom
        JPanel panelBottom = createPanelExt(0, frame.getHeight() - 80, frame.getWidth(), 60, Color.lightGray, panel);

        // Panel - table
        panelTable = createPanelExt(0, 60, frame.getWidth(), frame.getHeight() - 140, Color.white, panel);

        // Search by vehicle type
        cbTypeVehicle = new JComboBox<>();
        cbTypeVehicle.addItem("Всички");
        Object[][] garageData = DBFiles.garage.loadGarageList(true);
        for (Object[] garage : garageData) {
            cbTypeVehicle.addItem((String) garage[0]);
        }
        createSearchByType(20, 7, "Гараж:", cbTypeVehicle, panelSearch);

        // Search by insurance
        createSearchByDate(20, 32, "Каско:", txSearchByInsuranceDay, txSearchByInsuranceMonth, txSearchByInsuranceYear, chSearchByInsurance, panelSearch);

        // Search by registration number
        createSearchBy(290, 7, "Рег.но.:", txSearchByRegNum, Constants.filter.FILTER_NULL, chSearchByRegNum, panelSearch);

        // Search by brand
        createSearchBy(290, 32, "Марка:", txSearchByBrand, Constants.filter.FILTER_NULL, chSearchByBrand, panelSearch);

        // Search by model
        createSearchBy(560, 7, "Модел:", txSearchByModel, Constants.filter.FILTER_NULL, chSearchByModel, panelSearch);

        // Search by registration
        createSearchBy(560, 32, "Регистр.:", txSearchByRegistration, Constants.filter.FILTER_INTEGER, chSearchByRegistration, panelSearch);

        // Search button
        createButton(panelBottom.getWidth() - 105, 5, 80, btnSearch, panelBottom);

        // Clear search button
        createButton(panelBottom.getWidth() - 260, 5, 150, btnClear, panelBottom);

        // Edit button
        createButton(panelBottom.getWidth() - 385, 5, 120, btnView, panelBottom);

        panel.add(panelTable);
        frame.add(panel);
        frame.setVisible(true);

    }

    // Create table
    private void createSearchTable(String filterGarage, String filterRegNumber, String filterBrand, String filterModel, String filterDateInsurance, String filterRegister) {

        // Get data
        Object[][] tableData = DBFiles.car.loadVehicleList(filterGarage, filterRegNumber, filterBrand, filterModel, filterDateInsurance, filterRegister);
        String[] tableTitles = { "Рег.номер", "Марка", "Модел", "Разход", "BHP", "Година на регистрация", "Вид гориво" };

        // Disable editable cells
        DefaultTableModel tableModel = new DefaultTableModel(tableData, tableTitles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableSearch = new JTable(tableModel);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableSearch.getModel());
        tableSearch.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(tableSearch);

        panelTable.setLayout(null);
        scrollPane.setBounds(0, 0, panelTable.getWidth() - 15, panelTable.getHeight());
        scrollPane.setForeground(Color.white);

        panelTable.add(scrollPane);
        panelTable.revalidate();
        panelTable.repaint();
        isTableCreated = true;

    }

    // Clear table
    private void clearTable() {
        panelTable.removeAll();
        panelTable.revalidate();
        panelTable.repaint();
        isTableCreated = false;
    }

    // Search by type
    private void createSearchByType(int x, int y, String title, JComboBox<String> combobox, JPanel panelParent) {

        JLabel label = new JLabel(title);
        label.setBounds(x, y, 60, 20);
        label.setFont(labelFont);

        combobox.setBounds(x + 60, y, 163, 20);

        panelParent.add(combobox);
        panelParent.add(label);

    }

    // Search by Field
    private void createSearchBy(int x, int y, String title, JTextField inputbox, int filter, JCheckBox checkbox, JPanel parentPanel) {

        JLabel label = new JLabel(title);
        label.setBounds(x, y, 60, 20);
        label.setFont(labelFont);

        inputbox.setBounds(x + 60, y, 150, 20);
        inputbox.setFont(labelFont);

        checkbox.setBounds(x + 220, y - 1, 20, 20);
        checkbox.setBackground(parentPanel.getBackground());

        ((AbstractDocument) inputbox.getDocument()).setDocumentFilter(new NumericDocumentFilter(filter, -1));

        parentPanel.add(label);
        parentPanel.add(inputbox);
        parentPanel.add(checkbox);

    }

    // Search by Date
    private void createSearchByDate(int x, int y, String title, JTextField inputboxDay, JTextField inputboxMonth, JTextField inputboxYear, JCheckBox checkbox, JPanel parentPanel) {

        JLabel label = new JLabel(title);
        label.setBounds(x, y, 60, 20);
        label.setFont(labelFont);

        inputboxDay.setBounds(x + 60, y, 25, 20);
        inputboxDay.setFont(labelFont);
        ((AbstractDocument) inputboxDay.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_INTEGER, 2));

        JLabel label2 = new JLabel("/");
        label2.setBounds(x + 90, y, 20, 20);

        inputboxMonth.setBounds(x + 100, y, 25, 20);
        inputboxMonth.setFont(labelFont);
        ((AbstractDocument) inputboxMonth.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_INTEGER, 2));

        JLabel label1 = new JLabel("/");
        label1.setBounds(x + 130, y, 20, 20);

        inputboxYear.setBounds(x + 140, y, 45, 20);
        inputboxYear.setFont(labelFont);
        ((AbstractDocument) inputboxYear.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_INTEGER, -1));

        checkbox.setBounds(x + 195, y - 1, 20, 20);
        checkbox.setBackground(parentPanel.getBackground());

        parentPanel.add(label);
        parentPanel.add(label1);
        parentPanel.add(label2);
        parentPanel.add(inputboxDay);
        parentPanel.add(inputboxMonth);
        parentPanel.add(inputboxYear);
        parentPanel.add(checkbox);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnSearch) {

            clearTable();
            String filterGarage, filterRegNum, filterBrand, filterModel, filterInsurance, filterRegistration;

            if (cbTypeVehicle.getSelectedIndex() != 0) {
                filterGarage = (String) cbTypeVehicle.getSelectedItem();
            } else filterGarage = "";

            if (!txSearchByRegNum.getText().isEmpty() && chSearchByRegNum.isSelected()) {
                filterRegNum = txSearchByRegNum.getText();
            } else filterRegNum = "";

            if (!txSearchByBrand.getText().isEmpty() && chSearchByBrand.isSelected()) {
                filterBrand = txSearchByBrand.getText();
            } else filterBrand = "";

            if (!txSearchByModel.getText().isEmpty() && chSearchByModel.isSelected()) {
                filterModel = txSearchByModel.getText();
            } else filterModel = "";

            if (!txSearchByRegistration.getText().isEmpty() && chSearchByRegistration.isSelected()) {
                filterRegistration = txSearchByRegistration.getText();
            } else filterRegistration = "";

            if (
                    (!txSearchByInsuranceDay.getText().isEmpty()) &&
                    (!txSearchByInsuranceMonth.getText().isEmpty()) &&
                    (!txSearchByInsuranceYear.getText().isEmpty()) &&
                    (chSearchByInsurance.isSelected())
            ) {
                filterInsurance = new Date(
                        Integer.parseInt(txSearchByInsuranceYear.getText()),
                        Integer.parseInt(txSearchByInsuranceMonth.getText()),
                        Integer.parseInt(txSearchByInsuranceDay.getText())
                ).toString(false);
            } else filterInsurance = "";

            // Reload table
            createSearchTable(filterGarage, filterRegNum, filterBrand, filterModel, filterInsurance, filterRegistration);

        }

        if (e.getSource() == btnClear) {

            // Clear table
            clearTable();

        }

        if (e.getSource() == btnView) {

            // Edit selected
            if (isTableCreated) {
                int selectedRow = tableSearch.getSelectedRow();
                Object firstCellValue = tableSearch.getValueAt(selectedRow, 0);
                if (firstCellValue != null) {
                    Car car = DBFiles.car.loadCar(firstCellValue.toString());
                    int xStart = frame.getX() + (frame.getWidth() / 2);
                    int yStart = frame.getY() + (frame.getHeight() / 2);
                    new CarInfo(xStart, yStart, car, false, false);
                }
            }

        }

    }

}
