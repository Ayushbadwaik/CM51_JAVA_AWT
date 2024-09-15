import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class BankS {
    private static final int PORT = 12345;
    private static ConcurrentHashMap<String, Double> accounts = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started and listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    String[] parts = clientMessage.split(" ");
                    String command = parts[0];
                    String account = parts[1];
                    double amount = Double.parseDouble(parts[2]);

                    switch (command) {
                        case "DEPOSIT":
                            accounts.merge(account, amount, Double::sum);
                            out.println("Deposited " + amount + " to account " + account);
                            break;
                        case "WITHDRAW":
                            accounts.computeIfPresent(account, (k, v) -> {
                                if (v >= amount) {
                                    return v - amount;
                                } else {
                                    out.println("Insufficient funds");
                                    return v;
                                }
                            });
                            out.println("Withdrew " + amount + " from account " + account);
                            break;
                        case "BALANCE":
                            double balance = accounts.getOrDefault(account, 0.0);
                            out.println("Balance for account " + account + ": " + balance);
                            break;
                        default:
                            out.println("Unknown command");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
