import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class ManageScreensPanel extends JPanel {
    private JPanel mainPanel;
    private JLabel screenLabel;
    private int screenId;
    private JPanel gridPanel;
    private Screen currentScreen;

    public ManageScreensPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());

        gridPanel = new JPanel();
        gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        screenLabel = new JLabel();
        add(screenLabel, BorderLayout.NORTH);

        add(gridPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("MainMenu"));
        add(backButton, BorderLayout.SOUTH);
    }

    private void switchPanel(String panelName, int id) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
        Component[] components = mainPanel.getComponents();
        for (Component component : components) {
            if (component instanceof SeatPanel) {
                ((SeatPanel) component).setSeatInfo(id, screenId);
            }
        }
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
        loadScreenData();
    }

    private void loadScreenData() {
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
                screenLabel.setText("Screen " + id + " - " + rows + "x" + columns + " - " + (is3D ? "3D" : "2D"));

                createGrid(rows, columns);
                revalidate();
                repaint();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createGrid(int rows, int columns) {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(rows, columns));

        String query = "SELECT id, isWheelchairAccessible, isEnabled FROM Seats WHERE screenId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, screenId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int seatId = resultSet.getInt("id");
                boolean isWheelchairAccessible = resultSet.getBoolean("isWheelchairAccessible");
                boolean isEnabled = resultSet.getBoolean("isEnabled");

                JButton button = new JButton("Seat " + seatId);
                if (isWheelchairAccessible) {
                    button.setBackground(Color.BLUE);
                } else if (isEnabled) {
                    button.setBackground(Color.GREEN);
                } else {
                    button.setBackground(Color.GRAY);
                }
                button.addActionListener(e -> switchPanel("Seat", seatId));
                gridPanel.add(button);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        revalidate();
        repaint();
    }
}