package ui.dialog;

import components.Garage;
import ui.Panels;
import ui.frame.GarageInfo;
import ui.frame.Mine;
import use.DBFiles;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GarageSelector extends Panels implements ActionListener {

    JDialog frame;
    JComboBox cbGarageList;
    JButton btnOpen;

    public GarageSelector(int x, int y) {

        // Create frame
        frame = initializeDialog(x, y, 280, 145, Mine.frame, "Избери гараж");

        // Panel
        JPanel panel = createPanel();

        // Garage selector
        cbGarageList = new JComboBox<>();
        Object[][] garageData = DBFiles.garage.loadGarageList(false);
        for (Object[] garage : garageData) {
            cbGarageList.addItem((String) garage[0]);
        }
        createComboBox(
                panel, 15, 10, "Избери:",
                cbGarageList, null, true
        );

        // Open garage button
        createButton(15, 65, 237, btnOpen = new JButton("Отвори"), panel);

        // Add to frame
        frame.add(panel);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Open garage
        if (e.getSource() == btnOpen) {

            String selected = (cbGarageList.getSelectedItem() != null) ? (String) cbGarageList.getSelectedItem() : "";
            if (!selected.isEmpty()) {

                Garage garage = new Garage(selected);
                int xStart = frame.getX() + (frame.getWidth() / 2);
                int yStart = frame.getY() + (frame.getHeight() / 2);
                new GarageInfo(xStart, yStart, garage, false);

            }

        }

    }

}
