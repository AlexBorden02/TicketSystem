import javax.swing.*;
import java.awt.*;

public class ManageSeatsPanel extends JPanel {
    private JPanel mainPanel;

    public ManageSeatsPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Manage Seats");
        add(label, BorderLayout.NORTH);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("MainMenu"));
        add(backButton, BorderLayout.SOUTH);
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }
}