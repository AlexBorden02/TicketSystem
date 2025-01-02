import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            //(connection, 10, 20, true);
           // insertScreen(connection, 15, 25, false);
            //insertSeats(connection, 1, 8, 12);
            //insertSeats(connection, 2, 5, 8);
            insertDummyFilms(connection);
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

    private static void insertDummyFilms(Connection connection) throws SQLException {
        String sql = "INSERT INTO Films (title, duration, screeningTimes, screenId) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "The Matrix");
            statement.setInt(2, 120);
            statement.setString(3, LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            statement.setInt(4, 1);
            statement.addBatch();

            statement.setString(1, "Inception");
            statement.setInt(2, 150);
            statement.setString(3, LocalDateTime.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            statement.setInt(4, 1);
            statement.addBatch();

            statement.setString(1, "Interstellar");
            statement.setInt(2, 169);
            statement.setString(3, LocalDateTime.now().plusDays(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            statement.setInt(4, 1);
            statement.addBatch();

            statement.executeBatch();
        }
    }
}