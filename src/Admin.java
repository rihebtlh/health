import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Admin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    ImageIcon Img1 = new ImageIcon("Img/Health_Sync.png");

    // Database connection constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/healthSync";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private JTable table;  // Declare JTable here

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Admin frame = new Admin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Admin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 871, 609);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(10, 10, 837, 76);
        contentPane.add(panel);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.WHITE);
        panel_1.setBounds(168, 6, 527, 60);
        panel.add(panel_1);
        panel_1.setLayout(null);

        JButton homeButton = new JButton("HOME");
        homeButton.setBackground(Color.WHITE);
        homeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        homeButton.setBounds(206, 10, 85, 41);
        homeButton.setBorderPainted(false); // Removed border
        panel_1.add(homeButton);

        JButton logoutButton = new JButton("LOGOUT");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null, 
                        "Are you sure you want to log out?", 
                        "Logout Confirmation", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);

                if (confirmed == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setBounds(695, 20, 107, 40);
        panel.add(logoutButton);
        logoutButton.setForeground(new Color(23, 183, 251));
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel logoLabel = new JLabel("");
        logoLabel.setBounds(10, 10, 140, 56);
        ImageIcon resizedImg = new ImageIcon(Img1.getImage().getScaledInstance(140, 56, java.awt.Image.SCALE_SMOOTH));
        logoLabel.setIcon(resizedImg);
        panel.add(logoLabel);

        // Create a custom DefaultTableModel to make the table non-editable
        DefaultTableModel tableModel = new DefaultTableModel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                // Allow editing only for the "Action" column
                return column == 2; // "Action" column index
            }
        };

        // Set column names for the table
        tableModel.addColumn("Full Name");
        tableModel.addColumn("userId");  // Hidden column for userId
        tableModel.addColumn("Action");  // Action column

        // Fetch the list of users from the database and populate the table
        fetchUsers(tableModel);

        // Create the JTable to display user data
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setForeground(Color.BLACK);
        table.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));

        // Add the delete button to the Action column in each row
        TableColumn actionColumn = table.getColumn("Action");
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, table));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(132, 190, 601, 330);
        contentPane.add(scrollPane);

        JLabel lblNewLabel = new JLabel("USERS");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel.setBounds(182, 150, 118, 30);
        contentPane.add(lblNewLabel);
    }

    private void fetchUsers(DefaultTableModel tableModel) {
        String query = "SELECT full_name, id FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Clear any previous data in the table
            tableModel.setRowCount(0);

            // Iterate through the result set and add user names to the table
            while (rs.next()) {
                String username = rs.getString("full_name");
                int userId = rs.getInt("id");

                // Add user name, userId (hidden column), and a delete button for the "Action" column
                tableModel.addRow(new Object[]{username, userId, "Delete"});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching users.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser(int userId) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "User deletion failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ButtonRenderer class to render buttons in the Action column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
            setText("Delete");
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // ButtonEditor class to handle button events (delete action)
    class ButtonEditor extends DefaultCellEditor {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JButton button;
        public ButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, JTable table) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    int userId = (int) tableModel.getValueAt(row, 1); // Get the userId from the second column
                    deleteUser(userId);  // Delete the user from the database
                    fetchUsers(tableModel);  // Refresh the table
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}
