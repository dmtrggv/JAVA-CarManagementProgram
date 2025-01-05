package use;

import components.Car;
import components.Garage;
import components.User;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;

public class Files {

    // This Class is from the Invisible Library project - check it out

    // Get working directory
    public static String getFileDirectory() {
        String projectRoot = System.getProperty("user.dir");
        String srcPath = Paths.get(projectRoot, "src").toString();
        String filePath = Paths.get(srcPath, "files").toString();

        return filePath;
    }

    //region User - save / load / find

    // If user exists
    public static boolean usernameExists(String username) {

        File file = new File(getFileDirectory() + "\\users.txt");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(";\\s*");
                    String usernameCheck = fields[0];
                    if (usernameCheck.equals(username)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;

    }

    // Save user info
    public static void saveUser(User user) {

        // File path
        String filePath = getFileDirectory() + "\\users.txt";

        // Read all users from the file
        List<String> usersList = new ArrayList<>();
        boolean usernameExists = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                usersList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Prepare the new user data
        String userData = String.format(
                "%s; %s; %s; %s; %s; %s%n",
                user.getUsername(), user.getPassword(), user.getNameFirst(), user.getNameLast(),
                (user.getAddress() != null) ? user.getAddress().toString() : "N/A",
                (user.getInfo() != null) ? user.getInfo().replace("\n", "\\n") : "N/A"
        );

        // Check if the username already exists
        for (int i = 0; i < usersList.size(); i++) {
            String currentUserData = usersList.get(i);
            String currentUsername = currentUserData.split(";")[0].trim(); // Assuming usernames are the first part of the string
            if (currentUsername.equals(user.getUsername())) {
                usersList.set(i, userData);
                usernameExists = true;
                break;
            }
        }

        // If the username doesn't exist
        if (!usernameExists) {
            usersList.add(userData);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String userLine : usersList) {
                writer.write(userLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Load user info
    public static User loadUser(String username, String password) {

        // File path
        String filePath = getFileDirectory() + "\\users.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {

                // Split
                String[] parts = line.split("; ");

                // Username and password
                String fileUsername = parts[0];
                String filePassword = parts[1];

                // If username and password match
                if (fileUsername.equals(username) && filePassword.equals(password)) {
                    if (parts.length == 6) {
                        String nameFirst = parts[2];
                        String nameLast = parts[3];
                        Address address = new Address(parts[4]);
                        String info = parts[5].replace("//n", "/n");
                        return new User(fileUsername, filePassword, nameFirst, nameLast, address, info);
                    } else {
                        String nameFirst = parts[2];
                        String nameLast = parts[3];
                        Address address = new Address(parts[4]);
                        return new User(fileUsername, filePassword, nameFirst, nameLast, address, null);
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // User doesn't exists
        return null;

    }

    //endregion

    //region Cars - save / load / find

    // If cars file exists
    public static boolean carFileExists(String user) {

        if (!user.isEmpty() && user != null) {
            File file = new File(getFileDirectory() + "\\" + user + ".cars");
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } else return false;

    }

    // If car exists
    public static boolean carExists(String user, String registrationNumber) {

        File file = new File(getFileDirectory() + "\\" + user + ".cars");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(";\\s*");
                    String registrationNumberCheck = fields[0];
                    if (registrationNumberCheck.equals(registrationNumber)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;

    }

    // Save car info
    public static void saveCar(Car car, String user) {

        File file = new File(getFileDirectory() + "\\" + user + ".cars");

        if (!file.exists()) {
            try {
                file.createNewFile(); // Create file
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        List<String> fileContent = new ArrayList<>();
        boolean carExists = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] carData = line.split("; ");
                String regNumber = carData[0];
                if (regNumber.equals(car.getRegistrationNumber().toString())) {
                    // Replace car info
                    String updatedCarData = String.format(
                            "%s; %s; %s; %d; %d; %.2f; %.2f; %s; %d; %s; %d; %d; %s; %s; %s; %s; %s",
                            car.getRegistrationNumber(), car.getBrand(), car.getModel(), car.getMileage(), car.getLastOilChange(),
                            car.getMpg(), car.getGasTank(), car.getFuelType(), car.getHp(), car.getGearboxType(), car.getYearProduction(), car.getYearRegistration(),
                            car.getDateInsurance().toString(false), car.getDateGTP().toString(false), car.getDateWheelChange().toString(false),
                            (car.getGarage() == null) ? "N/A" : car.getGarage(), (car.getInfo() == null || car.getInfo().isEmpty()) ? "N/A" : car.getInfo().replace("\n", "\\n")
                    );
                    fileContent.add(updatedCarData);
                    carExists = true;
                } else {
                    fileContent.add(line); // keep car
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // If the car doesn't exist
        if (!carExists) {
            String newCarData = String.format(
                    "%s; %s; %s; %d; %d; %.2f; %.2f; %s; %d; %s; %d; %d; %s; %s; %s; %s; %s",
                    car.getRegistrationNumber(), car.getBrand(), car.getModel(), car.getMileage(), car.getLastOilChange(),
                    car.getMpg(), car.getGasTank(), car.getFuelType(), car.getHp(), car.getGearboxType(), car.getYearProduction(), car.getYearRegistration(),
                    car.getDateInsurance().toString(false), car.getDateGTP().toString(false),
                    car.getDateWheelChange().toString(false), (car.getGarage() == null) ? "N/A" : car.getGarage(), (car.getInfo() == null || car.getInfo().isEmpty()) ? "N/A" : car.getInfo().replace("\n", "\\n")
            );
            fileContent.add(newCarData);
        }

        // Override
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load car info
    public static Car loadCar(String registrationNumber, String user) {

        File file = new File(getFileDirectory() + "\\" + user + ".cars");

        // File not exists
        if (!file.exists()) {
            return null;
        } else {

            try (BufferedReader reader = new BufferedReader(new FileReader(file.getPath()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] carData = line.split("; ");

                    if (carData[0].equals(registrationNumber)) {

                        RegistrationNumber regNumber = new RegistrationNumber(carData[0]);
                        String brand = carData[1];
                        String model = carData[2];
                        int mileage = Integer.parseInt(carData[3]);
                        int lastOilChange = Integer.parseInt(carData[4]);
                        double mpg = Double.parseDouble(carData[5].replace(",", "."));
                        double gasTank = Double.parseDouble(carData[6].replace(",", "."));
                        String fuelType = carData[7];
                        int hp = Integer.parseInt(carData[8]);
                        String gearboxType = carData[9];
                        int yearProduction = Integer.parseInt(carData[10]);
                        int yearRegistration = Integer.parseInt(carData[11]);
                        Date dateInsurance = new Date(carData[12]);
                        Date dateGTP = new Date(carData[13]);
                        Date dateWheelChange = new Date(carData[14]);
                        String garage = (carData[15].equals("N/A")) ? null : carData[15];
                        String info = (carData[16].equals("N/A")) ? null : carData[16].replace("\\n", "\n");

                        // Create object
                        return new Car(brand, model, regNumber, mileage, lastOilChange, mpg, gasTank, fuelType, hp, gearboxType, yearProduction, yearRegistration, dateInsurance, dateGTP, dateWheelChange, info, garage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    // Delete car
    public static void deleteCar(String registrationNumber, String user) {

        File file = new File(getFileDirectory() + "\\" + user + ".cars");

        List<String> fileContent = new ArrayList<>();
        boolean carFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] carData = line.split("; ");
                String regNumber = carData[0];
                if (!regNumber.equals(registrationNumber)) {
                    fileContent.add(line);
                } else {
                    carFound = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (!carFound) return;

        // Override
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load cars from file
    public static Object[][] loadVehicles(String user, String filterVehicleType, String filterRegNumber, String filterBrand, String filterModel, String filterDateInsurance, String filterDateRegister) {

        List<Object[]> vehicleDataList = new ArrayList<>();

        String carsFilePath = getFileDirectory() + "\\" + user + ".cars";
        String motorsFilePath = getFileDirectory() + "\\" + user + ".motors";

        usethis.processCarsFile(carsFilePath, vehicleDataList, "", filterRegNumber, filterBrand, filterModel, filterDateInsurance, filterDateRegister);

        Object[][] vehicleDataArray = new Object[vehicleDataList.size()][];
        return vehicleDataList.toArray(vehicleDataArray);

    }

    //endregion

    //region Garages - save / load / find

    // If cars file exists
    public static boolean garageFileExists(String user) {
        if (!user.isEmpty() && user != null) {
            File file = new File(getFileDirectory() + "\\" + user + ".garages");
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } else return false;
    }

    // If garage exists
    public static boolean garageExists(String user, String name) {

        File file = new File(getFileDirectory() + "\\" + user + ".garages");

        if (file.exists()) {

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.equals(name)) {
                        return true;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    // Save garage info
    public static void saveGarage(Garage garage, String user, String replace) {

        File file = new File(getFileDirectory() + "\\" + user + ".garages");

        // Ensure the file exists or create it
        if (!file.exists()) {
            try {
                file.createNewFile(); // Create the file if it doesn't exist
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Create a list to store the content of the file
        List<String> garageList = new ArrayList<>();

        // Read existing garages from the file and process them
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean replaced = false; // Flag to check if replacement has occurred

            while ((line = reader.readLine()) != null) {
                // If replace is not null and the line matches the replace value, replace it
                if (replace != null && line.equals(replace)) {
                    garageList.add(garage.getName()); // Replace the garage name
                    replaced = true; // Mark that we replaced the line
                } else {
                    garageList.add(line); // Keep other garage names unchanged
                }
            }

            // If no replacement happened, add the new garage name at the end
            if (!replaced && replace != null) {
                garageList.add(garage.getName()); // Add the new garage name if replace was specified but not found
            } else if (replace == null) {
                // If replace is null, always add the new garage at the end
                garageList.add(garage.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Rewrite the file with the updated list
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String garageName : garageList) {
                writer.write(garageName);
                writer.newLine(); // Add a new line after each garage name
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load garages from file
    public static Object[][] loadGarages(String user) {

        List<Object[]> garageDataList = new ArrayList<>();

        // Define the file path for the garages
        String garagesFilePath = getFileDirectory() + "\\" + user + ".garages";

        // Process the file to load garage data
        usethis.processGaragesFile(garagesFilePath, garageDataList);

        // Convert the list to a 2D array and return
        Object[][] garageDataArray = new Object[garageDataList.size()][];

        return garageDataList.toArray(garageDataArray);
    }

    // Load cars from file
    public static Object[][] loadGarageVehicles(String user, String garage) {

        List<Object[]> vehicleDataList = new ArrayList<>();

        String carsFilePath = getFileDirectory() + "\\" + user + ".cars";
        String motorsFilePath = getFileDirectory() + "\\" + user + ".motors";

        usethis.processCarsFile(carsFilePath, vehicleDataList, garage, "", "", "", "", "");

        Object[][] vehicleDataArray = new Object[vehicleDataList.size()][];
        return vehicleDataList.toArray(vehicleDataArray);

    }

    //endregion -

    // in-use
    private static class usethis {

        // Process file
        private static void processCarsFile(String filePath, List<Object[]> vehicleDataList, String garage, String filterRegNumber, String filterBrand, String filterModel, String filterDateInsurance, String filterDateRegister) {

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(";\\s*");

                    boolean matches = true;

                    // Filter by garage
                    if (!garage.isEmpty() && !fields[15].equalsIgnoreCase(garage)) {
                        matches = false;
                    }

                    // Filter by reg. number (if provided)
                    if (!filterRegNumber.isEmpty() && !fields[0].toLowerCase().contains(filterRegNumber.toLowerCase())) {
                        matches = false;
                    }

                    // Filter by brand (if provided)
                    if (!filterBrand.isEmpty() && !fields[1].toLowerCase().contains(filterBrand.toLowerCase())) {
                        matches = false;
                    }

                    // Filter by model (if provided)
                    if (!filterModel.isEmpty() && !fields[2].toLowerCase().contains(filterModel.toLowerCase())) {
                        matches = false;
                    }

                    // Filter by insurance date (if provided)
                    if (!filterDateInsurance.isEmpty() && !fields[12].equals(filterDateInsurance)) {
                        matches = false;
                    }

                    // Filter by register year (if provided)
                    if (!filterDateRegister.isEmpty() && !fields[10].equals(filterDateRegister)) {
                        matches = false;
                    }

                    // If the line matches all the filters
                    if (matches) {
                        Object[] vehicleData = new Object[7];
                        vehicleData[0] = fields[0];                                                             // Reg. number
                        vehicleData[1] = fields[1];                                                             // Brand
                        vehicleData[2] = fields[2];                                                             // Model
                        vehicleData[3] = Double.parseDouble(fields[5].replace(",", "."));      // Mpg
                        vehicleData[4] = fields[8];                                                             // BHP
                        vehicleData[5] = Integer.parseInt(fields[11]);                                          // Registration year
                        vehicleData[6] = new Date(fields[12]).toString(false);                              // Insurance date

                        // Add data
                        vehicleDataList.add(vehicleData);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // Process garages file
        private static void processGaragesFile(String filePath, List<Object[]> garageDataList) {

            File file = new File(filePath);

            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        garageDataList.add(new Object[]{line});
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
