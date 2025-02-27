package ui.frame;

import components.Car;
import ui.Panels;
import use.*;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class CarInfo extends Panels implements ActionListener {

    private final boolean newCar;

    JDialog frame;
    JButton             btnSave, btnEdit, btnDelete;
    JButton             btnGenerateRegNumber = new JButton("Генерирай");        // Registration number generator button
    JTextField          txRegNumber = new JTextField();                              // Registration number
    JTextField          txMileage = new JTextField();                                // Mileage
    JTextField          txLastOilChange = new JTextField();                          // Last oil change
    JTextField          txBrand = new JTextField();                                  // Brand
    JTextField          txModel = new JTextField();                                  // Model
    JTextField          txMpg = new JTextField();                                    // l/100km / MPG
    JTextField          txGasTank = new JTextField();                                // Gas tank size
    JComboBox<String>   cbFuelType;                                                  // Fuel type box
    JTextField          txPower = new JTextField();                                  // HP
    JComboBox<String>   cbGearboxType;                                               // Gear box type box
    JTextField          txYearProduction = new JTextField();                         // Year of production
    JTextField          txYearRegister = new JTextField();                           // Year of registration
    JTextField          txDateInsuranceD = new JTextField();                         // Insurance date
    JTextField          txDateInsuranceM = new JTextField();
    JTextField          txDateInsuranceY = new JTextField();
    JTextField          txDateGtpD = new JTextField();                               // GTP date
    JTextField          txDateGtpM = new JTextField();
    JTextField          txDateGtpY = new JTextField();
    JTextField          txDateWheelChangeD = new JTextField();                       // Wheel change date
    JTextField          txDateWheelChangeM = new JTextField();
    JTextField          txDateWheelChangeY = new JTextField();
    JTextArea           txCarInfo = new JTextArea();                                 // Car info
    JComboBox<String>   cbGarageList, cbUserList;

    public CarInfo(int x, int y, Car car, boolean editable, boolean startEmpty) {

        // Set editable
        newCar = (car == null || startEmpty) ? true : false;
        if (newCar) editable = true;

        // Title
        String frameTitle;
        if (!newCar) {
            if (editable) frameTitle = "Редактиране на кола";
            else frameTitle = "Информация за кола";
        } else frameTitle = "Добави кола";

        // Create frame
        frame = initializeDialog(x, y, 530, 600, Mine.frame, frameTitle);

        // Panel
        JPanel panel = createPanel();

        //region Km to oil change

        // Get index
        String OilChangeRemind = "";
        if (!newCar) {
            if (((car.getLastOilChange() + 10000) - car.getMileage()) >= 0) {
                OilChangeRemind = Format.toDigitGrouping((car.getLastOilChange() + 10000) - car.getMileage(), true) + " км.";
            } else {
                OilChangeRemind = "Прескочено с " + Format.toDigitGrouping(((car.getLastOilChange() + 10000) - car.getMileage()) * -1, true) + " км.";
            }
        }

        // Label
        JLabel labelOilChangeRemind = new JLabel("До масло:");
        labelOilChangeRemind.setFont(new Font("Sans Serif", Font.BOLD, 15));
        labelOilChangeRemind.setBounds(265, 30, 200, 25);
        labelOilChangeRemind.setForeground(Color.gray);

        // Textbox
        JTextField txOilChangeRemind = new JTextField(OilChangeRemind);
        txOilChangeRemind.setBounds(350, 30, 150, 25);
        txOilChangeRemind.setDisabledTextColor(Color.darkGray);
        txOilChangeRemind.setEnabled(false);

        // Add to panel
        panel.add(labelOilChangeRemind);
        panel.add(txOilChangeRemind);

        //endregion

        // Registration number
        createRegistrationPlate(
                panel, 15, 10, "Регистрационен номер:",
                txRegNumber, (car != null) ? car.getRegistrationNumber().toString() : null,
                btnGenerateRegNumber, editable && newCar
        );

        // Brand
        createTextField(
                panel, 15, 60, "Марка:",
                txBrand, (car != null) ? car.getBrand() : null,
                editable, Constants.filter.FILTER_NULL, 20, Constants.format.FORMAT_NULL
        );

        // Model
        createTextField(
                panel, 265, 60, "Модел:",
                txModel, (car != null) ? car.getModel() : null,
                editable, Constants.filter.FILTER_NULL, 20, Constants.format.FORMAT_NULL
        );

        // Mileage
        createTextField(
                panel, 15, 110, "Пробег в км:",
                txMileage, (!newCar) ? "" + car.getMileage() : null,
                editable, Constants.filter.FILTER_INTEGER, -1, Constants.format.DIGIT_GROUPING
        );

        // Last oil change
        createTextField(
                panel, 265, 110, "Последна смяна на маслко в км:",
                txLastOilChange, (!newCar) ? "" + car.getLastOilChange() : null,
                editable, Constants.filter.FILTER_INTEGER, -1, Constants.format.DIGIT_GROUPING
        );

        // MPG - l/100km
        createTextField(
                panel, 15, 160, "Разход на гориво (l/100km):",
                txMpg, (!newCar) ? "" + car.getMpg() : null,
                editable, Constants.filter.FILTER_DOUBLE, -1, Constants.format.FORMAT_NULL
        );

        // GasTank
        createTextField(
                panel, 265, 160, "Обем на резервоара (в литри)",
                txGasTank, (!newCar) ? "" + car.getGasTank() : null,
                editable, Constants.filter.FILTER_DOUBLE, -1, Constants.format.FORMAT_NULL
        );

        // Fuel type
        createComboBox(
                panel, 15, 210, "Вид гориво:",
                cbFuelType = new JComboBox<>(new String[]{ "Бензин", "Дизел", "Газ", "Бензин/Газ", "Електрически", "Водород" }),
                (car != null) ? car.getFuelType() : null, editable
        );

        // Engine power
        createTextField(
                panel, 265, 210, "Мощност на двигателя в bhp:",
                txPower, (!newCar) ? "" + car.getHp() : null,
                editable, Constants.filter.FILTER_INTEGER, -1, Constants.format.FORMAT_NULL
        );

        // Gearbox type
        createComboBox(
                panel, 15, 260, "Вид скоростна кутия:",
                cbGearboxType = new JComboBox<>(new String[]{ "Ръчна", "Автоматик", "CVT" }),
                (car != null) ? car.getGearboxType() : null, editable
        );

        // Production year
        createTextField(
                panel, 265, 260, "Година на производство:",
                txYearProduction, (!newCar) ? "" + car.getYearProduction() : null,
                editable, Constants.filter.FILTER_INTEGER, -1, Constants.format.FORMAT_NULL
        );

        // Registration year
        createTextField(
                panel, 15, 310, "Година на първа регистрация:",
                txYearRegister, (!newCar) ? "" + car.getYearRegistration() : null,
                editable, Constants.filter.FILTER_INTEGER, -1, Constants.format.FORMAT_NULL
        );

        // Insurance date
        createDateField(
                panel, 265, 310, "Дата на застраховка (Каско):",
                txDateInsuranceD, txDateInsuranceM, txDateInsuranceY, (car != null) ? car.getDateInsurance() : null, editable
        );

        // GTP date
        createDateField(
                panel, 15, 360, "Дата за ГТП:",
                txDateGtpD, txDateGtpM, txDateGtpY, (car != null) ? car.getDateGTP() : null, editable
        );

        // Wheel change date
        createDateField(
                panel, 265, 360, "Дата за смяна на гуми:",
                txDateWheelChangeD, txDateWheelChangeM, txDateWheelChangeY, (car != null) ? car.getDateWheelChange() : null, editable
        );

        // Garages
        cbGarageList = new JComboBox<>();
        Object[][] garageData = DBFiles.garage.loadGarageList(false);
        if (car != null && car.getGarage() != null && !DBFiles.car.getCarOwnerFullName(car.getRegistrationNumber().toString()).equals(Mine.currentUser.getNameFull())) cbGarageList.addItem(car.getGarage());
        for (Object[] garage : garageData) {
            cbGarageList.addItem((String) garage[0]);
        }
        createComboBox(
                panel, 265, 410, "Добави към гараж:",
                cbGarageList, (car != null) ? car.getGarage() : null, editable
        );

        // User
        cbUserList = new JComboBox<>();
        if (car != null && car.getRegistrationNumber() != null)
            cbUserList.addItem(DBFiles.car.getCarOwnerFullName(car.getRegistrationNumber().toString()));
        else cbUserList.addItem(Mine.currentUser.getNameFull());
        createComboBox(
                panel, 265, 460, "Собственик:",
                cbUserList, null, false
        );

        // Car info
        createTextArea(
                panel, 15, 410, 115, "Описание за колата:",
                txCarInfo, (car != null) ? car.getInfo() : null, editable, -1
        );

        //region Buttons

        // Save button
        btnSave = createButton(355, 515, 147, "Запази промените", (editable) ? panel : null);

        // Edit button
        btnEdit = createButton(355, 515, 147, "Редактирай колата", (!editable) ? panel : null);

        // Delete button
        btnDelete = createButton(266, 515, 80, "Изтрий", panel);
        btnDelete.setEnabled(!newCar);

        //endregion

        // Add frame
        frame.add(panel);
        frame.setVisible(true);

    }

    // Create Registration number box
    private void createRegistrationPlate(JPanel panel, int x, int y, String title, JTextField inputbox, String inputboxString, JButton button, boolean editable) {

        // Get index
        if (inputboxString != null && (!inputboxString.isEmpty())) inputbox.setText(inputboxString);

        // Label
        JLabel label = new JLabel(title);
        label.setFont(labelFont);
        label.setBounds(x, y, 200, 20);
        label.setForeground((editable) ? Color.black : Color.darkGray);

        // Textbox
        inputbox.setBounds(x, y + 20, 130, 25);
        inputbox.setEnabled(editable);
        inputbox.setDisabledTextColor(Color.darkGray);

        // Button
        button.setBounds(x + 135, y + 20, 100, 24);
        button.addActionListener(this);
        button.setEnabled(editable);

        // Textbox filter
        ((AbstractDocument) inputbox.getDocument()).setDocumentFilter(new NumericDocumentFilter(Constants.filter.FILTER_NULL, 10));

        panel.add(label);
        panel.add(inputbox);
        panel.add(button);
    }

    // Create delete car confirmation box
    public static void deleteCarConfirmation(String registrationNumber, JDialog frame) {

        if (DBFiles.car.carCanModify(registrationNumber, Mine.currentUser.getUsername())) {

            int confirmation = JOptionPane.showConfirmDialog(frame,
                    "Сигурен ли си, че искаш да изтриеш " + registrationNumber + "?",
                    "Изтриване на кола?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmation == JOptionPane.YES_OPTION) {

                DBFiles.car.deleteCar(registrationNumber);
                frame.dispose();

            }

        } else JOptionPane.showMessageDialog(frame, "Нямаш правата да изтриваш тази кола!");

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Save car
        if (e.getSource() == btnSave) {

            // If car can be saved
            boolean canSave = true;
            if (newCar) canSave = !DBFiles.car.carExists(txRegNumber.getText());

            //region Check system

            boolean checkStrings = (
                (!txBrand.getText().isEmpty()) &&
                (!txModel.getText().isEmpty()) &&
                (!txRegNumber.getText().isEmpty()) &&
                (!txMileage.getText().isEmpty()) &&
                (!txLastOilChange.getText().isEmpty())
            );

            boolean checkDatesAgo = (
                (!txDateGtpY.getText().isEmpty()) &&
                (!txDateGtpM.getText().isEmpty()) &&
                (!txDateGtpD.getText().isEmpty()) &&
                (!new Date(Integer.parseInt(txDateGtpY.getText()), Integer.parseInt(txDateGtpM.getText()), Integer.parseInt(txDateGtpD.getText())).isInFuture()) &&
                (!txDateInsuranceY.getText().isEmpty()) &&
                (!txDateInsuranceM.getText().isEmpty()) &&
                (!txDateInsuranceD.getText().isEmpty()) &&
                (!new Date(Integer.parseInt(txDateInsuranceY.getText()), Integer.parseInt(txDateInsuranceM.getText()), Integer.parseInt(txDateInsuranceD.getText())).isInFuture())
            );

            boolean checkDatesFuture = (
                (!txDateWheelChangeY.getText().isEmpty()) &&
                (!txDateWheelChangeM.getText().isEmpty()) &&
                (!txDateWheelChangeD.getText().isEmpty()) &&
                (new Date(Integer.parseInt(txDateWheelChangeY.getText()), Integer.parseInt(txDateWheelChangeM.getText()), Integer.parseInt(txDateWheelChangeD.getText())).isInFuture())
            );

            boolean checkYears = (
                (!txYearRegister.getText().isEmpty() && Integer.parseInt(txYearRegister.getText()) >= 0) &&
                (!txYearProduction.getText().isEmpty() && Integer.parseInt(txYearProduction.getText()) >= 0)
            );

            boolean checkNumbers = (
                (!txMpg.getText().isEmpty() && (Double.parseDouble(txMpg.getText()) > 0 && Double.parseDouble(txMpg.getText()) <= 9999999.99)) &&
                (!txGasTank.getText().isEmpty() && (Double.parseDouble(txGasTank.getText()) > 0 && Double.parseDouble(txGasTank.getText()) <= 9999999.99)) &&
                (!txPower.getText().isEmpty() && Integer.parseInt(txPower.getText()) > 0)
            );

            //endregion

            if (canSave && checkStrings && checkYears && checkDatesAgo && checkDatesFuture && checkNumbers) {

                // Create object
                Car car = new Car(
                        (!txBrand.getText().isEmpty()) ? txBrand.getText() : "",
                        (!txModel.getText().isEmpty()) ? txModel.getText() : "",
                        (!txRegNumber.getText().isEmpty()) ? new RegistrationNumber(txRegNumber.getText()) : null,
                        (!txMileage.getText().isEmpty()) ? Integer.parseInt(txMileage.getText()) : 0,
                        (!txLastOilChange.getText().isEmpty()) ? Integer.parseInt(txLastOilChange.getText()) : 0,
                        (!txMpg.getText().isEmpty()) ? Double.parseDouble(txMpg.getText()) : 0,
                        (!txGasTank.getText().isEmpty()) ? Double.parseDouble(txGasTank.getText()) : 0,
                        (String) cbFuelType.getSelectedItem(),
                        (!txPower.getText().isEmpty()) ? Integer.parseInt(txPower.getText()) : 0,
                        (String) cbGearboxType.getSelectedItem(),
                        (!txYearProduction.getText().isEmpty()) ? Integer.parseInt(txYearProduction.getText()) : 0,
                        (!txYearRegister.getText().isEmpty()) ? Integer.parseInt(txYearRegister.getText()) : 0,
                        new Date(
                                (!txDateInsuranceY.getText().isEmpty()) ? Integer.parseInt(txDateInsuranceY.getText()) : 0,
                                (!txDateInsuranceM.getText().isEmpty()) ? Integer.parseInt(txDateInsuranceM.getText()) : 0,
                                (!txDateInsuranceD.getText().isEmpty()) ? Integer.parseInt(txDateInsuranceD.getText()) : 0
                        ),
                        new Date(
                                (!txDateGtpY.getText().isEmpty()) ? Integer.parseInt(txDateGtpY.getText()) : 0,
                                (!txDateGtpM.getText().isEmpty()) ? Integer.parseInt(txDateGtpM.getText()) : 0,
                                (!txDateGtpD.getText().isEmpty()) ? Integer.parseInt(txDateGtpD.getText()) : 0
                        ),
                        new Date(
                                (!txDateWheelChangeY.getText().isEmpty()) ? Integer.parseInt(txDateWheelChangeY.getText()) : 0,
                                (!txDateWheelChangeM.getText().isEmpty()) ? Integer.parseInt(txDateWheelChangeM.getText()) : 0,
                                (!txDateWheelChangeD.getText().isEmpty()) ? Integer.parseInt(txDateWheelChangeD.getText()) : 0
                        ),
                        (!txCarInfo.getText().isEmpty()) ? txCarInfo.getText() : "",
                        (String) cbGarageList.getSelectedItem()
                );

                // Save into Data base
                if (newCar) DBFiles.car.saveCar(car);
                else DBFiles.car.updateCar(car);

                // Restart frame
                int xStart = frame.getX() + (frame.getWidth() / 2);
                int yStart = frame.getY() + (frame.getHeight() / 2);
                new CarInfo(xStart, yStart, car, false, false);
                frame.dispose();

            } else {

                //region Error message

                String notFull =        "- Всички полета за задължителни!" + "\n";
                String notDatesAgo =    (!checkDatesAgo) ? "- Датите за Каско и за ГТП не трябва да са в бъдещето!" + "\n" : "";
                String notDatesFuture = (!checkDatesFuture) ? "- Датата за мяна на гуми, не трябва да е минала!" + "\n" : "";
                String notYears =       (!checkYears) ? "- Годините не могат да са отрицателни числа!" + "\n" : "";
                String notNumbers =     (!checkNumbers) ? "- Числата рябва да са задължително по-големи от нула!" + "\n" + "- Резервоарът и Разхода не могата да са по-големи от 9999999.99!" : "";

                JOptionPane.showMessageDialog(frame, notFull + notDatesAgo + notDatesFuture + notYears + notNumbers);

                //endregion

            }

        }

        // Edit car
        if (e.getSource() == btnEdit) {

            if (DBFiles.car.carCanModify(txRegNumber.getText(), Mine.currentUser.getUsername())) {

                Car car = new Car(
                        (!txBrand.getText().isEmpty()) ? txBrand.getText() : "",
                        (!txModel.getText().isEmpty()) ? txModel.getText() : "",
                        (!txRegNumber.getText().isEmpty()) ? new RegistrationNumber(txRegNumber.getText()) : null,
                        (!txMileage.getText().isEmpty()) ? Integer.parseInt(txMileage.getText()) : 0,
                        (!txLastOilChange.getText().isEmpty()) ? Integer.parseInt(txLastOilChange.getText()) : 0,
                        (!txMpg.getText().isEmpty()) ? Double.parseDouble(txMpg.getText()) : 0,
                        (!txGasTank.getText().isEmpty()) ? Double.parseDouble(txGasTank.getText()) : 0,
                        (String) cbFuelType.getSelectedItem(),
                        (!txPower.getText().isEmpty()) ? Integer.parseInt(txPower.getText()) : 0,
                        (String) cbGearboxType.getSelectedItem(),
                        (!txYearProduction.getText().isEmpty()) ? Integer.parseInt(txYearProduction.getText()) : 0,
                        (!txYearRegister.getText().isEmpty()) ? Integer.parseInt(txYearRegister.getText()) : 0,
                        new Date(
                                (!txDateInsuranceY.getText().isEmpty()) ? Integer.parseInt(txDateInsuranceY.getText()) : 0,
                                (!txDateInsuranceM.getText().isEmpty()) ? Integer.parseInt(txDateInsuranceM.getText()) : 0,
                                (!txDateInsuranceD.getText().isEmpty()) ? Integer.parseInt(txDateInsuranceD.getText()) : 0
                        ),
                        new Date(
                                (!txDateGtpY.getText().isEmpty()) ? Integer.parseInt(txDateGtpY.getText()) : 0,
                                (!txDateGtpM.getText().isEmpty()) ? Integer.parseInt(txDateGtpM.getText()) : 0,
                                (!txDateGtpD.getText().isEmpty()) ? Integer.parseInt(txDateGtpD.getText()) : 0
                        ),
                        new Date(
                                (!txDateWheelChangeY.getText().isEmpty()) ? Integer.parseInt(txDateWheelChangeY.getText()) : 0,
                                (!txDateWheelChangeM.getText().isEmpty()) ? Integer.parseInt(txDateWheelChangeM.getText()) : 0,
                                (!txDateWheelChangeD.getText().isEmpty()) ? Integer.parseInt(txDateWheelChangeD.getText()) : 0
                        ),
                        (!txCarInfo.getText().isEmpty()) ? txCarInfo.getText() : "",
                        (String) cbGarageList.getSelectedItem()
                );

                int xStart = frame.getX() + (frame.getWidth() / 2);
                int yStart = frame.getY() + (frame.getHeight() / 2);
                new CarInfo(xStart, yStart, car, true, false);
                frame.dispose();

            } else JOptionPane.showMessageDialog(frame, "Нямаш правата да редактираш тази кола!");

        }

        // Delete car
        if (e.getSource() == btnDelete) {

            if (txRegNumber.getText() != null && !txRegNumber.getText().isEmpty()) {
                deleteCarConfirmation(txRegNumber.getText(), frame);
            }

        }

        // Random registration number
        if (e.getSource() == btnGenerateRegNumber) {

            String regFirst = "";
            String regMid = "";
            String regLast = "";

            int maxAttempts = 100; // Max tries
            int attempts = 0;
            boolean unique = false;

            do {

                regFirst = new String[]{"E", "PB", "A", "CB", "C", "CA", "TX", "B"}[new Random().nextInt(8)];
                regMid = 1000 + (int) (Math.random() * 9000) + "";
                regLast = new String[]{"EX", "PB", "AX", "CB", "CX", "CA", "TX", "BA", "OX", "HA"}[new Random().nextInt(10)];

                String registrationNumber = regFirst + regMid + regLast;

                if (!DBFiles.car.carExists(registrationNumber)) {
                    unique = true;
                }

                attempts++;

            } while (!unique && attempts < maxAttempts);

            Car car = new Car(new RegistrationNumber(regFirst + regMid + regLast));

            int xStart = frame.getX() + (frame.getWidth() / 2);
            int yStart = frame.getY() + (frame.getHeight() / 2);
            new CarInfo(xStart, yStart, car, true, true);
            frame.dispose();

        }

    }

}