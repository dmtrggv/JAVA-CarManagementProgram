package ui.frame;

import ui.Panels;
import use.Constants;
import javax.swing.*;
import java.awt.*;

public class AboutProgram extends Panels {

    public AboutProgram() {

        JDialog dialog = initializeDialog(-1, -1, 350, 170, Mine.frame, "Информация за приложението");
        JPanel panel = createPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel appNameLabel = new JLabel(Constants.app.APP_NAME + " v1.0.0", SwingConstants.CENTER);
        JLabel dbDeveloperLabel = new JLabel("База данни: " + Constants.app.DB_DEVELOPER + ", 2301261062", SwingConstants.CENTER);
        JLabel codeDeveloperLabel = new JLabel("Java код: " + Constants.app.DEVELOPER + ", 2101261032", SwingConstants.CENTER);
        JLabel devStudioLabel = new JLabel("Студио: " + Constants.app.DEV_STUDIO, SwingConstants.CENTER);

        panel.add(appNameLabel);
        panel.add(dbDeveloperLabel);
        panel.add(codeDeveloperLabel);
        panel.add(devStudioLabel);

        dialog.add(panel);
        dialog.setVisible(true);

    }

}