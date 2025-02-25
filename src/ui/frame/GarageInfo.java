package ui.frame;

import components.Garage;
import ui.Panels;
import use.Constants;
import use.DBFiles;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GarageInfo extends Panels implements ActionListener {

    Garage currentGarage;
    JDialog frame;
    JButton btnEdit = new JButton("Редатиране");
    JButton btnSave = new JButton("Запазване");
    JButton btnSeeCars = new JButton("Виж МПС-тата");
    JTextField txGarageName = new JTextField();

    public GarageInfo(int x, int y, Garage garage, boolean editable) {

        // Garage
        currentGarage = garage;

        // Create frame
        frame = initializeDialog(x, y, 280, 185, Mine.frame, "Моят гараж");

        // Panel
        JPanel panel = createPanel();

        // Garage name
        createTextField(
                panel, 15, 10, "Име на гаража:",
                txGarageName, (garage != null) ? garage.getName() : null,
                editable, Constants.filter.FILTER_NULL, -1, Constants.format.FORMAT_NULL
        );

        // See vehicles
        btnSeeCars.setBounds(15, 60, 237, 25);
        btnSeeCars.addActionListener(this);
        btnSeeCars.setFocusable(true);
        btnSeeCars.setEnabled(!editable && garage != null);
        panel.add(btnSeeCars);

        // Save
        btnSave.setBounds(15, 110, 237, 25);
        btnSave.addActionListener(this);
        btnSave.setFocusable(true);
        if (editable) panel.add(btnSave);

        // Save
        btnEdit.setBounds(15, 110, 237, 25);
        btnEdit.addActionListener(this);
        btnEdit.setFocusable(true);
        if (!editable) panel.add(btnEdit);

        // Add frame
        frame.add(panel);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Save garage
        if (e.getSource() == btnSave) {

            if (
                (!txGarageName.getText().isEmpty()) &&
                (txGarageName.getText() != null) &&
                (!DBFiles.garage.garageExists(txGarageName.getText()))
            ) {

                Garage garage = new Garage(txGarageName.getText());

                if (currentGarage != null) {
                    DBFiles.garage.updateGarage(currentGarage, txGarageName.getText());
                } else DBFiles.garage.saveGarage(garage);

                int xStart = frame.getX() + (frame.getWidth() / 2);
                int yStart = frame.getY() + (frame.getHeight() / 2);

                new GarageInfo(xStart, yStart, garage, false);
                frame.dispose();

            } else {

                JOptionPane.showMessageDialog(frame, "Гаражат вече съществува, или името е празно!");

            }

        }

        // Edit garage
        if (e.getSource() == btnEdit) {

            JOptionPane.showMessageDialog(frame, "Важно! Когато смениш името на гаража, има шанс да изгубиш МПС-тата от него!");

            Garage garage = new Garage(txGarageName.getText());

            int xStart = frame.getX() + (frame.getWidth() / 2);
            int yStart = frame.getY() + (frame.getHeight() / 2);

            new GarageInfo(xStart, yStart, garage, true);
            frame.dispose();

        }

        // See cars in garage
        if (e.getSource() == btnSeeCars) {

            if (!txGarageName.getText().isEmpty() && txGarageName != null) {
                int xStart = frame.getX() + (frame.getWidth() / 2);
                int yStart = frame.getY() + (frame.getHeight() / 2);
                new GarageVehicleList(xStart, yStart, txGarageName.getText());
            } else {
                JOptionPane.showMessageDialog(frame, "Няма такъвгараж, или полето е празно :(");
            }

        }

    }

}
