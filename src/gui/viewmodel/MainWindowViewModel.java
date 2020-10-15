package gui.viewmodel;

import p2p.CancellationToken;
import p2p.Peer;
import p2p.events.EstablishConnectionEventArgs;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class MainWindowViewModel extends ViewModelBase {
    private Peer _peer;

    private String _senderPortNumber;
    private String _receiverPortNumber;
    private String _ipAddress;
    private String _message;

    public MainWindowViewModel() {

    }

    public String getSenderPortNumber() {
        return _senderPortNumber;
    }

    public void setSenderPortNumber(String value) {
        setProperty(value, _senderPortNumber, () -> _senderPortNumber = value, "senderPortNumber");
    }

    public String getReceiverPortNumber() {
        return _receiverPortNumber;
    }

    public void setReceiverPortNumber(String value) {
        setProperty(value, _receiverPortNumber, () -> _receiverPortNumber = value, "receiverPortNumber");
    }

    public String getIpAddress() {
        return _ipAddress;
    }

    public void setIpAddress(String value) {
        setProperty(value, _ipAddress, () -> _ipAddress = value, "ipAddress");
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String value) {
        setProperty(value, _message, () -> _message = value, "message");
    }

    public void transmitMessage() {
        try {
            _peer.transmit(_message, _ipAddress, Integer.parseInt(_receiverPortNumber.trim()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPeer() {
        destroyPreviousPeer();
        _peer = new Peer(Integer.parseInt(_senderPortNumber.trim()));
        _peer.setEstablishConnectionSubscription(this::onEstablishedConnection);
        try {
            _peer.beginListen(new CancellationToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onEstablishedConnection(EstablishConnectionEventArgs args) {
        Socket client = args.getClient();
        try (InputStream inputStream = client.getInputStream()) {
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            String receivedMessage = dataInputStream.readUTF();
            System.out.println(receivedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void destroyPreviousPeer() {
        if (_peer != null)
            _peer.close(); // This interrupts peer
    }
}
