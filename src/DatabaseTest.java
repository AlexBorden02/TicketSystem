import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            //(connection, 10, 20, true);
           // insertScreen(connection, 15, 25, false);
            insertSeats(connection, 1, 8, 12);
            insertSeats(connection, 2, 5, 8);
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

    private static void insertSeats(Connection connection, int screenId, int rows, int columns) throws SQLException {
        String sql = "INSERT INTO Seats (id, screenId, isBooked, isWheelchairAccessible) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < rows * columns; i++) {
                int seatId = Integer.parseInt(screenId + String.format("%02d", i + 1));
                statement.setInt(1, seatId);

                statement.setInt(2, screenId);
                statement.setBoolean(3, false);

                statement.setBoolean(4, i < 15);

                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}