import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageScreensPanel extends JPanel {
    private JPanel mainPanel;

    public ManageScreensPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Manage Screens");
        add(label, BorderLayout.NORTH);

        add(new JScrollPane(createScreenListPanel()), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("MainMenu"));
        add(backButton, BorderLayout.SOUTH);
    }

    private JPanel createScreenListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Screens")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int rows = resultSet.getInt("rows");
                int columns = resultSet.getInt("columns");
                boolean is3D = resultSet.getBoolean("is3D");

                JPanel screenPanel = new JPanel(new BorderLayout());
                JLabel screenLabel = new JLabel("ID: " + id);
                JButton viewScreenButton = new JButton("View");
                viewScreenButton.addActionListener(e -> switchPanel("Screen", id));

                screenPanel.add(screenLabel, BorderLayout.CENTER);
                screenPanel.add(viewScreenButton, BorderLayout.EAST);
                panel.add(screenPanel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return panel;
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

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }
}