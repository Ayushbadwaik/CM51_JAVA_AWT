import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Random;

public class BLaunch {
    private static HashMap<String, Account> accounts = new HashMap<>();
    private static final String AUTHORITY_ID = "admin123"; // Sample ID for authority
    private static TextArea serverLogArea; // To display server logs

    public static void main(String[] args) {
        Frame launchFrame = new Frame("Banking Application Launcher");
        launchFrame.setSize(400, 200);
        launchFrame.setLayout(new GridLayout(3, 1));
        launchFrame.setBackground(new Color(200, 230, 255));

        Label questionLabel = new Label("Are you a bank authority or server?", Label.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        Button serverButton = new Button("Server");
        serverButton.setBackground(new Color(70, 130, 180));
        serverButton.setForeground(Color.WHITE);
        Button authorityButton = new Button("Authority");
        authorityButton.setBackground(new Color(34, 139, 34));
        authorityButton.setForeground(Color.WHITE);

        launchFrame.add(questionLabel);
        launchFrame.add(serverButton);
        launchFrame.add(authorityButton);

        serverButton.addActionListener(e -> openServerMonitoringFrame());
        authorityButton.addActionListener(e -> promptAuthorityLogin(launchFrame));

        launchFrame.setVisible(true);
        launchFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // Server Monitoring Frame
    private static void openServerMonitoringFrame() {
        Frame serverFrame = new Frame("TASK Monitoring State Bank of India");
        serverFrame.setSize(600, 400);
        serverFrame.setLayout(new BorderLayout());
        serverFrame.setBackground(new Color(240, 248, 255));

        serverLogArea = new TextArea();
        serverLogArea.setEditable(false);
        serverLogArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        serverFrame.add(new Label("Acitivity:", Label.CENTER), BorderLayout.NORTH);
        serverFrame.add(serverLogArea, BorderLayout.CENTER);

        serverLogArea.append("Monitoring started...\n");

        serverFrame.setVisible(true);
        serverFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                serverFrame.dispose();
            }
        });
    }

    // Prompt authority login
    private static void promptAuthorityLogin(Frame launchFrame) {
        Frame loginFrame = new Frame("Authority Login");
        loginFrame.setSize(300, 150);
        loginFrame.setLayout(new GridLayout(3, 1));
        loginFrame.setBackground(new Color(255, 228, 196));

        Label idLabel = new Label("Enter Authority ID:", Label.CENTER);
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));
        TextField idField = new TextField();
        Button loginButton = new Button("Login");
        loginButton.setBackground(new Color(255, 140, 0));
        loginButton.setForeground(Color.WHITE);

        loginFrame.add(idLabel);
        loginFrame.add(idField);
        loginFrame.add(loginButton);

        loginButton.addActionListener(e -> {
            if (AUTHORITY_ID.equals(idField.getText())) {
                loginFrame.dispose();
                promptAuthorityDetails();
            } else {
                showMessage("Invalid ID! Access denied.");
            }
        });

        loginFrame.setVisible(true);
        loginFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                loginFrame.dispose();
            }
        });
    }

    // Prompt for authority details: IP and Name
    private static void promptAuthorityDetails() {
        Frame detailsFrame = new Frame("Authority Details");
        detailsFrame.setSize(300, 200);
        detailsFrame.setLayout(new GridLayout(5, 1));
        detailsFrame.setBackground(new Color(245, 245, 220));

        Label ipLabel = new Label("Enter IP Address:", Label.CENTER);
        ipLabel.setFont(new Font("Arial", Font.BOLD, 12));
        TextField ipField = new TextField();
        Label nameLabel = new Label("Enter Name:", Label.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        TextField nameField = new TextField();
        Button continueButton = new Button("Continue");
        continueButton.setBackground(new Color(46, 139, 87));
        continueButton.setForeground(Color.WHITE);

        detailsFrame.add(ipLabel);
        detailsFrame.add(ipField);
        detailsFrame.add(nameLabel);
        detailsFrame.add(nameField);
        detailsFrame.add(continueButton);

        continueButton.addActionListener(e -> {
            String ip = ipField.getText();
            String name = nameField.getText();
            detailsFrame.dispose();
            openAccountManagementFrame(name, ip);
        });

        detailsFrame.setVisible(true);
        detailsFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                detailsFrame.dispose();
            }
        });
    }

    // Open frame for account management
    private static void openAccountManagementFrame(String authorityName, String authorityIP) {
        Frame accountFrame = new Frame("Account Management");
        accountFrame.setSize(600, 400);
        accountFrame.setLayout(new GridLayout(7, 2));
        accountFrame.setBackground(new Color(224, 255, 255));

        Label nameLabel = new Label("Name:", Label.CENTER);
        TextField nameField = new TextField();
        Label depositLabel = new Label("Initial Deposit:", Label.CENTER);
        TextField depositField = new TextField();
        Button createAccountButton = new Button("Create Account");
        createAccountButton.setBackground(new Color(65, 105, 225));
        createAccountButton.setForeground(Color.WHITE);

        Button withdrawButton = new Button("Withdraw");
        withdrawButton.setBackground(new Color(220, 20, 60));
        withdrawButton.setForeground(Color.WHITE);
        Button depositButton = new Button("Deposit");
        depositButton.setBackground(new Color(34, 139, 34));
        depositButton.setForeground(Color.WHITE);
        Button balanceButton = new Button("Check Balance");
        balanceButton.setBackground(new Color(255, 215, 0));
        balanceButton.setForeground(Color.BLACK);
        TextArea resultArea = new TextArea();
        resultArea.setFont(new Font("Arial", Font.PLAIN, 12));

        accountFrame.add(nameLabel);
        accountFrame.add(nameField);
        accountFrame.add(depositLabel);
        accountFrame.add(depositField);
        accountFrame.add(createAccountButton);
        accountFrame.add(new Label()); // Empty space

        accountFrame.add(withdrawButton);
        accountFrame.add(depositButton);
        accountFrame.add(balanceButton);
        accountFrame.add(resultArea);

        createAccountButton.addActionListener(e -> {
            String name = nameField.getText();
            double initialDeposit = Double.parseDouble(depositField.getText());
            String accountNumber = generateAccountNumber();
            accounts.put(accountNumber, new Account(name, initialDeposit));
            resultArea.append("Account created for " + name + " with Account No: " + accountNumber + "\n");

            // Log account creation to the server frame
            logServerActivity(authorityName, authorityIP, "Account created: " + accountNumber + " for " + name);
        });

        withdrawButton.addActionListener(e -> handleOperation(authorityName, authorityIP, "WITHDRAW", resultArea));
        depositButton.addActionListener(e -> handleOperation(authorityName, authorityIP, "DEPOSIT", resultArea));
        balanceButton.addActionListener(e -> checkBalance(authorityName, authorityIP, resultArea));

        accountFrame.setVisible(true);
        accountFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                accountFrame.dispose();
            }
        });
    }

    // Handle operations other than balance checking
    private static void handleOperation(String authorityName, String authorityIP, String operation, TextArea resultArea) {
        Frame operationFrame = new Frame(operation);
        operationFrame.setSize(300, 200);
        operationFrame.setLayout(new GridLayout(4, 1));
        operationFrame.setBackground(new Color(255, 250, 205));

        Label accountLabel = new Label("Account Number:", Label.CENTER);
        TextField accountField = new TextField();
        Label amountLabel = new Label("Amount:", Label.CENTER);
        TextField amountField = new TextField();
        Button executeButton = new Button("Execute");
        executeButton.setBackground(new Color(123, 104, 238));
        executeButton.setForeground(Color.WHITE);

        operationFrame.add(accountLabel);
        operationFrame.add(accountField);
        operationFrame.add(amountLabel);
        operationFrame.add(amountField);
        operationFrame.add(executeButton);

        executeButton.addActionListener(e -> {
            String accountNo = accountField.getText();
            Account account = accounts.get(accountNo);
            if (account == null) {
                showMessage("Account not found!");
                return;
            }
            double amount = amountField.getText().isEmpty() ? 0 : Double.parseDouble(amountField.getText());
            if (operation.equals("WITHDRAW")) {
                account.withdraw(amount);
                resultArea.append("Withdrawn " + amount + " from Account No: " + accountNo + "\n");
                logServerActivity(authorityName, authorityIP, "Withdrawn " + amount + " from Account No: " + accountNo);
            } else if (operation.equals("DEPOSIT")) {
                account.deposit(amount);
                resultArea.append("Deposited " + amount + " to Account No: " + accountNo + "\n");
                logServerActivity(authorityName, authorityIP, "Deposited " + amount + " to Account No: " + accountNo);
            }
            operationFrame.dispose();
        });

        operationFrame.setVisible(true);
        operationFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                operationFrame.dispose();
            }
        });
    }

    // Simplified balance checking method
    private static void checkBalance(String authorityName, String authorityIP, TextArea resultArea) {
        Frame balanceFrame = new Frame("Check Balance");
        balanceFrame.setSize(300, 150);
        balanceFrame.setLayout(new GridLayout(3, 1));
        balanceFrame.setBackground(new Color(255, 239, 213));

        Label accountLabel = new Label("Account Number:", Label.CENTER);
        TextField accountField = new TextField();
        Button checkButton = new Button("Check");
        checkButton.setBackground(new Color(30, 144, 255));
        checkButton.setForeground(Color.WHITE);

        balanceFrame.add(accountLabel);
        balanceFrame.add(accountField);
        balanceFrame.add(checkButton);

        checkButton.addActionListener(e -> {
            String accountNo = accountField.getText();
            Account account = accounts.get(accountNo);
            if (account == null) {
                showMessage("Account not found!");
                return;
            }
            double balance = account.getBalance();
            resultArea.append("Balance for Account No: " + accountNo + " is " + balance + "\n");
            logServerActivity(authorityName, authorityIP, "Checked balance for Account No: " + accountNo);
            balanceFrame.dispose();
        });

        balanceFrame.setVisible(true);
        balanceFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                balanceFrame.dispose();
            }
        });
    }

    // Generate a random 6-digit account number
    private static String generateAccountNumber() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // Log server activities in the server monitoring frame
    private static void logServerActivity(String authorityName, String authorityIP, String message) {
        if (serverLogArea != null) {
            serverLogArea.append("[SERVER LOG: " + authorityName + " - " + authorityIP + "]: " + message + "\n");
        }
    }

    // Show a simple message dialog
    private static void showMessage(String message) {
        Frame messageFrame = new Frame();
        Dialog dialog = new Dialog(messageFrame, "Message", true);
        dialog.setLayout(new FlowLayout());
        Label label = new Label(message);
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.setVisible(false));
        dialog.add(label);
        dialog.add(okButton);
        dialog.setSize(200, 100);
        dialog.setVisible(true);
    }
}

// Simple Account class
class Account {
    private String name;
    private double balance;

    public Account(String name, double initialDeposit) {
        this.name = name;
        this.balance = initialDeposit;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            this.balance -= amount;
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    public double getBalance() {
        return this.balance;
    }
}
