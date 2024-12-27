import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            insertScreen(connection, 10, 20, true);
           // insertScreen(connection, 15, 25, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void insertScreen(Connection connection, int rows, int columns, boolean is3D) throws SQLException {
        String sql = "INSERT INTO Screens (rows, columns, is3D) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, rows);
            statement.setInt(2, columns);
            statement.setBoolean(3, is3D);
            statement.executeUpdate();
        }
    }
}