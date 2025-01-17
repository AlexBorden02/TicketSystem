import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private JPanel mainPanel;

    public MainMenuPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new GridLayout(4, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton manageSeatsButton = new JButton("Manage Seats");
        manageSeatsButton.addActionListener(e -> switchPanel("ManageSeats"));

        JButton manageFilmsButton = new JButton("Manage Films");
        manageFilmsButton.addActionListener(e -> switchPanel("ManageFilms"));

        JButton manageBookingsButton = new JButton("Manage Bookings");
        manageBookingsButton.addActionListener(e -> switchPanel("ManageBookings"));

        add(manageSeatsButton);
        add(manageFilmsButton);
        add(manageBookingsButton);
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }
}