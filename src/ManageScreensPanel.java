import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        add(new JScrollPane(fetchScreens()), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchPanel("MainMenu"));
        add(backButton, BorderLayout.SOUTH);
    }

    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    private JTable fetchScreens() {
        // model
        //DefaultTableModel model = (DefaultTableModel) screenList.getModel();

        AbstractTableModel testModel = new AbstractTableModel() {
            private Object[][] rows = {{"Button1", new JButton("Button1")},{"Button2", new JButton("Button2")},{"Button3", new JButton("Button3")}, {"Button4", new JButton("Button4")}};
            private String[] columns = {"Count", "Buttons"};

            public String getColumnName(int column) {
                return columns[column];
            }

            @Override
            public int getRowCount() {
                return 0;
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return rows[rowIndex][columnIndex];
            }

            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };

        JTable screenList = new JTable(testModel);
        TableCellRenderer tableRenderer = screenList.getDefaultRenderer(JButton.class);
        TableCellRenderer buttonRenderer = new JTableButtonRenderer(tableRenderer);
        screenList.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableRenderer));

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Screens")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int rows = resultSet.getInt("rows");
                int columns = resultSet.getInt("columns");
                boolean is3D = resultSet.getBoolean("is3D");

                JButton viewScreenButton = new JButton("View");
                viewScreenButton.addActionListener(e -> switchPanel("ScreenPanel"));
                //model.addRow(new Object[]{id, viewScreenButton});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return screenList;
    }
}

class JTableButtonRenderer implements TableCellRenderer {
    private TableCellRenderer defaultRenderer;
    public JTableButtonRenderer(TableCellRenderer renderer) {
        defaultRenderer = renderer;
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value instanceof Component)
            return (Component)value;
        return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}