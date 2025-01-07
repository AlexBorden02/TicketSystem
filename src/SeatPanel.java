import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SeatPanel extends JPanel {
    private JPanel mainPanel;
    private int seatId;
    private int screenId;
    private JLabel seatLabel;


    public SeatPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());

        seatLabel = new JLabel();
        add(seatLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();

        JButton disableButton = new JButton("Disable");
        disableButton.addActionListener(e -> disableSeat());
        buttonPanel.add(disableButton);

        JButton enableButton = new JButton("Enable");
        enableButton.addActionListener(e -> enableSeat());
        buttonPanel.add(enableButton);

        add(buttonPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("ManageScreens", screenId));
        add(backButton, BorderLayout.SOUTH);
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    private void switchPanel(String panelName, int screenId) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
        Component[] components = mainPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ManageScreensPanel) {
                ((ManageScreensPanel) component).setScreenId(screenId);
            }
        }
    }

    public void setSeatInfo(int id, int screenId) {
        this.seatId = id;
        this.screenId = screenId;
        loadSeatData();
    }

    private void loadSeatData() {
        seatLabel.setText("Seat " + seatId);
        revalidate();
        repaint();
    }

    private void disableSeat() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE Seats SET isEnabled = 0 WHERE id = ?")) {
            statement.setInt(1, seatId);
            statement.executeUpdate();
            loadSeatData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void enableSeat() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE Seats SET isEnabled = 1 WHERE id = ?")) {
            statement.setInt(1, seatId);
            statement.executeUpdate();
            loadSeatData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
