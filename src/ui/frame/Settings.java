package ui.frame;

import components.Garage;
import ui.Panels;
import use.Constants;
import use.Files;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends Panels implements ActionListener {

    JDialog frame;
    JButton btnSave = new JButton("Запазване");
    JTextField txDbPath = new JTextField();

    public Settings(int x, int y) {

        // Create frame
        frame = initializeDialog(x, y, 280, 230, Mine.frame, "Моят гараж");

        // Panel
        JPanel panel = createPanel();

        // Garage name
        createTextField(
                panel, 15, 10, "Път до базата данни:",
                txDbPath, (Mine.currentUser.getDbPath() != null) ? Mine.currentUser.getDbPath() : null,
                true, Constants.filter.FILTER_NULL, -1, Constants.format.FORMAT_NULL
        );

        // Label
        createTextArea(
                panel, 15, 60, 60, "ВАЖНО!",
                new JTextArea(), "Внимавай как въвеждаш пътят към базата данни (Data base), защото ако го сгрешиш, няма да може да се изпълни нито една заявка :)",
                false, -1
        );

        // Save
        btnSave.setBounds(15, 150, 237, 25);
        btnSave.addActionListener(this);
        btnSave.setFocusable(true);
        panel.add(btnSave);

        // Add frame
        frame.add(panel);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnSave) {

            if (!txDbPath.getText().isEmpty() && txDbPath.getText() != null) {

                Mine.currentUser.setDbPath(txDbPath.getText());
                JOptionPane.showMessageDialog(frame, "Пътят към базата данни бе обновен успешно!");

            } else {

                JOptionPane.showMessageDialog(frame, "Пътят към базата данни, не трябва да е празен!");

            }

        }

    }

}
