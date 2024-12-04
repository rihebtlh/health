import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel panel_1;
    private JPanel panel_2;
    private JTextField txtHey;
    private JTextField textField_1;
    private JPasswordField passwordField;
    private JTextField textField_2;
    private JPasswordField passwordField_1;

    ImageIcon Img1 = new ImageIcon("Img/Health_Sync.png");


    public static void main(String[] args) throws SQLException {
    	DatabaseHelper.testConnection();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public Login() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 50, 900, 700);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(450, 0, 436, 850);
        panel.setBackground(new Color(23, 183, 251));
        contentPane.add(panel);
        panel.setLayout(null);

        panel_1 = new JPanel();
        panel_1.setBounds(44, 184, 356, 440);
        panel_1.setBackground(new Color(197, 237, 254));
        panel.add(panel_1);
        panel_1.setLayout(null);

        textField_1 = new JTextField();
        textField_1.setBounds(53, 63, 249, 44);
        panel_1.add(textField_1);
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(53, 169, 249, 44);
        panel_1.add(textField_2);

        passwordField_1 = new JPasswordField();
        passwordField_1.setBounds(53, 281, 249, 42);
        panel_1.add(passwordField_1);

        JButton btnSignup = new JButton("SignUp");
        btnSignup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = textField_1.getText().trim();
                String email = textField_2.getText().trim();
                String password = new String(passwordField_1.getPassword()).trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (DatabaseHelper.registerUser(name, email, password)) {
                    JOptionPane.showMessageDialog(null, "Registration successful!");
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSignup.setForeground(new Color(23, 183, 251));
        btnSignup.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSignup.setBackground(new Color(255, 255, 255));
        btnSignup.setBounds(112, 364, 122, 39);
        panel_1.add(btnSignup);

        JLabel lblNewLabel_1 = new JLabel("Email");
        lblNewLabel_1.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNewLabel_1.setBounds(53, 144, 66, 24);
        panel_1.add(lblNewLabel_1);

        JLabel lblPassword_1 = new JLabel("Password");
        lblPassword_1.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPassword_1.setBounds(53, 258, 98, 24);
        panel_1.add(lblPassword_1);

        JLabel lblNewLabel_1_1 = new JLabel("Full Name");
        lblNewLabel_1_1.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNewLabel_1_1.setBounds(53, 37, 89, 24);
        panel_1.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_2_1 = new JLabel("You don't have an Account?");
        lblNewLabel_1_2_1.setForeground(new Color(255, 255, 255));
        lblNewLabel_1_2_1.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblNewLabel_1_2_1.setBounds(97, 92, 268, 44);
        panel.add(lblNewLabel_1_2_1);

        JLabel lblNewLabel_1_2_1_1 = new JLabel("Create one!");
        lblNewLabel_1_2_1_1.setForeground(Color.WHITE);
        lblNewLabel_1_2_1_1.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNewLabel_1_2_1_1.setBounds(171, 118, 98, 44);
        panel.add(lblNewLabel_1_2_1_1);

        panel_2 = new JPanel();
        panel_2.setBackground(new Color(197, 237, 254));
        panel_2.setBounds(41, 254, 336, 369);
        contentPane.add(panel_2);
        panel_2.setLayout(null);

        txtHey = new JTextField();
        txtHey.setBackground(new Color(9, 165, 187));
        txtHey.setBounds(41, 81, 249, 42);
        panel_2.add(txtHey);
        txtHey.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(9, 165, 187));
        passwordField.setBounds(41, 202, 249, 42);
        panel_2.add(passwordField);

        JButton btnNewButton = new JButton("SignIn");
        btnNewButton.setForeground(new Color(255, 255, 255));
        btnNewButton.setBackground(new Color(23, 183, 251));
        btnNewButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String email = txtHey.getText().trim();
                String password = new String(passwordField.getPassword());

                DatabaseHelper db = new DatabaseHelper();

                if (db.isAdmin(email, password)) {

                    Admin adminPage = new Admin();
                    adminPage.setVisible(true);
                    dispose(); 
                } 
                else if (db.isDoctor(email, password)) {
                    Doctor doctorPage = new Doctor();
                    doctorPage.setVisible(true);
                    dispose();
                }
                else {

                    int userID = db.validateUser(email, password);

                    if (userID != -1) {
                        JOptionPane.showMessageDialog(null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        int loggedInUserID = userID;
                        UserSession.setUserId(loggedInUserID);

                        HomePage homePage = new HomePage(userID);
                        homePage.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnNewButton.setBounds(105, 293, 122, 39);
        panel_2.add(btnNewButton);

        JLabel lblNewLabel = new JLabel("Email");
        lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNewLabel.setBounds(41, 56, 66, 24);
        panel_2.add(lblNewLabel);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPassword.setBounds(41, 181, 98, 24);
        panel_2.add(lblPassword);

        JLabel lblNewLabel_1_2 = new JLabel("Sign In to your Account");
        lblNewLabel_1_2.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblNewLabel_1_2.setBounds(98, 180, 233, 44);
        contentPane.add(lblNewLabel_1_2);

        JLabel Img = new JLabel("");
        Img.setBounds(31, 24, 150, 60);
        ImageIcon resizedImg = new ImageIcon(Img1.getImage().getScaledInstance(150, 60, java.awt.Image.SCALE_SMOOTH));
        Img.setIcon(resizedImg);
        contentPane.add(Img);
    }
}