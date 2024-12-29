import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScreenPanel extends JPanel {
    private JPanel mainPanel;

    private int screenId;
    private JLabel screenLabel;
    private Screen currentScreen;
    private int rows;
    private int columns;
    private boolean is3D;

    public ScreenPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        screenLabel = new JLabel();
        add(screenLabel, BorderLayout.NORTH);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {

            switchPanel("ManageScreens");
        });
        add(backButton, BorderLayout.SOUTH);
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
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
                revalidate();
                repaint();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
