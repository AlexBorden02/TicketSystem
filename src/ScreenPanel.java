import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import javax.swing.border.EmptyBorder;
import java.util.HashSet;

public class ScreenPanel extends JPanel {
    private JPanel mainPanel;
    private JPanel gridPanel;

    private int screenId;
    private String startTime;

    private JLabel screenLabel;
    private Screen currentScreen;
    private Set<Integer> selectedSeats;

    public ScreenPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.selectedSeats = new HashSet<>();

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        screenLabel = new JLabel();
        topPanel.add(screenLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("ManageFilms"));
        topPanel.add(backButton, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        gridPanel = new JPanel();
        gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(gridPanel, BorderLayout.CENTER);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> checkoutSeats());
        add(checkoutButton, BorderLayout.SOUTH);
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
        fetchScreenData(screenId);
    }

    public void setScreenInfo(int screenId, String startTime) {
        this.screenId = screenId;
        this.startTime = startTime;
        fetchScreenData(screenId);
    }

    private void fetchScreenData(int screenId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Screens WHERE id = ?")) {
            statement.setInt(1, screenId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int rows = resultSet.getInt("rows");
                int columns = resultSet.getInt("columns");
                boolean is3D = resultSet.getBoolean("is3D");

                currentScreen = new Screen(id, rows, columns, is3D);

                createGrid(rows, columns);
                revalidate();
                repaint();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // film info
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Films WHERE screeningTimes = ?")) {
            statement.setString(1, startTime);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                screenLabel.setText("Screen " + screenId + " - " + title + " - " + startTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createGrid(int rows, int columns) {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(rows, columns));

        String query = "SELECT s.id, s.isWheelchairAccessible, b.seatId AS bookedSeatId " +
                       "FROM Seats s LEFT JOIN Bookings b ON s.id = b.seatId " +
                       "WHERE s.screenId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, screenId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int seatId = resultSet.getInt("id");
                int displayedId = Integer.parseInt(String.valueOf(seatId).substring(1));
                boolean isWheelchairAccessible = resultSet.getBoolean("isWheelchairAccessible");
                boolean isBooked = resultSet.getInt("bookedSeatId") > 0;

                JButton button = new JButton("Seat " + displayedId);
                if (isBooked) {
                    button.setBackground(Color.RED);
                    button.setEnabled(false);
                } else if (isWheelchairAccessible) {
                    button.setBackground(Color.BLUE);
                } else {
                    button.setBackground(Color.GREEN);
                }
                button.addActionListener(e -> toggleSeatSelection(seatId, button));
                gridPanel.add(button);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        revalidate();
        repaint();
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    private void toggleSeatSelection(int seatId, JButton button) {
        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            button.setBackground(Color.GREEN);
        } else {
            selectedSeats.add(seatId);
            button.setBackground(Color.YELLOW);
        }
    }

    private void checkoutSeats(){
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one seat to checkout");
            return;
        }

        // switch to checkout panel and pass selected seats
        switchPanel("Checkout", screenId, startTime, selectedSeats);
    }

    private void switchPanel(String panelName, int screenId, String startTime, Set<Integer> selectedSeats) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
        Component[] components = mainPanel.getComponents();
        for (Component component : components) {
            if (component instanceof CheckoutPanel) {
                ((CheckoutPanel) component).setCheckoutInfo(screenId, startTime, selectedSeats);
            }
        }
    }

}