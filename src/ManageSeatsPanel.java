import javax.swing.*;
import java.awt.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageSeatsPanel extends JPanel {
    private JPanel mainPanel;
    private JPanel buttonPanel;

    public ManageSeatsPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Manage Seats");
        add(label, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        add(buttonPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("MainMenu"));
        add(backButton, BorderLayout.SOUTH);

        loadScreenList();
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

    private void loadScreenList() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Screens");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int screenId = resultSet.getInt("id");
                JButton screenButton = new JButton("Screen " + screenId);
                screenButton.addActionListener(e -> switchPanel("ManageScreens", screenId));
                buttonPanel.add(screenButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}