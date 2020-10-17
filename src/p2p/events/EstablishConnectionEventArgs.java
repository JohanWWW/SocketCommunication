package p2p.events;

import events.EventArgs;

import java.net.Socket;

public class EstablishConnectionEventArgs extends EventArgs {
    private final Socket client;

    public EstablishConnectionEventArgs(Socket client) {
        this.client = client;
    }

    /**
     * @return the client socket
     */
    public Socket getClient() {
        return client;
    }
}
