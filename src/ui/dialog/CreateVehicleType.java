package ui.dialog;

import components.Car;
import ui.Panels;
import ui.frame.CarInfo;
import ui.frame.Mine;
import ui.frame.Signin;
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
    JButton createCar = new JButton("Създай кола");
    JButton createMotor = new JButton("Създай мотор");

    public CreateVehicleType(int x, int y) {

        // Create frame
        frame = new JDialog(Mine.frame, "Добавяне на ново МПС");
        frame.setLocationRelativeTo(Signin.frame);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setBounds(x - 100, y - 65, 300, 130);
        frame.setResizable(false);

        // Panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(null);

        // Create car button
        createCar.setBounds(15, 15, 254, 30);
        createCar.setFocusable(true);
        createCar.addActionListener(this);

        // Create motor button
        createMotor.setBounds(15, 45, 254, 30);
        createMotor.setFocusable(true);
        createMotor.addActionListener(this);

        panel.add(createCar);
        panel.add(createMotor);
        frame.add(panel);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Create car
        if (e.getSource() == createCar) {

            if (Files.garageFileExists(Mine.currentUser.getUsername())) {
                Car car = Files.loadCar("102131", Mine.currentUser.getUsername());

                int xStart = frame.getX() + (frame.getWidth() / 2);
                int yStart = frame.getY() + (frame.getHeight() / 2);
                new CarInfo(xStart, yStart, car, true, false);
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