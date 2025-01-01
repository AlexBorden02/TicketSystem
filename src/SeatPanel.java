import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;

public class SeatPanel extends JPanel {
    private JPanel mainPanel;
    private int seatId;
    private int screenId;
    private JLabel seatLabel;
    private JButton bookButton;
    private JButton unbookButton;

    public SeatPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        seatLabel = new JLabel();
        add(seatLabel, BorderLayout.NORTH);

        bookButton = new JButton("Book Seat");
        bookButton.addActionListener(e -> bookSeat());
        add(bookButton, BorderLayout.CENTER);

        unbookButton = new JButton("Unbook Seat");
        unbookButton.addActionListener(e -> unbookSeat());
        add(unbookButton, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("ScreenPanel", screenId));
        add(backButton, BorderLayout.SOUTH);
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    private void switchPanel(String panelName, int id) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
        Component[] components = mainPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ScreenPanel) {
                ((ScreenPanel) component).setScreenId(id);
            }
        }
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
        seatLabel.setText("Seat " + seatId);
        fetchSeatInfo();
        revalidate();
        repaint();
    }

    private void fetchSeatInfo() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM seats WHERE id = ?")) {
            preparedStatement.setInt(1, seatId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                boolean isBooked = resultSet.getBoolean("isBooked");
                if (isBooked) {
                    seatLabel.setText("Seat " + seatId + " is booked");
                    bookButton.setVisible(false);
                    unbookButton.setVisible(true);
                } else {
                    seatLabel.setText("Seat " + seatId + " is available");
                    bookButton.setVisible(true);
                    unbookButton.setVisible(false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void bookSeat() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE seats SET isBooked = TRUE WHERE id = ?")) {
            preparedStatement.setInt(1, seatId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                seatLabel.setText("Seat " + seatId + " is booked");
                bookButton.setVisible(false);
                unbookButton.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void unbookSeat() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE seats SET isBooked = FALSE WHERE id = ?")) {
            preparedStatement.setInt(1, seatId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                seatLabel.setText("Seat " + seatId + " is available");
                bookButton.setVisible(true);
                unbookButton.setVisible(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}