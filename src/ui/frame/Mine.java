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
    JMenu menuProfile = new JMenu("Профил");
    JMenuItem menuProfileSwitchUser = new JMenuItem("Влиане в друг профил");
    JMenuItem menuProfileLogout = new JMenuItem("Излизане и затвори");
    JMenuItem menuProfileEditUser = new JMenuItem("Редактиране на потребителя");
    JMenuItem menuProfileNewUser = new JMenuItem("Добавяне на нов потребител");

    // Garage menu
    JMenu menuGarage = new JMenu("Гараж");
    JMenuItem menuGarageAdd = new JMenuItem("Добавяне на гараж");
    JMenuItem menuGarageEdit = new JMenuItem("Отваряне на гараж");

    // Vehicles menu
    JMenu menuVehicles = new JMenu("МПС");
    JMenuItem menuVehiclesAdd = new JMenuItem("Добавяне на ново МПС");
    JMenuItem menuVehiclesSearch = new JMenuItem("Търсене на МПС-та");

    // About menu
    JMenu menuMore = new JMenu("Още");
    JMenuItem menuMoreSettings = new JMenuItem("Настройки");
    JMenuItem menuMoreAbout = new JMenuItem("Относно програмата");

    //endregion

    /**
     * Constructor for the Mine class.
     * Initializes the main user interface and menu options based on the provided user.
     *
     * @param user The current logged-in user
    **/
    public Mine(User user) {

        currentUser = new User(user);
        frame = initializeFrame(-1, -1, 1100, 650, Constants.app.APP_NAME + " - " + currentUser.getUsername() + " - " + currentUser.getNameFull() + " - " + Constants.app.DEVELOPER + "@" + Constants.app.DEV_STUDIO);
        frame.setResizable(true);
        
        JPanel panel = createPanel();
        panel.setBackground(Color.CYAN);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // Set up profile menu
        setupMenu(menuProfile, new JMenuItem[]{ menuProfileSwitchUser, menuProfileLogout, menuProfileEditUser, menuProfileNewUser }, menuBar);

        // Set up garage menu
        setupMenu(menuGarage, new JMenuItem[]{ menuGarageAdd, menuGarageEdit }, menuBar);

        // Set up vehicles menu
        setupMenu(menuVehicles, new JMenuItem[]{ menuVehiclesAdd, menuVehiclesSearch }, menuBar);

        // Set up more menu
        setupMenu(menuMore, new JMenuItem[]{ menuMoreSettings, menuMoreAbout }, menuBar);

        // Add to frame
        frame.add(panel);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);

    }

    /**
     * Helper method to set up menu options for a given menu.
     *
     * @param menu The menu to configure
     * @param items Array of items
     * @param menuBar The menu bar to add the menu to
    **/
    private void setupMenu(JMenu menu, JMenuItem[] items, JMenuBar menuBar) {

        for (int i = 0; i < items.length; i++) {
            items[i].addActionListener(this);
            menu.add(items[i]);
        }

        menuBar.add(menu);

    }

    /**
     * Handles the actions triggered by menu items.
     * Based on the event, performs the corresponding action like switching user, logging out, or opening dialogs.
    **/
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
            if (Mine.currentUser.getUsername().equals("admin")) new UserInfo(xStart, yStart, false);
            else new VerifyAdmin(xStart, yStart, frame, "edituser");

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
