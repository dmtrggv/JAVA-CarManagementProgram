package ui.frame;

import components.User;
import ui.Panels;
import use.*;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User information management UI.
 * Supports both adding new users and editing existing ones.
**/
public class UserInfo extends Panels implements ActionListener {

    JDialog frame;

    // Actions
    JButton btnSave, btnRegister;

    // Username & Password
    JTextField txUsername, txPassword, txPasswordConfirm;
    JComboBox<String> cbUsernameSelection;

    // Names & Info
    JTextField txNameFirst, txNameLast;
    JTextArea txInfo;

    // Address
    JTextField txAddressType, txAddressStreet, txAddressNumber, txAddressTown, txAddressCountry;

    /**
     * Constructor to initialize the user information interface.
    **/
    public UserInfo(int x, int y, boolean newuser) {

        // Create frame
        String title = (newuser) ? "Add New User" : Mine.currentUser.getNameFull() + " - " + Mine.currentUser.getUsername();
        frame = initializeDialog(x, y, 530, 420, Mine.frame, title);

        // Panel setup
        JPanel panel = createPanel();

        // Bottom panel setup
        JPanel panelBottom = createPanelExt(0, frame.getHeight() - 80, frame.getWidth(), 60, Color.lightGray, panel);

        // Username field
        createUsernameField(panel, newuser);

        // Password field
        String password = (newuser || Mine.currentUser.getPassword().isEmpty()) ? null : Mine.currentUser.getPassword();
        txPassword = initializeTextField(panel, 15, 60, "Парола:", password, true);

        // Confirm password field
        String passwordConfirm = (newuser || Mine.currentUser.getPassword().isEmpty()) ? null : Mine.currentUser.getPassword();
        txPasswordConfirm = initializeTextField(panel, 265, 60, "Потвърди паролата:", passwordConfirm, true);

        // First name field
        String firstname = newuser ? null : Mine.currentUser.getNameFirst();
        txNameFirst = initializeTextField(panel, 15, 110, "Име:", firstname, true);

        // Last name field
        String lastname = newuser ? null : Mine.currentUser.getNameLast();
        txNameLast = initializeTextField(panel, 265, 110, "Фамилия:", lastname, true);

        // Address fields (address not needed in the text field initialization)
        Address address = newuser ? null : Mine.currentUser.getAddress();
        createAddressField(panel, 15, 160, txAddressType = new JTextField("ул."), txAddressStreet = new JTextField(), txAddressNumber = new JTextField(), txAddressTown = new JTextField(), txAddressCountry = new JTextField("България"), address, true);

        // Info field
        String info = newuser ? null : Mine.currentUser.getInfo();
        createTextArea(panel, 15, 210, 100, "Допълнителна информация:", txInfo = new JTextArea(), info, true);

        // Buttons
        btnSave = createButton(panelBottom.getWidth() - 180, 5, 150, "Запази промените", (!newuser) ? panelBottom : null);
        btnRegister = createButton(panelBottom.getWidth() - 220, 5, 190, "Регистрирай потребителя", (newuser) ? panelBottom : null);

        // Add to frame
        frame.add(panel);
        frame.setVisible(true);

    }

    /**
     * Creates a text field with given parameters and initializes it.
    **/
    private JTextField initializeTextField(JPanel parentPanel, int x, int y, String title, String initialValue, boolean editable) {

        // Initialize text field
        JTextField textField = new JTextField();

        // Set initial value if provided
        if (initialValue != null && !initialValue.isEmpty()) {
            textField.setText(initialValue);
        } else {
            textField.setText("");
        }

        // Label setup
        JLabel label = new JLabel(title);
        label.setBounds(x, y, 200, 20);
        label.setForeground(editable ? Color.black : Color.darkGray);
        parentPanel.add(label);

        // Text field setup
        textField.setBounds(x, y + 20, 200, 25);
        textField.setEnabled(editable);
        textField.setDisabledTextColor(Color.darkGray);

        parentPanel.add(textField);

        return textField;
    }

    /**
     * Reload user info when selecting a user from the dropdown.
    **/
    private void reloadUserInfo(String username) {

        User userReload = DBFiles.user.loadUser(username);
        if (userReload != null) {

            txPassword.setText(userReload.getPassword());
            txPasswordConfirm.setText(userReload.getPassword());
            txNameFirst.setText(userReload.getNameFirst());
            txNameLast.setText(userReload.getNameLast());
            txAddressType.setText(userReload.getAddress().getStreetType());
            txAddressStreet.setText(userReload.getAddress().getStreetName());
            txAddressNumber.setText(userReload.getAddress().getStreetNumber());
            txAddressTown.setText(userReload.getAddress().getTown());
            txAddressCountry.setText(userReload.getAddress().getCountry());
            txInfo.setText(userReload.getInfo());

        }

    }

    /**
     * Creates username input field
    **/
    private void createUsernameField(JPanel parentPanel, boolean newuser) {

        if (newuser) {
            txUsername = initializeTextField(parentPanel, 15, 10, "Потребителско име:", Mine.currentUser.getUsername(), newuser);
        } else {

            cbUsernameSelection = new JComboBox<>();
            Object[][] usernameList = DBFiles.user.loadUsernamesList();
            for (Object[] user : usernameList) {
                cbUsernameSelection.addItem((String) user[0]);
            }

            createComboBox(parentPanel, 15, 10, "Потребител:", cbUsernameSelection, Mine.currentUser.getUsername(), Mine.currentUser.getUsername().equals("admin"));
            cbUsernameSelection.addActionListener(e -> reloadUserInfo((String) cbUsernameSelection.getSelectedItem()));

        }

    }

    /**
     * Creates a text area in the user interface.
    **/
    private void createTextArea(JPanel parentPanel, int x, int y, int height, String title, JTextArea inputbox, String input, boolean editable) {

        if (input != null && !input.isEmpty()) inputbox.setText(input);

        JLabel label = new JLabel(title);
        label.setBounds(x, y, 487, 20);
        label.setForeground(editable ? Color.black : Color.darkGray);

        inputbox.setEnabled(editable);
        inputbox.setDisabledTextColor(Color.darkGray);
        inputbox.setLineWrap(true);
        inputbox.setWrapStyleWord(true);
        inputbox.setRows(5);
        inputbox.setColumns(20);

        JScrollPane scrollPane = new JScrollPane(inputbox);
        scrollPane.setBounds(x, y + 20, 487, height);

        ((AbstractDocument) inputbox.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_NULL, -1));

        parentPanel.add(label);
        parentPanel.add(scrollPane);

    }

    /**
     * Creates address input fields.
    **/
    private void createAddressField(JPanel parentPanel, int x, int y, JTextField type, JTextField street, JTextField number, JTextField town, JTextField country, Address address, boolean editable) {

        // Get Address attributes
        if (address != null) {
            type.setText(address.getStreetType());
            street.setText(address.getStreetName());
            number.setText(address.getStreetNumber());
            town.setText(address.getTown());
            country.setText(address.getCountry());
        }

        JLabel labelStreet = new JLabel("Улица:");
        labelStreet.setBounds(x, y, 150, 20);
        labelStreet.setForeground(editable ? Color.black : Color.darkGray);
        parentPanel.add(labelStreet);

        type.setBounds(x, y + 20, 35, 25);
        type.setEnabled(editable);
        type.setDisabledTextColor(Color.darkGray);
        parentPanel.add(type);

        street.setBounds(x + 40, y + 20, 130, 25);
        street.setEnabled(editable);
        street.setDisabledTextColor(Color.darkGray);
        parentPanel.add(street);

        JLabel labelNumber = new JLabel("Номер:");
        labelNumber.setBounds(x + 175, y, 40, 20);
        labelNumber.setForeground(editable ? Color.black : Color.darkGray);
        parentPanel.add(labelNumber);

        number.setBounds(x + 175, y + 20, 40, 25);
        number.setEnabled(editable);
        number.setDisabledTextColor(Color.darkGray);
        parentPanel.add(number);

        JLabel labelTown = new JLabel("Град:");
        labelTown.setBounds(x + 220, y, 130, 20);
        labelTown.setForeground(editable ? Color.black : Color.darkGray);
        parentPanel.add(labelTown);

        town.setBounds(x + 220, y + 20, 130, 25);
        town.setEnabled(editable);
        town.setDisabledTextColor(Color.darkGray);
        parentPanel.add(town);

        JLabel labelCountry = new JLabel("Държава:");
        labelCountry.setBounds(x + 355, y, 133, 20);
        labelCountry.setForeground(editable ? Color.black : Color.darkGray);
        parentPanel.add(labelCountry);

        country.setBounds(x + 355, y + 20, 133, 25);
        country.setEnabled(editable);
        country.setDisabledTextColor(Color.darkGray);
        parentPanel.add(country);

    }

    /**
     * Handles user registration.
    **/
    private void handleUserRegistration() {

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
                txInfo.getText().isEmpty() ? null : txInfo.getText()
        );

        DBFiles.user.saveUser(user);
        JOptionPane.showMessageDialog(frame, "Регистрацията е успешна!\nДанни за влизане " + user.getUsername() + ", " + user.getPassword() + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
        new Signin();
        frame.dispose();

    }

    /**
     * Handles user update.
    **/
    private void handleUserUpdate() {

        if (
            (txPassword.getText().isEmpty() || txPassword.getText() == null) ||
            (!txPassword.getText().equals(txPasswordConfirm.getText()))
        ) {
            JOptionPane.showMessageDialog(frame, "Паролите не съвпадат, или са празни!");
            return;
        }

        User user = new User(
                (String) cbUsernameSelection.getSelectedItem(),
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
                txInfo.getText().isEmpty() ? null : txInfo.getText()
        );

        DBFiles.user.updateUser(user);
        if (Mine.currentUser.getUsername().equals(user.getUsername())) Mine.currentUser = user;

    }

    /**
     * Action listener for save or register buttons.
    **/
    @Override
    public void actionPerformed(ActionEvent e) {

        // Register new user
        if (e.getSource() == btnRegister) handleUserRegistration();

        // Save existing user
        if (e.getSource() == btnSave) handleUserUpdate();

    }

}