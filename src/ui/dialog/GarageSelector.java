package ui.dialog;

import components.Garage;
import ui.Panels;
import ui.frame.GarageInfo;
import ui.frame.Mine;
import use.Files;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GarageSelector extends Panels implements ActionListener {

    JDialog frame;
    JComboBox cbGarageList;
    JButton btnOpen = new JButton("Отвори");

    public GarageSelector(int x, int y) {

        // Create frame
        frame = initializeDialog(x, y, 280, 140, Mine.frame, "Избери гараж");

        // Panel
        JPanel panel = createPanel();

        // Garage selector
        cbGarageList = new JComboBox<>();
        Object[][] garageData = Files.loadGarages(Mine.currentUser.getUsername());
        for (Object[] garage : garageData) {
            cbGarageList.addItem((String) garage[0]);
        }
        createComboBox(
                panel, 15, 10, "Избери:",
                cbGarageList, null, true
        );

        // Open garage button
        btnOpen.setBounds(15, 65, 237, 25);
        btnOpen.addActionListener(this);
        btnOpen.setFocusable(true);
        panel.add(btnOpen);

        // Add to frame
        frame.add(panel);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnOpen) {

            String selected = (String) cbGarageList.getSelectedItem();
            if (!selected.isEmpty()) {
                int xStart = frame.getX() + (frame.getWidth() / 2);
                int yStart = frame.getY() + (frame.getHeight() / 2);
                Garage garage = new Garage(selected);
                new GarageInfo(xStart, yStart, garage, false);
            }
        }

    }

}
