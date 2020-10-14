package com.company;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Peer implements Closeable {
    private final ServerSocket _serverSocket;
    private Consumer<EstablishConnectionEventArgs> _establishConnectionEvent;

    public Peer(int port) throws IOException {
        _serverSocket = new ServerSocket(port);
    }

    public void awaitConnection() throws IOException {
        try (Socket client = _serverSocket.accept()) {
            var eventArgs = new EstablishConnectionEventArgs(this, client);
            raiseOnEstablishConnectionEvent(eventArgs);

        }
    }

    public void sendMessage(String message, String ipAddress, int port) throws IOException {
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

    public void setEstablishConnectionEvent(Consumer<EstablishConnectionEventArgs> event) {
        _establishConnectionEvent = event;
    }

    private void raiseOnEstablishConnectionEvent(EstablishConnectionEventArgs eventArgs) {
        if (_establishConnectionEvent != null) {
            _establishConnectionEvent.accept(eventArgs);
        }
    }

    public void close() throws IOException {
        _establishConnectionEvent = null;
        _serverSocket.close();
    }
}
