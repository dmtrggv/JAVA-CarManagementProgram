package ui.frame;

import components.Garage;
import org.h2.jdbcx.JdbcConnectionPool;
import ui.Panels;
import use.Constants;
import use.DBFiles;
import use.Files;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Settings extends Panels implements ActionListener {

    private final String info = "Използвай %projectfiles% за ../project-location/files." + "\n" +
                                "Въвеждай внимателно пътят към базата данни, защото иначе няма да работят заявките!";

    JDialog frame;
    JButton btnSave = new JButton("Запазване");
    JTextField txDbPath = new JTextField();
    JTextField txDbAdmin = new JTextField();
    JTextField txDbPass = new JTextField();

    public Settings(int x, int y) {

        // Create frame
        frame = initializeDialog(x, y, 280, 330, Mine.frame, "Моят гараж");

        // Panel
        JPanel panel = createPanel();

        // Data base path
        createTextField(
                panel, 15, 10, "Път до базата данни:",
                txDbPath, DBFiles.configuration.GetPath(),
                true, Constants.filter.FILTER_NULL, -1, Constants.format.FORMAT_NULL
        );

        // Data base admin
        createTextField(
                panel, 15, 60, "Админ:",
                txDbAdmin, DBFiles.configuration.GetAdmin(),
                true, Constants.filter.FILTER_NULL, -1, Constants.format.FORMAT_NULL
        );

        // Data base password
        createTextField(
                panel, 15, 110, "Парола:",
                txDbPass, DBFiles.configuration.GetPassword(),
                true, Constants.filter.FILTER_NULL, -1, Constants.format.FORMAT_NULL
        );

        // Info
        createTextArea(
                panel, 15, 160, 60, "ВАЖНО!",
                new JTextArea(), info,
                false, -1
        );

        // Save
        btnSave.setBounds(15, 250, 237, 25);
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

            if (
                (!txDbPath.getText().isEmpty() && txDbPath.getText() != null) &&
                (!txDbAdmin.getText().isEmpty() && txDbAdmin.getText() != null) &&
                (!txDbPass.getText().isEmpty() && txDbPass.getText() != null)
            ) {

                try {
                    DBFiles.configuration.Update(txDbPath.getText(), txDbAdmin.getText(), txDbPass.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(frame, "Данните за базата данни бе обновен успешно!");

            } else {

                JOptionPane.showMessageDialog(frame, "Пътят, админът и паролата на базата данни, не трябва да е празен!");

            }

        }

    }

}
