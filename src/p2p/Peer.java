package p2p;

import events.EventArgs;
import p2p.events.EstablishConnectionEventArgs;
import events.Event;
import events.EventSubscriber;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.concurrent.CompletableFuture;

public class Peer implements Closeable {
    private final int port;
    private ServerSocket serverSocket;

    private final Event<EstablishConnectionEventArgs> onEstablishedConnection;

    /**
     *
     * @param port The port number that the peer will be listening to
     */
    public Peer(int port) {
        this.port = port;
        onEstablishedConnection = new Event<>(this);
    }

    /**
     * Starts a background task which awaits incoming connections
     * @param cancellationToken for interrupting the background task
     * @throws IOException if fails to create server on given port number
     */
    public void beginListen(CancellationToken cancellationToken) throws IOException {
        serverSocket = new ServerSocket(port);

        CompletableFuture.runAsync(() -> {
            while (!cancellationToken.isCancellationRequested()) {
                try (Socket client = serverSocket.accept()) {
                    var eventArgs = new EstablishConnectionEventArgs(client);
                    onEstablishedConnection.raise(eventArgs);
                } catch(SocketException e) {
                    // Is thrown when server socket is closed while listening for incoming connections
                    // Do nothing
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // On cancel, close server socket so that it stops listening for incoming connections
        cancellationToken.addCancellationRequestedSubscriber(this::onServerSocketClosed);
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
        return serverSocket.getLocalSocketAddress().toString();
    }

    /**
     * Adds an event subscription to the event handler
     * @param subscriber
     */
    public void addOnEstablishedConnectionSubscriber(EventSubscriber<EstablishConnectionEventArgs> subscriber) {
        onEstablishedConnection.subscribe(subscriber);
    }

    public void close() {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onServerSocketClosed(Object eventSource, EventArgs args) {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

