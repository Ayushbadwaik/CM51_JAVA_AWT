import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PortScannerSwing extends JFrame implements ActionListener {
    private JTextField ipField, startPortField, endPortField;
    private JTextPane resultPane;
    private JButton scanButton;
    private JPanel portInfoPanel;
    private static final Map<Integer, String> famousPorts = new HashMap<>();

    static {
        // Adding some famous ports and their common services
        famousPorts.put(21, "FTP - File Transfer Protocol");
        famousPorts.put(22, "SSH - Secure Shell");
        famousPorts.put(23, "Telnet - Unencrypted text communications");
        famousPorts.put(25, "SMTP - Simple Mail Transfer Protocol");
        famousPorts.put(53, "DNS - Domain Name System");
        famousPorts.put(80, "HTTP - HyperText Transfer Protocol");
        famousPorts.put(110, "POP3 - Post Office Protocol");
        famousPorts.put(443, "HTTPS - HTTP Secure");
        famousPorts.put(3306, "MySQL Database");
        famousPorts.put(8080, "HTTP Alternate (commonly used for testing)");
    }

    public PortScannerSwing() {
        setTitle("Advanced Port Scanner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // Input panel for fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(230, 230, 250)); // Light lavender color
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("IP Address:"), gbc);

        gbc.gridx = 1;
        ipField = new JTextField(15);
        inputPanel.add(ipField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Start Port (0 to 65535):"), gbc);

        gbc.gridx = 1;
        startPortField = new JTextField(5);
        inputPanel.add(startPortField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("End Port (0 to 65535):"), gbc);

        gbc.gridx = 1;
        endPortField = new JTextField(5);
        inputPanel.add(endPortField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        scanButton = new JButton("Scan Ports");
        scanButton.addActionListener(this);
        scanButton.setBackground(new Color(60, 179, 113)); // Medium sea green
        scanButton.setForeground(Color.WHITE);
        inputPanel.add(scanButton, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Text pane to display results with styled text
        resultPane = new JTextPane();
        resultPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultPane.setEditable(false);
        resultPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(resultPane);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel for famous port information
        portInfoPanel = new JPanel();
        portInfoPanel.setLayout(new GridLayout(0, 1));
        portInfoPanel.setBorder(BorderFactory.createTitledBorder("Famous Ports Information"));
        portInfoPanel.setBackground(new Color(240, 248, 255)); // Alice blue color

        for (Map.Entry<Integer, String> entry : famousPorts.entrySet()) {
            JLabel portLabel = new JLabel("Port " + entry.getKey() + ": " + entry.getValue());
            portInfoPanel.add(portLabel);
        }

        mainPanel.add(portInfoPanel, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        resultPane.setText(""); // Clear previous results
        String ip = ipField.getText();
        int startPort = Integer.parseInt(startPortField.getText());
        int endPort = Integer.parseInt(endPortField.getText());

        appendToPane(resultPane, "Starting scan on IP: " + ip + "\n\n", Color.BLACK);

        for (int port = startPort; port <= endPort; port++) {
            try (Socket socket = new Socket(ip, port)) {
                appendToPane(resultPane, "Port " + port + " is OPEN\n", Color.GREEN);
            } catch (IOException ex) {
                appendToPane(resultPane, "Port " + port + " is CLOSED\n", Color.RED);
            }
        }
    }

    // Helper method to append colored text to the JTextPane
    private void appendToPane(JTextPane pane, String msg, Color color) {
        try {
            // Style for the message
            javax.swing.text.Style style = pane.addStyle("style", null);
            javax.swing.text.StyleConstants.setForeground(style, color);
            pane.getDocument().insertString(pane.getDocument().getLength(), msg, style);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PortScannerSwing scanner = new PortScannerSwing();
            scanner.setVisible(true);
        });
    }
}
