package ui.frame;

import components.User;
import ui.dialog.VerifyAdmin;
import ui.Panels;
import use.Constants;
import use.DBFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Signin extends Panels implements ActionListener {

    private static boolean isLoggedIn = false;

    public static JFrame frame;
    JButton buttLanguage = new JButton("BG");
    JButton buttLogin = new JButton("Влез");
    JTextField txUsername = new JTextField();
    JPasswordField txPassword = new JPasswordField();
    JLabel creator = new JLabel("Създадено от " + Constants.app.DEVELOPER + ", 2101261032");
    JLabel copyright = new JLabel(Constants.app.APP_NAME + " v1.0.0 © Copyright " + Constants.app.RELEASE_YEAR);
    JCheckBox viewPassword = new JCheckBox("показване на паролата");

    public Signin() {

        // Create frame
        setPanelExist(true);
        frame = initializeFrame(xStart, yStart, 350, 350, "Влез в профил");

        // Panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(null);

        // Language button
        buttLanguage.setBounds(270, 10, 50, 30);
        buttLanguage.setFocusable(false);
        buttLanguage.addActionListener(this);

        // Username
        JLabel usernameLabel = new JLabel("Потребителско име:");
        usernameLabel.setBounds(20, 40, 150, 30);
        txUsername.setBounds(20, 65, 300, 35);
        txUsername.setFont(new Font("Sans Serif", 0, 15));
        txUsername.setFocusable(true);

        // Password
        JLabel passwordLabel = new JLabel("Парола:");
        passwordLabel.setBounds(20, 100, 100, 30);
        txPassword.setBounds(20, 125, 300, 35);
        txPassword.setFont(new Font("Sans Serif", 0, 15));

        // View password
        viewPassword.setBounds(20, 160, 300, 30);
        viewPassword.addActionListener(this);
        viewPassword.setBackground(Color.white);

        // Login button
        buttLogin.setBounds(17, 210, 300, 40);
        buttLogin.setFocusable(false);
        buttLogin.addActionListener(this);
        buttLogin.setFocusable(true);

        // Creator
        creator.setFont(new Font("Sans Serif", Font.BOLD, 12));
        creator.setForeground(Color.gray);

        // Copyright
        copyright.setFont(new Font("Sans Serif", Font.BOLD, 12));
        copyright.setForeground(Color.gray);

        // Set panel
        panel.add(buttLanguage);
        panel.add(usernameLabel);
        panel.add(txUsername);
        panel.add(passwordLabel);
        panel.add(txPassword);
        panel.add(viewPassword);
        panel.add(buttLogin);
        panel.add(creator);
        panel.add(copyright);
        frame.add(panel);
        frame.setVisible(true);
        updateCreatorPosition(panel);

    }

    private void updateCreatorPosition(JPanel panel) {

        int pW = panel.getWidth();
        int pH = panel.getHeight();
        int cW = creator.getPreferredSize().width;
        int cpW = copyright.getPreferredSize().width;

        // Change creator position
        creator.setBounds(((pW - cW) / 2) - 5, pH - 40, cW * 2, 15);
        copyright.setBounds(((pW - cpW) / 2) - 5, pH - 25, cpW * 2, 15);

        // Refresh
        panel.revalidate();
        panel.repaint();
    }

    //region Login status

    public static void setLoginStatus(boolean status) {
        isLoggedIn = status;
    }

    public static boolean getLoginStatus() {
        return isLoggedIn;
    }

    //endregion

    @Override
    public void actionPerformed(ActionEvent e) {

        // View password checkbox
        if (e.getSource() == viewPassword) {

            if (txPassword.getEchoChar() == (char) 0) txPassword.setEchoChar('*');
            else txPassword.setEchoChar((char) 0);

        }

        // Login
        if (e.getSource() == buttLogin) {

            if (!txUsername.getText().isEmpty() && !new String(txPassword.getPassword()).isEmpty()) {

                User user = DBFiles.user.loadUser(txUsername.getText(), new String(txPassword.getPassword()));

                if (user != null) {
                    new Mine(user);
                    frame.dispose();
                }

            } else JOptionPane.showMessageDialog(frame, "Полетата са задължителни!");

        }
        
    }
    
}
