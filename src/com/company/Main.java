package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {
        Peer peer;
        try {
            peer = new Peer(7777);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        peer.setEstablishConnectionEvent(Main::onPeerReceiveConnection);

        while (true) {
            System.out.println("Listening on specified port...");
            try {
                peer.awaitConnection();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private static void onPeerReceiveConnection(EstablishConnectionEventArgs eventArgs) {
        Peer receiver = eventArgs.getServer();
        Socket sender = eventArgs.getClient();
        try (InputStream inputStream = sender.getInputStream()) {
            System.out.printf("Receive data from sender -> %s:%s\n", sender.getInetAddress().getHostAddress(), sender.getPort());
            /*DataInputStream dataInputStream = new DataInputStream(inputStream);
            String message = dataInputStream.readUTF();
            System.out.printf("Received message: %s\n", message);*/
            byte[] messageLengthBytes = new byte[4];
            inputStream.read(messageLengthBytes, 0, 4);
            int messageLength = (((messageLengthBytes[3] & 0xff) << 24) | ((messageLengthBytes[2] & 0xff) << 16) |
                      ((messageLengthBytes[1] & 0xff) << 8) | (messageLengthBytes[0] & 0xff));
            byte[] messageBuffer = new byte[messageLength];
            inputStream.read(messageBuffer, 0, messageLength);
            String message = new String(messageBuffer, 0, messageLength);
            System.out.printf("Received message: %s\n", message);
        } catch (IOException e) {
            System.err.println("Could not get input stream");
        }

        String senderIp = sender.getInetAddress().getHostAddress();
        int senderPort = sender.getPort();

        try {
            receiver.sendMessage("Your message was received!", senderIp, senderPort);
        } catch (IOException e) {
            System.err.println("Could not send confirmation message");
        }
    }
}
