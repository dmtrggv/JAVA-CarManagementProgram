package ui.frame;

import components.User;
import ui.Panels;
import use.Address;
import use.Constants;
import use.Files;
import use.NumericDocumentFilter;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInfo extends Panels implements ActionListener {

    JDialog frame;
    JButton btnSave = new JButton("Запаи промените");
    JButton btnRegister = new JButton("Регистрирай поребителя");
    JTextField txUsername = new JTextField();
    JTextField txPassword = new JTextField();
    JTextField txPasswordConfirm = new JTextField();
    JTextField txNameFirst = new JTextField();
    JTextField txNameLast = new JTextField();
    JTextField txAddressType = new JTextField("ул.");
    JTextField txAddressStreet = new JTextField();
    JTextField txAddressNumber = new JTextField();
    JTextField txAddressTown = new JTextField();
    JTextField txAddressCountry = new JTextField();
    JTextArea txInfo = new JTextArea();

    public UserInfo(int x, int y, boolean newuser) {

        // Create frame
        String title = (newuser) ? "Добавяне на нов потребител" : Mine.currentUser.getNameFull() + " - " + Mine.currentUser.getUsername();
        frame = initializeDialog(x, y, 530, 420, Mine.frame, title);

        // Panel
        JPanel panel = createPanel();

        // Panel - bottom
        JPanel panelBottom = createPanelExt(0, frame.getHeight() - 80, frame.getWidth(), 60, Color.lightGray, panel);

        // Username
        String username = null;
        if (!newuser && !Mine.currentUser.getUsername().isEmpty() && Mine.currentUser.getUsername() != null) {
            username = Mine.currentUser.getUsername();
        }
        createTextField(
                panel, 15, 10, "Потребителско име:",
                txUsername, username,
                newuser, Constants.filter.FILTER_NULL, -1, Constants.format.FORMAT_NULL
        );

        // Password
        String password = null;
        if (!newuser && !Mine.currentUser.getPassword().isEmpty() && Mine.currentUser.getPassword() != null) {
            password = Mine.currentUser.getPassword();
        }
        createTextField(
                panel, 15, 60, "Парола:",
                txPassword, password,
                true, Constants.filter.FILTER_NULL, -1, Constants.format.FORMAT_NULL
        );

        // Password
        String passwordConfirm = null;
        if (!newuser && !Mine.currentUser.getPassword().isEmpty() && Mine.currentUser.getPassword() != null) {
            passwordConfirm = Mine.currentUser.getPassword();
        }
        createTextField(
                panel, 265, 60, "Потвърди паролата:",
                txPasswordConfirm, passwordConfirm,
                true, Constants.filter.FILTER_NULL, -1, Constants.format.FORMAT_NULL
        );

        // First name
        String firstname = null;
        if (!newuser && !Mine.currentUser.getNameFirst().isEmpty() && Mine.currentUser.getNameFirst() != null) {
            firstname = Mine.currentUser.getNameFirst();
        }
        createTextField(
                panel, 15, 110, "Име:",
                txNameFirst, firstname,
                true, Constants.filter.FILTER_NULL, -1, Constants.format.FORMAT_NULL
        );

        // Last name
        String lastname = null;
        if (!newuser && !Mine.currentUser.getNameLast().isEmpty() && Mine.currentUser.getNameLast() != null) {
            lastname = Mine.currentUser.getNameLast();
        }
        createTextField(
                panel, 265, 110, "Фамилия:",
                txNameLast, lastname,
                true, Constants.filter.FILTER_NULL, -1, Constants.format.FORMAT_NULL
        );

        // Address
        Address address = null;
        if (!newuser && Mine.currentUser.getAddress() != null) {
            address = Mine.currentUser.getAddress();
        }
        createAddressField(panel, 15, 160, txAddressType, txAddressStreet, txAddressNumber, txAddressTown, txAddressCountry, address, true);

        // Info
        String info = null;
        if (!newuser && Mine.currentUser.getInfo() != null) {
            info = Mine.currentUser.getInfo();
        }
        createTextArea(panel, 15, 210, 100, "Допълнителна информация и бележки за потребителя:", txInfo, info, true);

        // Open button
        if (!newuser) createButton(panelBottom.getWidth() - 180, 5, 150, btnSave, panelBottom);

        // Register button
        if (newuser) createButton(panelBottom.getWidth() - 220, 5, 190, btnRegister, panelBottom);

        // Add to frame
        frame.add(panel);
        frame.setVisible(true);

    }

    private void createTextArea(JPanel parentPanel, int x, int y, int height, String title, JTextArea inputbox, String input, boolean editable) {

        // Get index and set text if provided
        if (input != null && !input.isEmpty()) {
            inputbox.setText(input);
        }

        // Label
        JLabel label = new JLabel(title);
        label.setBounds(x, y, 487, 20);
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
        scrollPane.setBounds(x, y + 20, 487, height);  // Set the position and size of the scrollPane

        // Textbox filter
        ((AbstractDocument) inputbox.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_NULL, -1));

        parentPanel.add(label);
        parentPanel.add(scrollPane);

    }

    private void createAddressField(JPanel parentPanel, int x, int y, JTextField type, JTextField street, JTextField number, JTextField town, JTextField counry, Address address, boolean editable) {

        // Get index
        if (address != null) {
            type.setText(address.getStreetType());
            street.setText(address.getStreetName());
            number.setText(address.getStreetNumber());
            town.setText(address.getTown());
            counry.setText(address.getCountry());
        }

        // Street label
        JLabel labelStreet = new JLabel("Улица:");
        labelStreet.setFont(labelFont);
        labelStreet.setBounds(x, y, 150, 20);
        labelStreet.setForeground((editable) ? Color.black : Color.darkGray);
        parentPanel.add(labelStreet);

        // Street type
        type.setBounds(x, y + 20, 35, 25);
        type.setEnabled(editable);
        type.setDisabledTextColor(Color.darkGray);
        parentPanel.add(type);

        // Street textbox
        street.setBounds(x + 40, y + 20, 130, 25);
        street.setEnabled(editable);
        street.setDisabledTextColor(Color.darkGray);
        parentPanel.add(street);

        // Number label
        JLabel labelNumber = new JLabel("No:");
        labelNumber.setFont(labelFont);
        labelNumber.setBounds(x + 175, y, 40, 20);
        labelNumber.setForeground((editable) ? Color.black : Color.darkGray);
        parentPanel.add(labelNumber);

        // Number textbox
        number.setBounds(x + 175, y + 20, 40, 25);
        number.setEnabled(editable);
        number.setDisabledTextColor(Color.darkGray);
        parentPanel.add(number);

        // Town label
        JLabel labelTown = new JLabel("Град:");
        labelTown.setFont(labelFont);
        labelTown.setBounds(x + 220, y, 130, 20);
        labelTown.setForeground((editable) ? Color.black : Color.darkGray);
        parentPanel.add(labelTown);

        // Town textbox
        town.setBounds(x + 220, y + 20, 130, 25);
        town.setEnabled(editable);
        town.setDisabledTextColor(Color.darkGray);
        parentPanel.add(town);

        // Country label
        JLabel labelCountry = new JLabel("Държава:");
        labelCountry.setFont(labelFont);
        labelCountry.setBounds(x + 355, y, 133, 20);
        labelCountry.setForeground((editable) ? Color.black : Color.darkGray);
        parentPanel.add(labelCountry);

        // Country textbox
        counry.setBounds(x + 355, y + 20, 133, 25);
        counry.setEnabled(editable);
        counry.setDisabledTextColor(Color.darkGray);
        parentPanel.add(counry);

    }

    private void handleUserRegistration() {
        // Create a new User object
        User user = new User(
                txUsername.getText(),
                txPassword.getText(),
                txNameFirst.getText(),
                txNameLast.getText(),
                new Address(
                        txAddressType.getText(),
                        txAddressStreet.getText(),
                        txAddressNumber.getText(),
                        txAddressTown.getText(),
                        txAddressCountry.getText()
                ),
                (!txInfo.getText().isEmpty() && txInfo.getText() != null) ? txInfo.getText() : null
        );

        // Save user and transition to sign-in screen
        Files.saveUser(user);
        JOptionPane.showMessageDialog(frame, "Регистрацията е успешна!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        new Signin();
        frame.dispose();
    }

    private void handleUserUpdate() {
        // Create a new User object
        User user = new User(
                txUsername.getText(),
                txPassword.getText(),
                txNameFirst.getText(),
                txNameLast.getText(),
                new Address(
                        txAddressType.getText(),
                        txAddressStreet.getText(),
                        txAddressNumber.getText(),
                        txAddressTown.getText(),
                        txAddressCountry.getText()
                ),
                (!txInfo.getText().isEmpty() && txInfo.getText() != null) ? txInfo.getText() : null
        );

        // Save user and transition to sign-in screen
        Files.saveUser(user);
        Mine.currentUser = user;
        JOptionPane.showMessageDialog(frame, "Промените са успешни!", "Успех", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isValidInput() {
        if (
            (txUsername.getText().isEmpty()) ||
            (txPassword.getText().isEmpty()) ||
            (txPasswordConfirm.getText().isEmpty()) ||
            (txNameFirst.getText().isEmpty()) ||
            (txNameLast.getText().isEmpty()) ||
            (txAddressStreet.getText().isEmpty()) ||
            (txAddressNumber.getText().isEmpty()) ||
            (txAddressTown.getText().isEmpty()) ||
            (txAddressCountry.getText().isEmpty())
        ) {
            return false;
        }
        if (!txPassword.getText().equals(txPasswordConfirm.getText())) {
            JOptionPane.showMessageDialog(frame, "Паролите не съвпадат.", "Грешка", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister) {
            if (isValidInput()) {
                if (txUsername.getText() != null && Files.usernameExists(txUsername.getText())) {
                    JOptionPane.showMessageDialog(frame, "Това потребителско име вече съществува!\nМоля, избери друго уникално име за този профил :)");
                } else handleUserRegistration();
            } else {
                JOptionPane.showMessageDialog(frame, "Моля, попълнете всички полета и проверете дали паролите съвпадат.", "Грешка", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnSave) {
            if (isValidInput()) {
                handleUserUpdate();
            } else {
                JOptionPane.showMessageDialog(frame, "Моля, попълнете всички полета и проверете дали паролите съвпадат.", "Грешка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
