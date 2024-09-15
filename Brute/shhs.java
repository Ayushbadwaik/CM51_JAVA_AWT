import java.io.*;
import java.net.*;

public class shhs {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server started on port 8080");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read request
            String line;
            StringBuilder request = new StringBuilder();
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                request.append(line).append("\n");
            }

            // Parse request
            String requestString = request.toString();
            String responseMessage;
            // Extract password from URL
            String query = requestString.split(" ")[1];
            String[] params = query.split("\\?")[1].split("&");
            String password = null;
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue[0].equals("password")) {
                    password = URLDecoder.decode(keyValue[1], "UTF-8");
                }
            }

            // Validate password
            if ("4205".equals(password)) {
                responseMessage = "Correct password!";
                writer.println("HTTP/1.1 200 OK");
            } else {
                responseMessage = "Incorrect password. Try again.";
                writer.println("HTTP/1.1 401 Unauthorized");
            }

            // Send response
            writer.println("Content-Type: text/plain");
            writer.println();
            writer.println(responseMessage);

            clientSocket.close();
        }
    }
}
