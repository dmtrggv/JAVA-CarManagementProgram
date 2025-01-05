package ui.dialog;

import ui.Panels;
import ui.frame.UserInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VerifyAdmin extends Panels implements ActionListener {

    String action;
    JDialog frame;
    JPasswordField password = new JPasswordField();
    JCheckBox viewPassword = new JCheckBox("показване");
    JButton checkPassword = new JButton("Потвърди");


    public VerifyAdmin(int x, int y, JFrame frameMain, String action) {

        this.action = action;

        // Create frame
        frame = initializeDialog(x, y, 300, 170, frameMain, "Потвърди admin парола");

        // Panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(null);

        // Password box
        password.setBounds(15, 10, 255, 30);
        password.setFocusable(true);

        // Checkbox
        viewPassword.setBounds(15, 45, 255, 30);
        viewPassword.addActionListener(this);
        viewPassword.setBackground(Color.white);

        // Search button
        checkPassword.setBounds(15, 80, 254, 30);
        checkPassword.setFocusable(true);
        checkPassword.addActionListener(this);

        panel.add(password);
        panel.add(viewPassword);
        panel.add(checkPassword);
        frame.add(panel);
        frame.setVisible(true);

    }

    private void actionRegister() {
        int xStart = frame.getX() + (frame.getWidth() / 2);
        int yStart = frame.getY() + (frame.getWidth() / 2);
        new UserInfo(xStart, yStart, true);
        frame.getOwner().dispose();
    }

    private void actionEditUser() {
        int xStart = frame.getX() + (frame.getWidth() / 2);
        int yStart = frame.getY() + (frame.getWidth() / 2);
        new UserInfo(xStart, yStart, false);
        frame.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewPassword) {
            if (password.getEchoChar() == (char) 0) {
                password.setEchoChar('*');  // Mask the password again
            } else {
                password.setEchoChar((char) 0);  // Show the password as text
            }
        } else if (e.getSource() == checkPassword) {
            if (new String(password.getPassword()).equals("gogimaster")) {
                if (frame.getOwner() != null) {
                         if (action.equals("register")) actionRegister();
                    else if (action.equals("edituser")) actionEditUser();
                }
            } else JOptionPane.showMessageDialog(frame, "Паролата е невалидна, пробвай със 'gogimaster'");
        }
    }

}
