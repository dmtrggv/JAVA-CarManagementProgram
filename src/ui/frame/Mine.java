package ui.frame;

import components.User;
import ui.Panels;
import ui.dialog.CreateVehicleType;
import ui.dialog.GarageSelector;
import ui.dialog.VerifyAdmin;
import use.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mine extends Panels implements ActionListener {

    public static User currentUser;
    public static JFrame frame;

    //region Menu bar

    // Profile menu
    JMenu     menuProfile = new JMenu("Профил");
    JMenuItem menuProfileSwitchUser = new JMenuItem("Влиане в друг профил");
    JMenuItem menuProfileLogout = new JMenuItem("Излизане и затвори");
    JMenuItem menuProfileEditUser = new JMenuItem("Редактиране на потребителя");

    // Garage menu
    JMenu     menuGarage = new JMenu("Гараж");
    JMenuItem menuGarageAdd = new JMenuItem("Добавяне на гараж");
    JMenuItem menuGarageEdit = new JMenuItem("Отваряне на гараж");

    // Vehicles
    JMenu     menuVehicles = new JMenu("МПС");
    JMenuItem menuVehiclesAdd = new JMenuItem("Добавяне на ново МПС");
    JMenuItem menuVehiclesSearch = new JMenuItem("Търсене на МПС-та");

    // About
    JMenu     menuMore = new JMenu("Още");
    JMenuItem menuMoreSettings = new JMenuItem("Настройки");
    JMenuItem menuMoreAbout = new JMenuItem("Относно програмата");

    //endregion

    public Mine(User user) {

        // Create the user
        currentUser = new User(user);

        // Create frame
        frame = initializeFrame(-1, -1, 1100, 650, Constants.app.APP_NAME + " - " + currentUser.getUsername() + " - " + currentUser.getNameFull() + " - " + Constants.app.DEVELOPER + "@" + Constants.app.DEV_STUDIO);

        // Panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.CYAN);
        panel.setLayout(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        //region Menu - profile

        // Add listeners
        menuProfileSwitchUser.addActionListener(this);
        menuProfileLogout.addActionListener(this);
        menuProfileEditUser.addActionListener(this);

        // Add options
        menuProfile.add(menuProfileSwitchUser);
        menuProfile.add(menuProfileLogout);
        menuProfile.add(menuProfileEditUser);

        // Add menu
        menuBar.add(menuProfile);

        //endregion

        //region Menu - garage

        // Add listeners
        menuGarageAdd.addActionListener(this);
        menuGarageEdit.addActionListener(this);

        // Add options
        menuGarage.add(menuGarageAdd);
        menuGarage.add(menuGarageEdit);

        // Add menu
        menuBar.add(menuGarage);

        //endregion

        //region Menu - vehicles

        // Add listeners
        menuVehiclesAdd.addActionListener(this);
        menuVehiclesSearch.addActionListener(this);

        // Add options
        menuVehicles.add(menuVehiclesAdd);
        menuVehicles.add(menuVehiclesSearch);

        // Add more
        menuBar.add(menuVehicles);

        //endregion

        //region Menu - more

        // Add listeners
        menuMoreSettings.addActionListener(this);
        menuMoreAbout.addActionListener(this);

        // Add options
        if (currentUser.getUsername().equals("admin")) menuMore.add(menuMoreSettings);
        menuMore.add(menuMoreAbout);

        // Add menu
        menuBar.add(menuMore);

        //endregion

        frame.add(panel);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //region Profile menu

        // Switch user
        if (e.getSource() == menuProfileSwitchUser) {

            // Switch user
            new Signin();
            frame.dispose();

        }

        // Logout
        if (e.getSource() == menuProfileLogout) {

            // Close program
            frame.dispose();

        }

        // Edit user
        if (e.getSource() == menuProfileEditUser) {

            // Edit user
            int xStart = (frame.getX() + frame.getWidth()) / 2;
            int yStart = ((frame.getY() + frame.getHeight()) / 2) + 50;
            new VerifyAdmin(xStart, yStart, frame, "edituser");

        }

        //endregion

        //region Garage menu

        // Add garage
        if (e.getSource() == menuGarageAdd) {

            int xStart = frame.getX() + (frame.getWidth() / 2);
            int yStart = frame.getY() + (frame.getHeight() / 2);
            new GarageInfo(xStart, yStart, null, true);

        }

        // Edit garage
        if (e.getSource() == menuGarageEdit) {

            int xStart = frame.getX() + (frame.getWidth() / 2);
            int yStart = frame.getY() + (frame.getHeight() / 2);
            new GarageSelector(xStart, yStart);

        }

        //endregion

        //region Vehicles menu

        // Add vehicle
        if (e.getSource() == menuVehiclesAdd) {

            int xStart = frame.getX() + (frame.getWidth() / 2);
            int yStart = frame.getY() + (frame.getHeight() / 2);
            new CreateVehicleType(xStart, yStart);

        }

        // Search vehicle
        if (e.getSource() == menuVehiclesSearch) {

            int xStart = frame.getX() + (frame.getWidth() / 2);
            int yStart = frame.getY() + (frame.getHeight() / 2);
            new VehicleSearch(xStart, yStart);

        }

        //endregion

        //region More menu

        // Settings
        if (e.getSource() == menuMoreSettings) {

            int xStart = frame.getX() + (frame.getWidth() / 2);
            int yStart = frame.getY() + (frame.getHeight() / 2);
            new Settings(xStart, yStart);

        }

        //endregion

    }

}
