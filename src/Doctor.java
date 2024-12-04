import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable patientTable;
    ImageIcon Img1 = new ImageIcon("Img/Health_Sync.png");

    public Doctor() {
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
        homeButton.setBounds(230, 10, 85, 41);
        homeButton.setBorderPainted(false);
        panel_1.add(homeButton);
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        logoutButton.setBounds(720, 10, 107, 40);
        panel.add(logoutButton);
        logoutButton.setForeground(new Color(23, 183, 251));
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel logoLabel = new JLabel("");
        logoLabel.setBounds(10, 10, 140, 56);
        ImageIcon resizedImg = new ImageIcon(Img1.getImage().getScaledInstance(140, 56, java.awt.Image.SCALE_SMOOTH));
        logoLabel.setIcon(resizedImg);
        panel.add(logoLabel);

        String[] columnNames = {"Patients"};
        Object[][] data = fetchPatientNames();
        patientTable = new JTable(data, columnNames) {
           
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing
            }
        };
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBounds(102, 131, 700, 300);
        contentPane.add(scrollPane);

        JButton viewHistoryBtn = new JButton("View Medical History");
        viewHistoryBtn.setBackground(new Color(255, 255, 255));
        viewHistoryBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewHistoryBtn.setBounds(169, 450, 200, 30);
        viewHistoryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String patientName = (String) patientTable.getValueAt(selectedRow, 0);
                    int patientId = DatabaseHelper.getPatientIdByName(patientName);
                    if (patientId != -1) {
                        DocPatient docPatientPage = new DocPatient(patientId);
                        docPatientPage.setVisible(true);
                        dispose();
                    }
                }
            }
        });
        contentPane.add(viewHistoryBtn);

        // Good Button
        JButton btnGood = new JButton("Good");
        btnGood.setBackground(Color.WHITE);
        btnGood.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGood.setBounds(432, 450, 100, 30);
        btnGood.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String patientName = (String) patientTable.getValueAt(selectedRow, 0);
                    int patientId = DatabaseHelper.getPatientIdByName(patientName);
                    if (patientId != -1) {
                        boolean success = DatabaseHelper.sendNotification(patientId, "Good: Keep taking care of yourself");
                        if (success) {
                            System.out.println("Good notification sent.");
                        } else {
                            System.out.println("Failed to send Good notification.");
                        }
                    }
                }
            }
        });
        contentPane.add(btnGood);

        JButton btnAlert = new JButton("Alert");
        btnAlert.setBackground(Color.WHITE);
        btnAlert.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAlert.setBounds(615, 450, 100, 30);
        btnAlert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String patientName = (String) patientTable.getValueAt(selectedRow, 0);
                    int patientId = DatabaseHelper.getPatientIdByName(patientName);
                    if (patientId != -1) {
                        boolean success = DatabaseHelper.sendNotification(patientId, "Alert: Please check the hospital immediately");
                        if (success) {
                            System.out.println("Alert notification sent.");
                        } else {
                            System.out.println("Failed to send Alert notification.");
                        }
                    }
                }
            }
        });
        contentPane.add(btnAlert);
    }

    private Object[][] fetchPatientNames() {

        List<String> patientNames = DatabaseHelper.fetchPatientNames();

        if (patientNames == null || patientNames.isEmpty()) {
            return new Object[0][0];
        }

        List<String> filteredNames = new ArrayList<>();
        for (String patientName : patientNames) {
            int patientId = DatabaseHelper.getPatientIdByName(patientName);
            if (patientId != 7 && patientId != 8) {
                filteredNames.add(patientName);
            }
        }

        Object[][] data = new Object[filteredNames.size()][1];
        for (int i = 0; i < filteredNames.size(); i++) {
            data[i][0] = filteredNames.get(i);
        }
        return data;
    }

}


