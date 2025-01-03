import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import javax.swing.border.EmptyBorder;
import java.util.HashSet;

public class CheckoutPanel extends JPanel {
    private JPanel mainPanel;
    private int screenId;
    private String startTime;
    private Set<Integer> selectedSeats = new HashSet<>();

    private JList<String> seatList;
    private DefaultListModel<String> listModel;

    public CheckoutPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Checkout");
        add(label, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        seatList = new JList<>(listModel);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("ManageFilms"));
        add(backButton, BorderLayout.SOUTH);

        JPanel checkoutPanel = new JPanel();
        checkoutPanel.setLayout(new BoxLayout(checkoutPanel, BoxLayout.Y_AXIS));
        checkoutPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        checkoutPanel.add(new JScrollPane(seatList));
        add(checkoutPanel, BorderLayout.CENTER);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> confirmCheckout());
        checkoutPanel.add(confirmButton, BorderLayout.SOUTH);

    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    public void setCheckoutInfo(int screenId, String startTime, Set<Integer> selectedSeats) {
        this.screenId = screenId;
        this.startTime = startTime;
        this.selectedSeats = selectedSeats;

        listModel.clear();
        for (int seat : selectedSeats) {
            listModel.addElement("Seat " + seat);
        }

        revalidate();
        repaint();
    }

    private void confirmCheckout(){
        // todododo
        // book the seats!!!
    }

}