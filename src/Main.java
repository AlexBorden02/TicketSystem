import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Movie Theater Ticket System");

            // screen setup
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new CardLayout());


            JPanel mainPanel = new JPanel(new CardLayout());
            MainMenuPanel mainMenuPanel = new MainMenuPanel(mainPanel);

            ManageScreensPanel manageScreensPanel = new ManageScreensPanel(mainPanel);
            ManageSeatsPanel manageSeatsPanel = new ManageSeatsPanel(mainPanel);
            ManageFilmsPanel manageFilmsPanel = new ManageFilmsPanel(mainPanel);
            ManageBookingsPanel manageBookingsPanel = new ManageBookingsPanel(mainPanel);
            ScreenPanel screenPanel = new ScreenPanel(mainPanel);

            mainPanel.add(mainMenuPanel, "MainMenu");
            mainPanel.add(manageScreensPanel, "ManageScreens");
            mainPanel.add(manageSeatsPanel, "ManageSeats");
            mainPanel.add(manageFilmsPanel, "ManageFilms");
            mainPanel.add(manageBookingsPanel, "ManageBookings");
            mainPanel.add(screenPanel, "Screen");

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}