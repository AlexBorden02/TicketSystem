import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageScreensPanel extends JPanel {
    private JPanel mainPanel;
    private JList<String> screenList;

    public ManageScreensPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Manage Screens");
        add(label, BorderLayout.NORTH);

        screenList = new JList<>(fetchScreens());
        add(new JScrollPane(screenList), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("MainMenu"));
        add(backButton, BorderLayout.SOUTH);
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    private DefaultListModel<String> fetchScreens() {
        DefaultListModel<String> model = new DefaultListModel<>();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Screens")) {

            while (resultSet.next()) {
                int rows = resultSet.getInt("rows");
                int columns = resultSet.getInt("columns");
                boolean is3D = resultSet.getBoolean("is3D");
                model.addElement("ID: " + resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }
}