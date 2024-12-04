import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.*;

public class MedicalHistory extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/healthSync";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";



    ImageIcon Img1 = new ImageIcon("Img/Health_Sync.png");

    public static void main(String[] args) {
        UserSession.setUserId(1);

        EventQueue.invokeLater(() -> {
            try {
                MedicalHistory frame = new MedicalHistory();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Failed to start the application: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public MedicalHistory() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 900);
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
        homeButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
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
        historyButton.setFont(new Font("SansSerif", Font.BOLD, 16));
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
        ImageIcon resizedImg = new ImageIcon(Img1.getImage().getScaledInstance(140, 56, java.awt.Image.SCALE_SMOOTH)); // Resizing the image
        logoLabel.setIcon(resizedImg);
        panel.add(logoLabel);

        addChartPanel(30, 150, "Weight", "Days", "Weight (kg)",
                "SELECT weight FROM health_records WHERE id = ? AND date >= CURDATE() - INTERVAL 7 DAY ORDER BY date DESC", 400, 300);

        addChartPanel(470, 150, "Temperature", "Days", "Temperature (°C)",
                "SELECT temperature FROM health_records WHERE id = ? AND date >= CURDATE() - INTERVAL 7 DAY ORDER BY date DESC", 400, 300);

        addDualChartPanel(30, 470, "Blood Pressure", "Days", "Pressure (mmHg)",
                "SELECT tension_systolic FROM health_records WHERE id = ? AND date >= CURDATE() - INTERVAL 7 DAY ORDER BY date DESC",
                "SELECT tension_diastolic FROM health_records WHERE id = ? AND date >= CURDATE() - INTERVAL 7 DAY ORDER BY date DESC", 840, 300);
    }

    private void addChartPanel(int x, int y, String title, String xAxis, String yAxis, String query, int width, int height) {
        List<Double> yData = fetchData(query); 
        
        if (yData.isEmpty()) {
            JPanel placeholderPanel = new JPanel();
            placeholderPanel.setBounds(x, y, width, height);
            placeholderPanel.setLayout(new BorderLayout());
            placeholderPanel.setBackground(Color.LIGHT_GRAY);

            JLabel placeholderLabel = new JLabel("No data available for " + title, SwingConstants.CENTER);
            placeholderLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            placeholderPanel.add(placeholderLabel, BorderLayout.CENTER);

            contentPane.add(placeholderPanel);
            return;
        }

        List<String> xData = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            xData.add("Day " + (i + 1)); 
        }

        JPanel chartPanel = new JPanel();
        chartPanel.setBounds(x, y, width, height);
        chartPanel.setLayout(new BorderLayout());
        contentPane.add(chartPanel);

        CategoryChart chart = createChart(title, xAxis, yAxis, xData, yData);
        XChartPanel<CategoryChart> chartPanelComponent = new XChartPanel<>(chart);
        chartPanel.add(chartPanelComponent, BorderLayout.CENTER);
    }

    private void addDualChartPanel(int x, int y, String title, String xAxis, String yAxis, String querySystolic, String queryDiastolic, int width, int height) {
        List<Double> systolicData = fetchData(querySystolic);
        List<Double> diastolicData = fetchData(queryDiastolic);

        if (systolicData.isEmpty() && diastolicData.isEmpty()) {
            JPanel placeholderPanel = new JPanel();
            placeholderPanel.setBounds(x, y, width, height);
            placeholderPanel.setLayout(new BorderLayout());
            placeholderPanel.setBackground(Color.LIGHT_GRAY);

            JLabel placeholderLabel = new JLabel("No data available for " + title, SwingConstants.CENTER);
            placeholderLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            placeholderPanel.add(placeholderLabel, BorderLayout.CENTER);

            contentPane.add(placeholderPanel);
            return;
        }

        List<String> xData = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            xData.add("Day " + (i + 1));  // Example for 7 days, or you could use actual dates
        }

        JPanel chartPanel = new JPanel();
        chartPanel.setBounds(x, y, width, height);
        chartPanel.setLayout(new BorderLayout());
        contentPane.add(chartPanel);

        CategoryChart chart = createDualChart(title, xAxis, yAxis, xData, systolicData, diastolicData);
        XChartPanel<CategoryChart> chartPanelComponent = new XChartPanel<>(chart);
        chartPanel.add(chartPanelComponent, BorderLayout.CENTER);
    }

    private CategoryChart createChart(String title, String xAxisTitle, String yAxisTitle, List<String> xData, List<Double> yData) {
        CategoryChart chart = new CategoryChartBuilder().title(title).xAxisTitle(xAxisTitle).yAxisTitle(yAxisTitle).build();

        if (yData.isEmpty() || xData.isEmpty()) {
            throw new IllegalArgumentException("Y-Axis data cannot be empty!");
        }

        chart.addSeries(title, xData, yData);
        return chart;
    }

    private CategoryChart createDualChart(String title, String xAxisTitle, String yAxisTitle, List<String> xData, List<Double> systolicData, List<Double> diastolicData) {
        CategoryChart chart = new CategoryChartBuilder().title(title).xAxisTitle(xAxisTitle).yAxisTitle(yAxisTitle).build();

        // Add systolic and diastolic data
        chart.addSeries("Systolic", xData, systolicData);
        chart.addSeries("Diastolic", xData, diastolicData);
        return chart;
    }

    private List<Double> fetchData(String query) {
        List<Double> data = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, UserSession.getUserId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(resultSet.getDouble(1));
            }

            while (data.size() < 7) {
                data.add(Double.NaN);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Database error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return data;
    }
}

