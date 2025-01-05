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
    private Film film;
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

    public void setCheckoutInfo(int screenId, Film film, Set<Integer> selectedSeats) {
        this.screenId = screenId;
        this.film = film;
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

        for (int seat : selectedSeats) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO Bookings (filmID, seatID) VALUES (?, ?)")) {
                statement.setInt(1, film.getId());
                statement.setInt(2, seat);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        JOptionPane.showMessageDialog(this, "Seats booked successfully!");
        switchPanel("ManageFilms");
    }

}