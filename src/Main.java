import javax.swing.*;
import java.awt.*;
import javax.swing.UIManager.*;

public class Main {
    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //DatabaseSetup.createTables();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Movie Theater Ticket System");

            // screen setup
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new CardLayout());


            JPanel mainPanel = new JPanel(new CardLayout());
            MainMenuPanel mainMenuPanel = new MainMenuPanel(mainPanel);

            ManageScreensPanel manageScreensPanel = new ManageScreensPanel(mainPanel);
            ManageSeatsPanel manageSeatsPanel = new ManageSeatsPanel(mainPanel);
            ManageFilmsPanel manageFilmsPanel = new ManageFilmsPanel(mainPanel);
            ManageBookingsPanel manageBookingsPanel = new ManageBookingsPanel(mainPanel);
            ScreenPanel screenPanel = new ScreenPanel(mainPanel);
            CheckoutPanel checkoutPanel = new CheckoutPanel(mainPanel);
            SeatPanel seatPanel = new SeatPanel(mainPanel);

            mainPanel.add(mainMenuPanel, "MainMenu");
            mainPanel.add(manageScreensPanel, "ManageScreens");
            mainPanel.add(manageSeatsPanel, "ManageSeats");
            mainPanel.add(manageFilmsPanel, "ManageFilms");
            mainPanel.add(manageBookingsPanel, "ManageBookings");
            mainPanel.add(screenPanel, "Screen");
            mainPanel.add(checkoutPanel, "Checkout");
            mainPanel.add(seatPanel, "Seat");

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}