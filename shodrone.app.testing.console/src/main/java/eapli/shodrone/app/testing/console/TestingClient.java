package eapli.shodrone.app.testing.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestingClient {

    private static String SERVER_IP;
    private static int SERVER_PORT;

//    private static final String SERVER_IP = Application.settings().serverHost();
//    private static final Integer SERVER_PORT = Application.settings().serverPort();

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public TestingClient(String serverIP, int serverPort) {
        SERVER_IP = serverIP;
        SERVER_PORT = serverPort;
    }

    public TestingClient() {
        SERVER_IP = "127.0.0.1";
        SERVER_PORT = 8080;
    }

    /**
     * Connects to the server daemon.
     * @return true if the connection is successful, false otherwise.
     */
    public boolean connect() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (IOException e) {
            System.out.println("Error connecting to the server: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sends a request to the server and returns the full multi-line response.
     * @param request The request string to send.
     * @return The server's response.
     * @throws IOException If there is a communication error.
     */
    public String sendRequest(String request) throws IOException {
        out.println(request);
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.trim().isEmpty()) {
            response.append(line).append("\n");
        }
        return response.toString();
    }

    /**
     * Disconnects from the server, closing all resources.
     * This method ensures that the socket and streams are properly closed
     */
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error disconnecting from the server: " + e.getMessage());
        }
    }
}
