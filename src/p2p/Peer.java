package p2p;

import p2p.events.EstablishConnectionEventArgs;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Peer implements Closeable {
    private int _port;
    private ServerSocket _serverSocket;
    private Consumer<EstablishConnectionEventArgs> _establishConnectionSubscription;

    /**
     *
     * @param port The port number that the peer will be listening to
     */
    public Peer(int port) {
        _port = port;
    }

    /**
     * Starts a background task which awaits incoming connections
     * @param cancellationToken for interrupting the background task
     * @throws IOException if fails to create server on given port number
     */
    public void beginListen(CancellationToken cancellationToken) throws IOException {
        _serverSocket = new ServerSocket(_port);

        CompletableFuture.runAsync(() -> {
            while (!cancellationToken.isCancellationRequested()) {
                try (Socket client = _serverSocket.accept()) {
                    var eventArgs = new EstablishConnectionEventArgs(this, client);
                    raiseOnEstablishConnectionEvent(eventArgs);
                } catch(SocketException e) {
                    // Is thrown when server socket is closed while listening for incoming connections
                    // Do nothing
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // On cancel, close server socket so that it stops listening for incoming connections
        cancellationToken.setCancellationRequestSubscription(this::closeServerSocket);
    }

    /**
     * Transmits a message to a target socket
     * @param message The message to send
     * @param ipAddress The target ip address
     * @param port The target port number
     * @throws IOException
     */
    public void transmit(String message, String ipAddress, int port) throws IOException {
        Socket target = new Socket(ipAddress, port);
        OutputStream outputStream = target.getOutputStream();
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        }
    }

    public String getAddress() {
        return _serverSocket.getLocalSocketAddress().toString();
    }

    /**
     * Creates an event subscription
     */
    public void setEstablishConnectionSubscription(Consumer<EstablishConnectionEventArgs> subscriber) {
        _establishConnectionSubscription = subscriber;
    }

    public void close() {
        try {
            if (_serverSocket != null)
                _serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void raiseOnEstablishConnectionEvent(EstablishConnectionEventArgs eventArgs) {
        if (_establishConnectionSubscription != null) {
            _establishConnectionSubscription.accept(eventArgs);
        }
    }

    private void closeServerSocket() {
        try {
            _serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

