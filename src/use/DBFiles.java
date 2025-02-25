package use;

import components.Car;
import components.Garage;
import components.User;
import org.h2.jdbcx.JdbcConnectionPool;
import ui.frame.Mine;

import javax.swing.*;
import java.io.*;
import java.sql.*;

public class DBFiles {

    // Connection attributes
    private static String CONNECTION_PATH;
    private static String CONNECTION_ADMIN;
    private static String CONNECTION_PASS;

    static {
        try {
            String[] config = configuration.Load();
            CONNECTION_PATH = config[0];
            CONNECTION_ADMIN = config[1];
            CONNECTION_PASS = config[2];
        } catch (IOException e) {
            CONNECTION_PATH = "jdbc:h2:~\\test";
            CONNECTION_ADMIN = "admin";
            CONNECTION_PASS = "pass";
        }
    }

    // Get connection pool
    private static JdbcConnectionPool CONNECTION_POOL = JdbcConnectionPool.create(CONNECTION_PATH, CONNECTION_ADMIN, CONNECTION_PASS);

    // Configurations
    public static class configuration {

        // Get path to DB
        public static String GetPath() {
            return CONNECTION_PATH;
        }

        // Get admin name
        public static String GetAdmin() {
            return CONNECTION_ADMIN;
        }

        // Get password
        public static String GetPassword() {
            return CONNECTION_PASS;
        }

        public static String[] Load() throws IOException {

            String filePath = Files.getFileDirectory() + "\\db-configurations";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                return reader.readLine().split(", ");
            }

        }

        public static void Update(String path, String user, String pass) throws IOException {

            String filePath = Files.getFileDirectory() + "\\db-configurations";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(path + ", " + user + ", " + pass);
            }
        }

    }

    // Users
    public static class user {

        // If username exists
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

        // Save new user
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

        // Update existing user
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

        // Load user
        public static User loadUser(String username, String password) {

            String sql = "SELECT id, username, password, first_name, last_name, address, info FROM USERS WHERE username = ?";

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

    // Cars
    public static class car {

        // If car exists
        public static boolean carExists() {

            // todo
            return false;

        }

        // Save car info
        public static void saveCar() {

            // todo

        }

        // Load car info
        public static Car loadCar() {

            // todo
            return null;

        }

        // Delete car
        public static void deleteCar() {

            // todo

        }

    }

    // Garages
    public static class garages {

        // Save garage
        public static void saveGarage(Garage garage) {

            // Get user id
            int userId = Mine.currentUser.getID();

            // User not exists
            if (userId <= 0) {
                System.out.println("User ID not found.");
                return;
            }

            // Save garage string
            String sql = "MERGE INTO GARAGE (garage_name, user_id) " +
                         "KEY(garage_name, user_id) VALUES (?, ?)";

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, garage.getName());
                stmt.setInt(2, userId);
                stmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

}
