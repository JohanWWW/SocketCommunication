package p2p.events;

import p2p.Peer;

import java.net.Socket;

public class EstablishConnectionEventArgs {
    private final Peer _serverPeer;
    private final Socket _client;

    public EstablishConnectionEventArgs(Socket client) {
        _client = client;
        _serverPeer = null;
    }

    public EstablishConnectionEventArgs(Peer server, Socket client) {
        _serverPeer = server;
        _client = client;
    }

    /**
     * @return the peer that triggered the event
     */
    public Peer getEventPublisher() {
        return _serverPeer;
    }

    /**
     * @return the client socket
     */
    public Socket getClient() {
        return _client;
    }
}
