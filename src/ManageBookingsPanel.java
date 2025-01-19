import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class ManageBookingsPanel extends JPanel {
    private JPanel mainPanel;
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private int searchId;

    public ManageBookingsPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Manage Bookings");
        add(label, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Booking ID", "Film ID", "Seat ID", "Film Title", "Screen ID"}, 0);
        bookingsTable = new JTable(tableModel);
        add(new JScrollPane(bookingsTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);

        JButton removeButton = new JButton("Remove Booking");
        removeButton.addActionListener(e -> removeBooking());
        bottomPanel.add(removeButton, BorderLayout.SOUTH);

        JTextField searchField = new JTextField();
        bottomPanel.add(searchField, BorderLayout.NORTH);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            try {
                searchId = Integer.parseInt(searchField.getText());
                searchBookings();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID.");
            }
        });
        bottomPanel.add(searchButton, BorderLayout.EAST);

        JButton clearButton = new JButton("Clear Search");
        clearButton.addActionListener(e -> {
            searchId = -1;
            loadBookings();
            searchField.setText("");
        });
        bottomPanel.add(clearButton, BorderLayout.WEST);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("MainMenu"));
        add(backButton, BorderLayout.NORTH);

        loadBookings();
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    private void loadBookings() {
        tableModel.setRowCount(0);
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT b.id, b.filmID, b.seatID, f.title, f.screenID " +
                 "FROM Bookings b " +
                 "JOIN Films f ON b.filmID = f.id"
             ); // getting film data as I need the title and screenID
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int bookingId = resultSet.getInt("id");
                int filmId = resultSet.getInt("filmID");
                int seatId = resultSet.getInt("seatID");

                String title = resultSet.getString("title");
                int screenId = resultSet.getInt("screenID");

                tableModel.addRow(new Object[]{bookingId, filmId, seatId, title, screenId});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow != -1) {
            int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM Bookings WHERE id = ?")) {
                statement.setInt(1, bookingId);
                statement.executeUpdate();
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Booking removed successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking to remove.");
        }
    }

    private void searchBookings(){
        if (searchId != -1) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                     "SELECT b.id, b.filmID, b.seatID, f.title, f.screenID " +
                     "FROM Bookings b " +
                     "JOIN Films f ON b.filmID = f.id " +
                     "WHERE b.id = ?"
                 )) {
                statement.setInt(1, searchId);
                ResultSet resultSet = statement.executeQuery();
                tableModel.setRowCount(0);
                while (resultSet.next()) {
                    int bookingId = resultSet.getInt("id");
                    int filmId = resultSet.getInt("filmID");
                    int seatId = resultSet.getInt("seatID");

                    String title = resultSet.getString("title");
                    int screenId = resultSet.getInt("screenID");

                    tableModel.addRow(new Object[]{bookingId, filmId, seatId, title, screenId});
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.");
        }
    }
}