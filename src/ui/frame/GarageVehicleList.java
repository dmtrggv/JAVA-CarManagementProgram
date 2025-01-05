package ui.frame;

import components.Garage;
import ui.Panels;
import use.Files;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GarageVehicleList extends Panels implements ActionListener {

    JDialog frame;
    JButton btnOpen = new JButton("Отвори");
    JTable tableSearch;
    JPanel panelTable;

    public GarageVehicleList(int x, int y, String garageName) {

        // Create frame
        frame = initializeDialog(x, y, 830, 300, Mine.frame, "МПС-тата на " + garageName);

        // Panel
        JPanel panel = createPanel();

        // Panel - table
        panelTable = createPanelExt(0, 0, frame.getWidth(), frame.getHeight() - 80, Color.white, panel);

        // Panel - bottom
        JPanel panelBottom = createPanelExt(0, frame.getHeight() - 80, frame.getWidth(), 60, Color.lightGray, panel);

        // Open button
        createButton(panelBottom.getWidth() - 105, 5, 80, btnOpen, panelBottom);

        // Table
        createSearchTable(Mine.currentUser.getUsername(), garageName);

        // Add to frame
        frame.add(panel);
        frame.setVisible(true);

    }

    // Create table
    private void createSearchTable(String username, String garage) {

        // Get data
        Object[][] tableData = Files.loadGarageVehicles(username, garage);
        String[] tableTitles = { "Рег.номер", "Марка", "Модел", "Разход", "BHP", "Година на регистрация", "Дата на застраховка" };

        if (Files.carFileExists(username)) {

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

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
