package com.company;

import java.net.ServerSocket;
import java.net.Socket;

public class EstablishConnectionEventArgs {
    private final Peer _server;
    private final Socket _client;

    public EstablishConnectionEventArgs(Socket client) {
        _client = client;
        _server = null;
    }

    public EstablishConnectionEventArgs(Peer server, Socket client) {
        _server = server;
        _client = client;
    }

    public Peer getServer() {
        return _server;
    }

    public Socket getClient() {
        return _client;
    }
}
