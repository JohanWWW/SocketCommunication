package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static Scanner _scanner = new Scanner(System.in);
    private static Peer _peer;
    private static int _portNumber;

    public static void main(String[] args) {

        System.out.print("Please enter a port number: ");
        _portNumber = Integer.parseInt(readLine().trim());
        try {
            _peer = new Peer(_portNumber);
        } catch (IOException e) {
            System.err.println("Failed to establish server");
            System.exit(-1);
        }
        _peer.setEstablishConnectionEvent(Main::onPeerReceiveConnection);
        menu();
    }

    private static void menu() {
        while (true) {
            System.out.println("What do you want to do?");
            System.out.println("[0]: Await message");
            System.out.println("[1]: Transmit message");
            System.out.println("[2]: Exit");
            switch (readLine()) {
                case "0" -> {
                    System.out.printf("Awaiting input on port '%s'...\n", _portNumber);
                    try {
                        _peer.awaitConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "1" -> {
                    System.out.print("Please enter a message to transmit: ");
                    String message = readLine();
                    System.out.print("Please enter IP: ");
                    String ipString = readLine().trim();
                    System.out.print("Please enter port number: ");
                    int portNumber = Integer.parseInt(readLine().trim());
                    try {
                        _peer.sendMessage(message, ipString, portNumber);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "2" -> System.exit(0);
                default -> System.out.println("You can only enter integers.");
            }
        }
    }

    private static void onPeerReceiveConnection(EstablishConnectionEventArgs eventArgs) {
        Socket sender = eventArgs.getClient();
        try (InputStream inputStream = sender.getInputStream()) {
            System.out.printf("A connection was established with %s:%s\n", sender.getInetAddress().getHostAddress(), sender.getPort());
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            String receivedMessage = dataInputStream.readUTF();
            System.out.printf("Received message: %s\n", receivedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readLine() {
        return _scanner.nextLine();
    }
}
