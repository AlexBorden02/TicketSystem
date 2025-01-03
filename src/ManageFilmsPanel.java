import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManageFilmsPanel extends JPanel {
    private JPanel mainPanel;
    private JList<String> filmList;
    private DefaultListModel<String> listModel;

    public ManageFilmsPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        filmList = new JList<>(listModel);
        add(new JScrollPane(filmList), BorderLayout.CENTER);

        JButton selectButton = new JButton("Select Film");
        selectButton.addActionListener(e -> selectFilm());
        add(selectButton, BorderLayout.SOUTH);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("MainMenu"));
        add(backButton, BorderLayout.NORTH);

        fetchFilmsPlayingSoon();
    }

    private void fetchFilmsPlayingSoon() {
        listModel.clear();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Films WHERE screeningTimes > ?")) {
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            statement.setString(1, currentTime);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String film = resultSet.getString("title");
                listModel.addElement(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void switchPanel(String panelName, int screenId, String startTime) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
        Component[] components = mainPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ScreenPanel) {
                ((ScreenPanel) component).setScreenInfo(screenId, startTime);
            }
        }
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    private void selectFilm() {
        String selectedFilm = filmList.getSelectedValue();

        // fetch screen id for selected film
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Films WHERE title = ?")) {
            statement.setString(1, selectedFilm);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int screenId = resultSet.getInt("screenID");
                String screeningTimes = resultSet.getString("screeningTimes");
                switchPanel("Screen", screenId, screeningTimes);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}