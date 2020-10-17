package gui.viewmodel;

import p2p.CancellationToken;
import p2p.Peer;
import p2p.events.EstablishConnectionEventArgs;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class MainWindowViewModel extends ViewModelBase {
    private Peer peer;
    private String senderPortNumber;
    private String receiverPortNumber;
    private String ipAddress;
    private String message;

    public static final class Member {
        public static final String senderPortNumber = "senderPortNumber";
        public static final String receiverPortNumber = "receiverPortNumber";
        public static final String ipAddress = "ipAddress";
        public static final String message = "message";
    }

    public MainWindowViewModel() {

    }

    public String getSenderPortNumber() {
        return senderPortNumber;
    }

    public void setSenderPortNumber(String value) {
        setMemberValue(value, senderPortNumber, () -> senderPortNumber = value, Member.senderPortNumber);
    }

    public String getReceiverPortNumber() {
        return receiverPortNumber;
    }

    public void setReceiverPortNumber(String value) {
        setMemberValue(value, receiverPortNumber, () -> receiverPortNumber = value, Member.receiverPortNumber);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String value) {
        setMemberValue(value, ipAddress, () -> ipAddress = value, Member.ipAddress);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        setMemberValue(value, message, () -> message = value, Member.message);
    }

    public void transmitMessage() {
        try {
            peer.transmit(message, ipAddress, Integer.parseInt(receiverPortNumber.trim()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPeer() {
        destroyPreviousPeer();
        peer = new Peer(Integer.parseInt(senderPortNumber.trim()));
        peer.addOnEstablishedConnectionSubscriber(this::onEstablishedConnection);
        try {
            peer.beginListen(new CancellationToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onEstablishedConnection(Object eventSource, EstablishConnectionEventArgs args) {
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
        if (peer != null)
            peer.close(); // This interrupts peer
    }
}
