import javax.swing.*;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomePage extends JFrame {
    private int userID;
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    private JLabel lblUserName;
    private JLabel lblUserEmail;

    ImageIcon Img1 = new ImageIcon("Img/Health_Sync.png");

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HomePage frame = new HomePage(); 
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public HomePage() {
        this(UserSession.getUserId());
    }

    public HomePage(int userID) {
        this.userID = UserSession.getUserId();
        if (this.userID == -1) {
            JOptionPane.showMessageDialog(this, "No user logged in. Please log in first.");
            System.exit(0);
        }
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
        homeButton.setBounds(37, 9, 85, 41);
        homeButton.setBorderPainted(false);
        panel_1.add(homeButton);

        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomePage homePage = new HomePage();
                homePage.setVisible(true);
                dispose();
            }
        });

        JButton notificationsButton = new JButton("NOTIFICATIONS");
        notificationsButton.setBackground(Color.WHITE);
        notificationsButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        notificationsButton.setBounds(160, 11, 145, 40);
        notificationsButton.setBorderPainted(false);
        panel_1.add(notificationsButton);
        notificationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Notifications notificationPage = new Notifications();
                notificationPage.setVisible(true);
                dispose();
            }
        });

        JButton historyButton = new JButton("MEDICAL HISTORY");
        historyButton.setBackground(Color.WHITE);
        historyButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        historyButton.setBounds(315, 12, 183, 40);
        historyButton.setBorderPainted(false);
        panel_1.add(historyButton);

        historyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MedicalHistory medicalHistoryPage = new MedicalHistory();
                medicalHistoryPage.setVisible(true);
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
                    UserSession.setUserId(-1);
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
        
        lblUserName = new JLabel("Full Name: ");
        lblUserName.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblUserName.setBounds(107, 110, 300, 30);
        contentPane.add(lblUserName);

        lblUserEmail = new JLabel("Email: ");
        lblUserEmail.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblUserEmail.setBounds(107, 139, 300, 30);
        contentPane.add(lblUserEmail);

        loadUserData();  

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(Color.WHITE);
        panel_2.setBounds(58, 179, 729, 258);
        contentPane.add(panel_2);
        panel_2.setLayout(null);

        JPanel weightPanel = new JPanel();
        weightPanel.setBackground(new Color(23, 183, 251));
        weightPanel.setBounds(48, 10, 175, 168);
        panel_2.add(weightPanel);
        weightPanel.setLayout(null);

        JLabel weightLabel = new JLabel("Weight");
        weightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        weightLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        weightLabel.setBounds(38, 65, 102, 32);
        weightPanel.add(weightLabel);

        JPanel tempPanel = new JPanel();
        tempPanel.setBackground(new Color(23, 183, 251));
        tempPanel.setBounds(282, 10, 175, 168);
        panel_2.add(tempPanel);
        tempPanel.setLayout(null);

        JLabel tempLabel = new JLabel("Temperature");
        tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tempLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        tempLabel.setBounds(41, 72, 104, 31);
        tempPanel.add(tempLabel);

        JPanel tensionPanel = new JPanel();
        tensionPanel.setBackground(new Color(23, 183, 251));
        tensionPanel.setBounds(514, 10, 175, 168);
        panel_2.add(tensionPanel);
        tensionPanel.setLayout(null);

        JLabel tensionLabel = new JLabel("Tension");
        tensionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tensionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        tensionLabel.setBounds(39, 53, 102, 34);
        tensionPanel.add(tensionLabel);

        JButton addButton = new JButton("Add here");
        addButton.setBackground(new Color(78, 182, 252));
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.setBounds(314, 188, 102, 46);
        panel_2.add(addButton);
        addButton.setForeground(Color.DARK_GRAY);
        
        JButton btnNewButton = new JButton("Save");
        btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    MedicalHistory medicalHistoryPage = new MedicalHistory();
                    medicalHistoryPage.setVisible(true);
                    dispose();
                }
        });
        btnNewButton.setBackground(new Color(78, 182, 252));
        btnNewButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnNewButton.setForeground(Color.DARK_GRAY);
        btnNewButton.setBounds(369, 476, 118, 42);
        contentPane.add(btnNewButton);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            	java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
                String weight = JOptionPane.showInputDialog(null, "Enter the Weight value:", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
                String temperature = JOptionPane.showInputDialog(null, "Enter the Temperature value:", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
                String tension1 = JOptionPane.showInputDialog(null, "Enter the first Tension value (Systolic):", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
                String tension2 = JOptionPane.showInputDialog(null, "Enter the second Tension value (Diastolic):", "Input Dialog", JOptionPane.PLAIN_MESSAGE);

                if (weight != null && !weight.isEmpty() &&
                    temperature != null && !temperature.isEmpty() &&
                    tension1 != null && !tension1.isEmpty() &&
                    tension2 != null && !tension2.isEmpty()) {

                    try {

                        float weightValue = Float.parseFloat(weight);
                        float tempValue = Float.parseFloat(temperature);
                        int tensionSystolic = Integer.parseInt(tension1);
                        int tensionDiastolic = Integer.parseInt(tension2);

                        // Insert into the database
                        try (Connection connection = DatabaseConnection.getConnection()) {
                            String sql = "INSERT INTO health_records (id, weight, temperature, tension_systolic, tension_diastolic, date) VALUES (?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                                statement.setInt(1, userID);     
                                statement.setFloat(2, weightValue);
                                statement.setFloat(3, tempValue);    
                                statement.setInt(4, tensionSystolic);   
                                statement.setInt(5, tensionDiastolic);  
                                statement.setDate(6, new java.sql.Date(currentDate.getTime()));
                                statement.executeUpdate();

                                JOptionPane.showMessageDialog(null, "Data saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                    } catch (NumberFormatException ex) {

                        JOptionPane.showMessageDialog(null, "Please enter valid numeric values for all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {

                    JOptionPane.showMessageDialog(null, "All fields must be filled.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
    }

    private void loadUserData() {
        try (Connection connection = DatabaseHelper.getConnection()) {
            String query = "SELECT full_name, email FROM users WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String userName = resultSet.getString("full_name");
                        String userEmail = resultSet.getString("email");

                        lblUserName.setText("User Name: " + userName);
                        lblUserEmail.setText("User Email: " + userEmail);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage());
        }
    }
}
