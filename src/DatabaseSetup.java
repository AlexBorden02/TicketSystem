import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    public static void createTables() {
        String createScreensTable = "CREATE TABLE IF NOT EXISTS Screens (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "rows INTEGER NOT NULL," +
                "columns INTEGER NOT NULL," +
                "is3D BOOLEAN NOT NULL" +
                ");";

        String createSeatsTable = "CREATE TABLE IF NOT EXISTS Seats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "screenID INTEGER NOT NULL," +
                "isBooked BOOLEAN NOT NULL," +
                "isWheelchairAccessible BOOLEAN NOT NULL," +
                "FOREIGN KEY(screenID) REFERENCES Screens(id)" +
                ");";

        String createFilmsTable = "CREATE TABLE IF NOT EXISTS Films (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "duration INTEGER NOT NULL," +
                "screeningTimes TEXT NOT NULL," +
                "screenID INTEGER NOT NULL," +
                "FOREIGN KEY(screenID) REFERENCES Screens(id)" +
                ");";

        String createBookingsTable = "CREATE TABLE IF NOT EXISTS Bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "filmID INTEGER NOT NULL," +
                "seatID INTEGER NOT NULL," +
                "FOREIGN KEY(filmID) REFERENCES Films(id)," +
                "FOREIGN KEY(seatID) REFERENCES Seats(id)" +
                ");";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createScreensTable);
            stmt.execute(createSeatsTable);
            stmt.execute(createFilmsTable);
            stmt.execute(createBookingsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}