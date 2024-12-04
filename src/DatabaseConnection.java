import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/healthSync";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
}
