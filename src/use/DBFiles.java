package use;

import components.Car;
import components.Garage;
import components.User;
import org.h2.jdbcx.JdbcConnectionPool;
import ui.frame.Mine;
import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBFiles {

    // Connection attributes
    private static String CONNECTION_PATH;
    private static String CONNECTION_PATH_REAL;
    private static String CONNECTION_ADMIN;
    private static String CONNECTION_PASS;

    static {
        try {
            String[] config = configuration.Load();
            if (config.length >= 3) {
                CONNECTION_PATH_REAL = config[0];
                CONNECTION_PATH = CONNECTION_PATH_REAL.replace("%projectfiles%", Files.getFileDirectory());
                CONNECTION_ADMIN = config[1];
                CONNECTION_PASS = config[2];
            } else {
                throw new IOException("Invalid configuration file format");
            }
        } catch (IOException e) {
            CONNECTION_PATH_REAL = "jdbc:h2:" + Files.getFileDirectory();
            CONNECTION_PATH = CONNECTION_PATH_REAL;
            CONNECTION_ADMIN = "admin";
            CONNECTION_PASS = "pass";
            e.printStackTrace();
        }
    }

    // Get connection pool
    private static final JdbcConnectionPool CONNECTION_POOL = JdbcConnectionPool.create(CONNECTION_PATH, CONNECTION_ADMIN, CONNECTION_PASS);

    /*
     * The configuration class provides static methods for managing database configurations, such as loading, updating,
     * and retrieving the database connection path, admin name, and password.
     */
    public static class configuration {

        /*
         * Loads database configuration from a file and returns it as an array of strings.
         */
        public static String[] Load() throws IOException {

            String filePath = Files.getFileDirectory() + "\\db-configurations";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                return reader.readLine().split(", ");
            }

        }


        /*
         * Updates the database configuration file with the given path, user, and password.
         */
        public static void Update(String path, String user, String pass) throws IOException {

            String filePath = Files.getFileDirectory() + "\\db-configurations";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(path + ", " + user + ", " + pass);
            }
        }

        /*
         * Returns the current database connection path.
         */
        public static String GetPath() {
            return CONNECTION_PATH_REAL;
        }

        /*
         * Returns the admin name for the database connection.
         */
        public static String GetAdmin() {
            return CONNECTION_ADMIN;
        }

        /*
         * Returns the password for the database connection.
         */
        public static String GetPassword() {
            return CONNECTION_PASS;
        }

    }

    /*
     * The user class provides static methods to interact with the database for user management.
     * These methods include checking if a username exists, saving, updating, and loading user data.
     */
    public static class user {

        /*
         * Checks if the provided username already exists in the database.
         * @param username - The username to check.
         * @return true if the username exists, false otherwise.
         */
        public static boolean usernameExists(String username) {

            String sql = "SELECT COUNT(*) FROM USERS WHERE username = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) return rs.getInt(1) > 0;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;

        }

        /*
         * Saves a new user to the database.
         * @param user - The user object to save.
         */
        public static void saveUser(User user) {

            String sql = "MERGE INTO USERS (username, password, first_name, last_name, address, info) " +
                         "KEY(username) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getNameFirst());
                stmt.setString(4, user.getNameLast());
                stmt.setString(5, user.getAddress() != null ? user.getAddress().toString() : "N/A");
                stmt.setString(6, user.getInfo() != null ? user.getInfo().replace("\n", "\\n") : "N/A");

                stmt.executeUpdate();

                // Save ID to the user object
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setID(rs.getInt(1));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*
         * Updates the details of an existing user in the database.
         * @param user - The user object with updated data.
         */
        public static void updateUser(User user) {

            String sql = "UPDATE USERS SET password = ?, first_name = ?, last_name = ?, address = ?, info = ? WHERE username = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, user.getPassword());
                stmt.setString(2, user.getNameFirst());
                stmt.setString(3, user.getNameLast());
                stmt.setString(4, user.getAddress() != null ? user.getAddress().toString() : "N/A");
                stmt.setString(5, user.getInfo() != null ? user.getInfo().replace("\n", "\\n") : "N/A");
                stmt.setString(6, user.getUsername());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "Промените са успешни!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Потребител с това име не съществува!", "Грешка!", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*
         * Loads a user from the database based on the provided username and password.
         * @param username - The username of the user to load.
         * @param password - The password to validate.
         * @return the User object if found and password is correct, null otherwise.
         */
        public static User loadUser(String username, String password) {

            String sql = "SELECT id, username, password, first_name, last_name, address info FROM USERS WHERE username = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {

                    // Get data
                    String dbPassword = rs.getString("password");

                    // Wrong password
                    if (!dbPassword.equals(password)) {
                        JOptionPane.showMessageDialog(null, "Грешна парола!", "Грешка", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }

                    // Load the user
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            dbPassword,
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            new Address(rs.getString("address")),
                            rs.getString("info")
                    );

                    return user;

                } else {

                    JOptionPane.showMessageDialog(null, "Потребителят не съществува!", "Грешка!", JOptionPane.ERROR_MESSAGE);
                    return null;

                }

            } catch (SQLException e) {

                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Грешка при свързване с базата данни!", "Грешка!", JOptionPane.ERROR_MESSAGE);

            }

            return null;
        }

    }

    /*
     * The car class provides static methods to interact with the database for car management.
     * These methods include checking if a car exists, saving, updating, loading car data, and performing delete operations.
     */
    public static class car {

        /*
         * Checks if a car with the given registration number exists in the database.
         * @param registrationNumber - The registration number of the car.
         * @return true if the car exists, false otherwise.
         */
        public static boolean carExists(String registrationNumber) {

            String sql = "SELECT COUNT(*) FROM CARS WHERE id = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, registrationNumber);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) return rs.getInt(1) > 0;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;

        }

        /*
         * Saves car information to the database.
         * @param car - The car object to save.
         */
        public static void saveCar(Car car) {

            // Garage not exists
            Integer garageId = null;
            if (car.getGarage() != null && !car.getGarage().equalsIgnoreCase("N/A")) {
                garageId = garage.getGarageIdByName(car.getGarage(), Mine.currentUser.getID());
            } else {
                JOptionPane.showMessageDialog(null, "Не съществува такъв гараж!");
                return;
            }

            // SQL syntax
            String sql = "MERGE INTO CARS " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                //region Get date formats

                // Insurance date
                java.sql.Date sqlDateInsurance = java.sql.Date.valueOf(LocalDate.of(
                        car.getDateInsurance().getYear(),
                        car.getDateInsurance().getMonth(),
                        car.getDateInsurance().getDay())
                );

                // GTP date
                java.sql.Date sqlDateGTP = java.sql.Date.valueOf(LocalDate.of(
                        car.getDateGTP().getYear(),
                        car.getDateGTP().getMonth(),
                        car.getDateGTP().getDay())
                );

                // Wheel change date
                java.sql.Date sqlDateWheelChange = java.sql.Date.valueOf(LocalDate.of(
                        car.getDateWheelChange().getYear(),
                        car.getDateWheelChange().getMonth(),
                        car.getDateWheelChange().getDay())
                );

                //endregion

                // Save data
                stmt.setString(1, car.getRegistrationNumber().toString());           // id - registration_number
                stmt.setString(2, car.getBrand());                                   // brand
                stmt.setString(3, car.getModel());                                   // model
                stmt.setInt(4, car.getMileage());                                    // mileage
                stmt.setInt(5, car.getLastOilChange());                              // last_oil_change
                stmt.setDouble(6, car.getMpg());                                     // mpg
                stmt.setDouble(7, car.getGasTank());                                 // gas_tank
                stmt.setString(8, car.getFuelType());                                // fuel_type
                stmt.setInt(9, car.getHp());                                         // hp
                stmt.setString(10, car.getGearboxType());                            // gearbox
                stmt.setInt(11, car.getYearProduction());                            // release_year
                stmt.setInt(12, car.getYearRegistration());                          // registration_year
                stmt.setDate(13, sqlDateInsurance);                                  // insurance_date
                stmt.setDate(14, sqlDateGTP);                                        // gtp_date
                stmt.setDate(15, sqlDateWheelChange);                                // wheels_date
                stmt.setObject(16, garageId, Types.INTEGER);                         // garage_id
                stmt.setString(17, (car.getInfo() == null || car.getInfo().isEmpty()) ? "N/A" : car.getInfo().replace("\n", "\\n")); // info

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) JOptionPane.showMessageDialog(null, "Колата е запазена успешно!");
                else JOptionPane.showMessageDialog(null, "Грешка при запис на колата!");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        /*
         * Updates the information of an existing car in the database.
         * @param car - The car object with updated data.
         */
        public static void updateCar(Car car) {

            // Garage not exists
            Integer garageId = null;
            if (car.getGarage() != null && !car.getGarage().equalsIgnoreCase("N/A")) {
                garageId = garage.getGarageIdByName(car.getGarage(), Mine.currentUser.getID());
            } else {
                JOptionPane.showMessageDialog(null, "Не съществува такъв гараж!");
                return;
            }

            // SQL syntax
            String sql = "UPDATE CARS SET " +
                    "brand = ?, model = ?, mileage = ?, last_oil_change = ?, mpg = ?, gas_tank = ?, " +
                    "fuel_type = ?, hp = ?, gearbox = ?, release_year = ?, registration_year = ?, " +
                    "insurance_date = ?, gtp_date = ?, wheels_date = ?, garage_id = ?, info = ? " +
                    "AND id = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                //region Get date formats

                // Insurance date
                java.sql.Date sqlDateInsurance = java.sql.Date.valueOf(LocalDate.of(
                        car.getDateInsurance().getYear(),
                        car.getDateInsurance().getMonth(),
                        car.getDateInsurance().getDay())
                );

                // GTP date
                java.sql.Date sqlDateGTP = java.sql.Date.valueOf(LocalDate.of(
                        car.getDateGTP().getYear(),
                        car.getDateGTP().getMonth(),
                        car.getDateGTP().getDay())
                );

                // Wheel change date
                java.sql.Date sqlDateWheelChange = java.sql.Date.valueOf(LocalDate.of(
                        car.getDateWheelChange().getYear(),
                        car.getDateWheelChange().getMonth(),
                        car.getDateWheelChange().getDay())
                );

                //endregion

                // Update data
                stmt.setString(1, car.getBrand());                                  // brand
                stmt.setString(2, car.getModel());                                  // model
                stmt.setInt(3, car.getMileage());                                   // mileage
                stmt.setInt(4, car.getLastOilChange());                             // last_oil_change
                stmt.setDouble(5, car.getMpg());                                    // mpg
                stmt.setDouble(6, car.getGasTank());                                // gas_tank
                stmt.setString(7, car.getFuelType());                               // fuel_type
                stmt.setInt(8, car.getHp());                                        // hp
                stmt.setString(9, car.getGearboxType());                            // gearbox
                stmt.setInt(10, car.getYearProduction());                           // release_year
                stmt.setInt(11, car.getYearRegistration());                         // registration_year
                stmt.setDate(12, sqlDateInsurance);                                 // insurance_date
                stmt.setDate(13, sqlDateGTP);                                       // gtp_date
                stmt.setDate(14, sqlDateWheelChange);                               // wheels_date
                stmt.setObject(15, garageId, Types.INTEGER);                        // garage_id
                stmt.setString(16, (car.getInfo() == null || car.getInfo().isEmpty()) ? "N/A" : car.getInfo().replace("\n", "\\n")); // info
                stmt.setString(17, car.getRegistrationNumber().toString());         // id - registration_number

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) JOptionPane.showMessageDialog(null, "Данните за колата бяха обновени успешно!");
                else JOptionPane.showMessageDialog(null, "Не бе намерена такава кола :(");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        /*
         * Loads car information from the database based on the given registration number.
         * @param registrationNumber - The registration number of the car to load.
         * @return the Car object with loaded data, or null if the car is not found.
         */
        public static Car loadCar(String registrationNumber) {

            // SQL syntax
            String sql = "SELECT * FROM CARS WHERE id = ?";

            // Car object
            Car car = null;

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Set parameter index
                stmt.setString(1, registrationNumber);

                try (ResultSet rs = stmt.executeQuery()) {

                    // Load data
                    if (rs.next()) {
                        car = new Car(
                                rs.getString("brand"),                                                          // brand
                                rs.getString("model"),                                                          // model
                                new RegistrationNumber(rs.getString("id")),                                     // id - registration number
                                rs.getInt("mileage"),                                                           // mileage
                                rs.getInt("last_oil_change"),                                                   // last_oil_change
                                rs.getDouble("mpg"),                                                            // mpg
                                rs.getDouble("gas_tank"),                                                       // gas_tank
                                rs.getString("fuel_type"),                                                      // fuel_type
                                rs.getInt("hp"),                                                                // hp
                                rs.getString("gearbox"),                                                        // gearbox
                                rs.getInt("release_year"),                                                      // release_year
                                rs.getInt("registration_year"),                                                 // registration_year
                                new Date(rs.getDate("gtp_date").toLocalDate()),                                 // gtp_date
                                new Date(rs.getDate("insurance_date").toLocalDate()),                           // insurance_date
                                new Date(rs.getDate("wheels_date").toLocalDate()),                              // wheels_date
                                (rs.getString("info").equals("N/A")) ? null : rs.getString("info"),  // info
                                garage.getGarageNameById(Mine.currentUser.getID(), rs.getInt("garage_id"))      // garage_id
                        );
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return car;

        }

        /*
         * Checks if the car with the given registration number can be modified by the provided username.
         * @param registrationNumber - The registration number of the car.
         * @param username - The username of the user requesting modification.
         * @return true if the car can be modified by the user, false otherwise.
         */
        public static boolean carCanModify(String registrationNumber, String username) {

            // Check if registration number and username are valid
            if (registrationNumber == null || registrationNumber.trim().isEmpty() || username == null || username.trim().isEmpty()) {
                return false;
            }

            // SQL syntax
            String sql = "SELECT u.username " +
                    "FROM CARS c " +
                    "JOIN GARAGES g ON c.garage_id = g.id " +
                    "JOIN USERS u ON g.user_id = u.id " +
                    "WHERE c.id = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Set registration number parameter
                stmt.setString(1, registrationNumber);

                // Make transaction
                ResultSet rs = stmt.executeQuery();

                // Can delete
                if (rs.next()) {
                    String garageOwnerUsername = rs.getString("username");
                    return garageOwnerUsername != null && garageOwnerUsername.equals(username);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;

        }

        /*
         * Deletes the car with the given registration number from the database.
         * @param registrationNumber - The registration number of the car to delete.
         */
        public static void deleteCar(String registrationNumber) {

            // Check registration number
            if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Невалиден регистрационен номер.");
                return;
            }

            // SQL syntax
            String sql = "DELETE FROM CARS WHERE id = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Set registration number as paramether
                stmt.setString(1, registrationNumber);

                // Make transaction
                int rowsAffected = stmt.executeUpdate();

                // Check if the row is deleted
                if (rowsAffected > 0)
                    JOptionPane.showMessageDialog(null, "Колата с регистрационен номер " + registrationNumber + " беше изтрита успешно.");
                else
                    JOptionPane.showMessageDialog(null, "Няма кола с регистрационен номер " + registrationNumber + ".");

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Грешка при изтриване на колата.");
            }

        }

        /*
         * Loads a list of cars from the database based on various filter parameters.
         * @param filterGarage - The garage filter to apply (optional).
         * @param filterRegNumber - The registration number filter to apply (optional).
         * @param filterBrand - The brand filter to apply (optional).
         * @param filterModel - The model filter to apply (optional).
         * @param filterDateInsurance - The insurance date filter to apply (optional).
         * @param filterDateRegister - The registration year filter to apply (optional).
         * @return a 2D Object array with car data that matches the filters.
         */
        public static Object[][] loadVehicleList(String filterGarage, String filterRegNumber, String filterBrand, String filterModel, String filterDateInsurance, String filterDateRegister) {

            List<Object[]> vehicleDataList = new ArrayList<>();

            // SQL syntax
            String sql = "SELECT c.id, c.brand, c.model, c.mileage, c.last_oil_change, c.mpg, c.gas_tank, c.fuel_type, c.hp, c.gearbox, c.release_year, c.registration_year, c.insurance_date " +
                    "FROM CARS c JOIN GARAGES g ON c.garage_id = g.id";

            //region Filters

            // Garage filter
            if (!filterGarage.isEmpty()) sql += " WHERE c.garage_id = ?";

            // Registration number filter
            if (!filterRegNumber.isEmpty()) sql += " AND c.id LIKE ?";

            // Brand filter
            if (!filterBrand.isEmpty()) sql += " AND c.brand LIKE ?";

            // Model filter
            if (!filterModel.isEmpty()) sql += " AND c.model LIKE ?";

            // Insurance date filter
            if (!filterDateInsurance.isEmpty()) sql += " AND c.insurance_date = ?";

            // Registration year filter
            if (!filterDateRegister.isEmpty()) sql += " AND c.registration_year = ?";

            //endregion

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                //region Filters

                int paramIndex = 1;

                // Garage filter
                if (!filterGarage.isEmpty()) stmt.setInt(paramIndex++, garage.getGarageIdByName(filterGarage, -1));

                // Registration number filter
                if (!filterRegNumber.isEmpty()) stmt.setString(paramIndex++, "%" + filterRegNumber.toLowerCase() + "%");

                // Brand filter
                if (!filterBrand.isEmpty()) stmt.setString(paramIndex++, "%" + filterBrand.toLowerCase() + "%");

                // Model filter
                if (!filterModel.isEmpty()) stmt.setString(paramIndex++, "%" + filterModel.toLowerCase() + "%");

                // Insurance date filter
                if (!filterDateInsurance.isEmpty()) {
                    Date dateInsurance = new Date(filterDateInsurance);
                    java.sql.Date sqlDateInsurance = java.sql.Date.valueOf(LocalDate.of(
                            dateInsurance.getYear(), dateInsurance.getMonth(), dateInsurance.getDay())
                    );
                    stmt.setDate(paramIndex++, sqlDateInsurance);
                }

                // Registration year filter
                if (!filterDateRegister.isEmpty()) stmt.setInt(paramIndex++, Integer.parseInt(filterDateRegister));

                //endregion

                // Изпълняваме заявката и обработваме резултатите
                ResultSet rs = stmt.executeQuery();

                // Прочитаме резултатите и добавяме в списъка
                while (rs.next()) {
                    Object[] vehicleData = new Object[7];
                    vehicleData[0] = rs.getString("id");           // id - registration number
                    vehicleData[1] = rs.getString("brand");        // brand
                    vehicleData[2] = rs.getString("model");        // model
                    vehicleData[3] = rs.getDouble("mpg");          // mpg
                    vehicleData[4] = rs.getInt("hp");              // hp
                    vehicleData[5] = rs.getInt("release_year");    // release_year
                    vehicleData[6] = rs.getString("fuel_type");    // fuel_type

                    vehicleDataList.add(vehicleData);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Връщаме данните като масив от масиви
            return vehicleDataList.toArray(new Object[0][]);
        }

    }

    /*
     * The garage class provides static methods to interact with the database for managing garages.
     * These methods include checking if a garage exists, saving, updating, and loading garage data.
     */
    public static class garage {

        /*
         * Retrieves the garage ID based on the garage name and user ID.
         * @param garageName - The name of the garage.
         * @param userId - The user ID of the current user.
         * @return the garage ID if found, null otherwise.
         */
        public static Integer getGarageIdByName(String garageName, int userId) {

            // Garage name not exists
            if (garageName == null || garageName.trim().isEmpty()) return null;

            // SQL syntax
            String sql = "SELECT id FROM GARAGES WHERE garage_name = ?";
            if (userId >= 0) sql += " AND user_id = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, garageName);
                if (userId >= 0) stmt.setInt(2, userId);
                ResultSet rs = stmt.executeQuery();

                // Returns garage ID
                if (rs.next()) return rs.getInt("id");

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;

        }

        /*
         * Retrieves the garage name based on the garage ID and user ID.
         * @param garageId - The ID of the garage.
         * @param userId - The user ID of the current user.
         * @return the garage name if found, null otherwise.
         */
        public static String getGarageNameById(int garageId, int userId) {

            // Check for garage ID and user ID
            if (garageId <= 0 || userId <= 0) return null;

            // SQL syntax
            String sql = "SELECT garage_name FROM GARAGES WHERE id = ? AND user_id = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, garageId);
                stmt.setInt(2, userId);
                ResultSet rs = stmt.executeQuery();

                // Return garage
                if (rs.next()) return rs.getString("garage_name");

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;

        }

        /*
         * Checks if the current user has any garages associated with them.
         * @return true if the user has at least one garage, false otherwise.
         */
        public static boolean userHasGarages() {

            // Get user db.id
            int userId = Mine.currentUser.getID();

            // If user not exists
            if (userId <= 0) {
                System.out.println("User ID not found.");
                return false;
            }

            // SQL syntax
            String sql = "SELECT COUNT(*) FROM GARAGES " +
                         "WHERE user_id = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                // If user has at least 1 garage
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        }

        /*
         * Checks if a garage with the specified name exists for the current user.
         * @param name - The name of the garage.
         * @return true if the garage exists, false otherwise.
         */
        public static boolean garageExists(String name) {

            // Get user db.id
            int userId = Mine.currentUser.getID();

            // User not exists
            if (userId <= 0) {
                System.out.println("User ID not found.");
                return false;
            }

            // Command line for SQL
            String sql = "SELECT COUNT(*) FROM GARAGES " +
                         "WHERE user_id = ? " +
                         "AND garage_name = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, userId);
                stmt.setString(2, name);
                ResultSet rs = stmt.executeQuery();

                // Garage name exists
                if (rs.next() && rs.getInt(1) > 0) return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;

        }

        /*
         * Saves a new garage to the database for the current user.
         * @param garage - The garage object to save.
         */
        public static void saveGarage(Garage garage) {

            // Get user db.id
            int userId = Mine.currentUser.getID();

            // User not exists
            if (userId <= 0) {
                System.out.println("User ID not found.");
                return;
            }

            // Save garage string
            String sql = "MERGE INTO GARAGES (garage_name, user_id) " +
                         "KEY(garage_name, user_id) VALUES (?, ?)";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, garage.getName());
                stmt.setInt(2, userId);
                stmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        /*
         * Updates the name of an existing garage for the current user.
         * @param garage - The garage object with the current name.
         * @param newName - The new name for the garage.
         */
        public static void updateGarage(Garage garage, String newName) {

            // Get user db.id
            int userId = Mine.currentUser.getID();

            // User not exists
            if (userId <= 0) {
                System.out.println("User ID not found.");
                return;
            }

            // Garage not exists
            if (!garageExists(garage.getName())) {
                System.out.println("Garage not found. Cannot update.");
                return;
            }

            // SQl syntax
            String sql = "UPDATE GARAGES SET garage_name = ? " +
                         "WHERE user_id = ? " +
                         "AND garage_name = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, newName);
                stmt.setInt(2, userId);
                stmt.setString(3, garage.getName());

                int rowsAffected = stmt.executeUpdate();

                // Update message
                if (rowsAffected > 0) JOptionPane.showMessageDialog(null, "Името на гаражът бе променено успешно!");
                else JOptionPane.showMessageDialog(null, "Не бе намерен гараж, който да бъде променен!");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        /*
         * Loads a list of garage names associated with the current user or optionally ignores user filtering.
         * @param ignoreUser - If true, the method ignores the user filter and loads all garages; otherwise, it filters by user.
         * @return a 2D array of garage names.
         */
        public static Object[][] loadGarageList(boolean ignoreUser) {

            List<Object[]> garageDataList = new ArrayList<>();

            // Get user db.id
            int userId = Mine.currentUser.getID();

            // User not exists
            if (userId <= 0) {
                System.out.println("User ID not found.");
                return new Object[0][];
            }

            // Select syntax
            String sql = "SELECT garage_name FROM GARAGES ";
            if (!ignoreUser) sql += "WHERE user_id = ?";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                if (!ignoreUser) stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                // Add to list
                while (rs.next()) {
                    String garageName = rs.getString("garage_name");
                    garageDataList.add(new Object[]{ garageName });
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Return list of names
            return garageDataList.toArray(new Object[0][]);

        }

    }

}
