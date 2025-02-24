package use;

import components.Car;
import components.User;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFiles {

    // Get connection pool
    private static JdbcConnectionPool CONNECTION_POOL;
    static {
        try {
            String[] config = configuration.Load();
            CONNECTION_POOL = JdbcConnectionPool.create(config[0], config[1], config[2]);
        } catch (IOException e) {
            CONNECTION_POOL = JdbcConnectionPool.create("jdbc:h2:~\\test", "sa", "");
        }
    }

    // Configurations
    public static class configuration {

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

            try (Connection conn = CONNECTION_POOL.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getNameFirst());
                stmt.setString(4, user.getNameLast());
                stmt.setString(5, user.getAddress() != null ? user.getAddress().toString() : "N/A");
                stmt.setString(6, user.getInfo() != null ? user.getInfo().replace("\n", "\\n") : "N/A");

                stmt.executeUpdate();

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

            String sql = "SELECT username, password, first_name, last_name, address, info FROM USERS WHERE username = ?";

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

                    // If all data is correct return the user
                    return new User(
                            rs.getString("username"),
                            dbPassword,
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            new Address(rs.getString("address")),
                            rs.getString("info")
                    );

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

        // todo

    }

}
