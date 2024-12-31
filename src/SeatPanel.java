import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.border.EmptyBorder;

public class SeatPanel extends JPanel{
    private JPanel mainPanel;

    private int seatId;
    private int screenId;

    private JLabel seatLabel;


    public SeatPanel(JPanel mainPanel){
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        seatLabel = new JLabel();
        add(seatLabel, BorderLayout.NORTH);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("ManageScreens"));
        add(backButton, BorderLayout.SOUTH);
    }

    private void switchPanel(String panelName){
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    public void setScreenId(int screenId){
        this.screenId = screenId;
    }

    public void setSeatId(int seatId){
        this.seatId = seatId;
        seatLabel.setText("Seat " + seatId);
        revalidate();
        repaint();
    }
}
