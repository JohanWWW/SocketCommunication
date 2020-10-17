package p2p.events;

import p2p.Peer;

import java.net.Socket;

public class EstablishConnectionEventArgs extends EventArgs {
    private final Socket _client;

    public EstablishConnectionEventArgs(Socket client) {
        _client = client;
    }

    /**
     * @return the client socket
     */
    public Socket getClient() {
        return _client;
    }
}
