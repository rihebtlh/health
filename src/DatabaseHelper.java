import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/healthSync?useSSL=false&serverTimezone=UTC"; 
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = ""; 

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
        return null;
    }

    public static void testConnection() throws SQLException {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Database connection successful!");
            } else {
                System.out.println("Failed to make connection!");
            }
        }
    }

    public int validateUser(String email, String password) {
        int userId = -1;
        String sql = "SELECT id FROM users WHERE email = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error validating user: " + e.getMessage());
        }
        return userId;
    }

    public static boolean registerUser(String fullName, String email, String password) {
        boolean success = false;
        String sql = "INSERT INTO users (full_name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, password);
            success = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
        return success;
    }

    public boolean isAdmin(String email, String password) {
        boolean isAdmin = false;
        try (Connection conn = getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND password = ? AND email = 'admin@gmail.com'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    isAdmin = true; 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAdmin;
    }
    public boolean isDoctor(String email, String password) {
        boolean isDoctor = false;
        try (Connection conn = getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND password = ? AND email = 'doctor@gmail.com'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    isDoctor = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDoctor;
    }

    public static List<String> fetchPatientNames() {
        List<String> patientNames = new ArrayList<>();
        String sql = "SELECT full_name FROM users";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                patientNames.add(rs.getString("full_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching patient names: " + e.getMessage());
        }
        return patientNames;
    }

    public static boolean sendNotification(int patientId, String message) {
        String sql = "INSERT INTO notifications (id, message) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            stmt.setString(2, message);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error sending notification: " + e.getMessage());
        }
        return false;
    }

    public static List<String> getNotifications(int patientId) {
        List<String> notifications = new ArrayList<>();
        String sql = "SELECT message, date FROM notifications WHERE id = ? ORDER BY date DESC";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(rs.getString("message") + " (" + rs.getString("timestamp") + ")");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching notifications: " + e.getMessage());
        }
        return notifications;
    }

    public static int getPatientIdByName(String patientName) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT id FROM users WHERE full_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, patientName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
