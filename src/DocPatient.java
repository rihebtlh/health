import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DocPatient extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable healthRecordsTable;
    ImageIcon Img1 = new ImageIcon("Img/Health_Sync.png");

    public DocPatient(int patientId) {
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
        homeButton.setBorderPainted(false);
        panel_1.add(homeButton);
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create an instance of the HomePage
                Doctor doctorH = new Doctor();
                doctorH.setVisible(true);
                dispose();
            }
        });

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
        logoutButton.setBounds(720, 6, 107, 40);
        panel.add(logoutButton);
        logoutButton.setForeground(new Color(23, 183, 251));
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel logoLabel = new JLabel("");
        logoLabel.setBounds(10, 10, 140, 56);
        ImageIcon resizedImg = new ImageIcon(Img1.getImage().getScaledInstance(140, 56, java.awt.Image.SCALE_SMOOTH));
        logoLabel.setIcon(resizedImg);
        panel.add(logoLabel);

        String[] columnNames = { "Weight", "Temperature", "Tension Systolic", "Tension Diastolic", "Date"};
        Object[][] data = fetchHealthRecords(patientId);
        healthRecordsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(healthRecordsTable);
        scrollPane.setBounds(82, 115, 700, 400);
        contentPane.add(scrollPane);
    }

    private Object[][] fetchHealthRecords(int patientId) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT  weight, temperature, tension_systolic, tension_diastolic, date FROM health_records WHERE id = ? ORDER BY date DESC";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                stmt.setInt(1, patientId);
                ResultSet rs = stmt.executeQuery();

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                rs.last();
                int rowCount = rs.getRow();
                rs.beforeFirst();

                Object[][] data = new Object[rowCount][columnCount];
                int rowIndex = 0;
                while (rs.next()) {
                    for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                        data[rowIndex][colIndex - 1] = rs.getObject(colIndex);
                    }
                    rowIndex++;
                }
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[0][5];
    }
}
