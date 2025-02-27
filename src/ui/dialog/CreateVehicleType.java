package ui.dialog;

import components.Car;
import ui.Panels;
import ui.frame.CarInfo;
import ui.frame.Mine;
import ui.frame.Signin;
import use.DBFiles;
import use.Date;
import use.Files;
import use.RegistrationNumber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateVehicleType extends Panels implements ActionListener {

    JDialog frame;
    JButton createCar, createMotor;

    public CreateVehicleType(int x, int y) {

        // Create frame
        frame = initializeDialog(xStart, yStart, 300, 130, Mine.frame, "Добавяне на ново МПС");
        frame.setLocationRelativeTo(Signin.frame);

        // Panel
        JPanel panel = createPanel();

        // Create car button
        createCar = createButton(15, 15, 254, "Създай кола", panel);

        // Create motor button
        createMotor = createButton(15, 45, 254, "Създай мотор", panel);

        frame.add(panel);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Create car
        if (e.getSource() == createCar) {

            if (DBFiles.garage.userHasGarages()) {

                int xStart = frame.getX() + (frame.getWidth() / 2);
                int yStart = frame.getY() + (frame.getHeight() / 2);
                new CarInfo(xStart, yStart, null, true, false);

            } else {
                JOptionPane.showMessageDialog(frame, "Няма нито един намерен гараж!\nДобавете поне един гараж, преди да продължите.");
            }

        }

        // Create motor
        if (e.getSource() == createMotor) {
            JOptionPane.showMessageDialog(frame, "Все още няма функционалност за това :)");
        }

    }

}