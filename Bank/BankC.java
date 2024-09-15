import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class BankC {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {
        Frame frame = new Frame("Banking Application");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2));

        Label label1 = new Label("Account:");
        TextField accountField = new TextField();
        Label label2 = new Label("Amount:");
        TextField amountField = new TextField();
        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");
        Button balanceButton = new Button("Check Balance");
        TextArea resultArea = new TextArea();

        frame.add(label1);
        frame.add(accountField);
        frame.add(label2);
        frame.add(amountField);
        frame.add(depositButton);
        frame.add(withdrawButton);
        frame.add(balanceButton);
        frame.add(resultArea);

        // ActionListener for the deposit button
        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendRequest("DEPOSIT", accountField.getText(), amountField.getText(), resultArea);
            }
        });

        // ActionListener for the withdraw button
        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendRequest("WITHDRAW", accountField.getText(), amountField.getText(), resultArea);
            }
        });

        // ActionListener for the balance button
        balanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendRequest("BALANCE", accountField.getText(), "", resultArea);
            }
        });

        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.exit(0);
            }
        });

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to send requests to the server
    private static void sendRequest(String command, String account, String amount, TextArea resultArea) {
        try {
            out.println(command + " " + account + " " + amount);
            String response = in.readLine();
            resultArea.append(response + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
