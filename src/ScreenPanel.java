import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;

public class ScreenPanel extends JPanel {
    private JPanel mainPanel;
    private JPanel gridPanel;
    private int screenId;
    private JLabel screenLabel;
    private Screen currentScreen;

    public ScreenPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        screenLabel = new JLabel();
        add(screenLabel, BorderLayout.NORTH);

        gridPanel = new JPanel();
        gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(gridPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("ManageScreens"));
        add(backButton, BorderLayout.SOUTH);
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
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

                screenLabel.setText("Screen " + screenId);
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

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Seats WHERE screenId = ?")) {
            statement.setInt(1, screenId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int seatId = resultSet.getInt("id");
                int displayedId = Integer.parseInt(String.valueOf(seatId).substring(1));
                boolean isBooked = resultSet.getBoolean("isBooked");
                boolean isWheelchairAccessible = resultSet.getBoolean("isWheelchairAccessible");

                JButton button = new JButton("Seat " + displayedId);
                if (isBooked) {
                    button.setBackground(Color.RED);
                } else if (isWheelchairAccessible) {
                    button.setBackground(Color.BLUE);
                } else {
                    button.setBackground(Color.GREEN);
                }
                button.addActionListener(e -> switchPanel("Seat", seatId));
                gridPanel.add(button);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        revalidate();
        repaint();

    };

    private void switchPanel(String panelName, int id) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
        Component[] components = mainPanel.getComponents();
        for (Component component : components) {
            if (component instanceof SeatPanel) {
                ((SeatPanel) component).setSeatId(id);
                ((SeatPanel) component).setScreenId(screenId);
            }
        }
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

}