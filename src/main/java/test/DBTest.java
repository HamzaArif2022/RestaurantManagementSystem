import java.sql.Connection;
import java.sql.SQLException;

public class DBTest {
    public static void main(String[] args) {
        try (Connection conn = com.restaurant.config.DBConfig.getConnection()) {
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}