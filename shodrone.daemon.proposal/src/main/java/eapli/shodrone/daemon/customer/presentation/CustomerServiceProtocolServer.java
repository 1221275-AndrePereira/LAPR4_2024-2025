package eapli.shodrone.daemon.customer.presentation;

import eapli.shodrone.daemon.customer.requests.ProtocolRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server socket for the customer daemon using the CSV-based protocol.
 * This class is responsible for accepting client connections and handling them
 * in separate threads.
 */
public class CustomerServiceProtocolServer {

    private static final Logger LOGGER = LogManager.getLogger(CustomerServiceProtocolServer.class);

    /**
     * Inner class to handle communication with a single client.
     * Each client connection is managed in its own thread.
     */
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private final MessageParser parser;

        public ClientHandler(final Socket socket, final MessageParser parser) {
            this.clientSocket = socket;
            this.parser = parser;
        }

        @Override
        public void run() {
            final var clientIP = clientSocket.getInetAddress();
            LOGGER.debug("Accepted connection from {}:{}", clientIP.getHostAddress(), clientSocket.getPort());

            try (var out = new PrintWriter(clientSocket.getOutputStream(), true);
                 var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    LOGGER.debug("Received message:----\n{}\n----", inputLine);
                    final ProtocolRequest request = parser.parse(inputLine);
                    final String response = request.execute();
                    out.println(response);
                    LOGGER.debug("Sent message:----\n{}\n----", response);
                    if (request.isGoodbye()) {
                        break;
                    }
                }
            } catch (final IOException e) {
                LOGGER.error("Error in client communication", e);
            } finally {
                try {
                    clientSocket.close();
                    LOGGER.debug("Closing client socket {}:{}", clientIP.getHostAddress(), clientSocket.getPort());
                } catch (final IOException e) {
                    LOGGER.error("While closing the client socket {}:{}", clientIP.getHostAddress(),
                            clientSocket.getPort(), e);
                }
            }
        }
    }

    private final MessageParser parser;

    /**
     * Default constructor. Initializes the message parser.
     */
    public CustomerServiceProtocolServer() {
        this.parser = new MessageParser();
    }

    /**
     * Listens for incoming client connections on the specified port.
     *
     * @param port The port number to listen on.
     */
    @SuppressWarnings("java:S2189") // The server loop is meant to be infinite
    private void listen(final int port) {
        try (var serverSocket = new ServerSocket(port)) {
            while (true) {
                final var clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, parser).start();
            }
        } catch (final IOException e) {
            LOGGER.error("Error starting the server", e);
        }
    }

    /**
     * Starts the server.
     *
     * @param port The port to listen on.
     * @param blocking If true, the server runs in the main thread; otherwise, in a new thread.
     */
    public void start(final int port, final boolean blocking) {
        if (blocking) {
            listen(port);
        } else {
            new Thread(() -> listen(port)).start();
        }
    }
}
