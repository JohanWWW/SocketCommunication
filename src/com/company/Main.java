package com.company;

import gui.view.MainWindow;
import p2p.Peer;
import p2p.events.EstablishConnectionEventArgs;
import p2p.CancellationToken;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private final static Scanner _scanner = new Scanner(System.in);
    private static Peer _peer;
    private static int _portNumber;

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();

        /*System.out.print("Please enter a port number: ");
        _portNumber = Integer.parseInt(readLine().trim());
        _peer = new Peer(_portNumber);
        _peer.addOnEstablishedConnectionSubscriber(Main::onEstablishedConnection);
        start();*/
    }

    private static void start() {
        CancellationToken listeningTask = null;
        while (true) {
            System.out.println("What do you want to do?");
            System.out.println("[0]: Begin listen");
            System.out.println("[1]: Cancel listen");
            System.out.println("[2]: Transmit message");
            System.out.println("[3]: Exit");
            switch (readLine()) {
                case "0" -> {
                    try {
                        listeningTask = new CancellationToken();
                        _peer.beginListen(listeningTask);
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                    System.out.printf("Now listening on port [%s]...\n", _portNumber);
                }
                case "1" -> {
                    if (listeningTask.cancel())
                        System.out.println("Requested cancellation");
                    else
                        System.out.println("Cancellation already requested");
                }
                case "2" -> {
                    System.out.print("Please enter a message to transmit: ");
                    String message = readLine();
                    System.out.print("Please enter IP: ");
                    String ipString = readLine().trim();
                    System.out.print("Please enter port number: ");
                    int portNumber = Integer.parseInt(readLine().trim());
                    try {
                        _peer.transmit(message, ipString, portNumber);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "3" -> {
                    _peer.close();
                    System.exit(0);
                }
                default -> System.out.println("You can only enter integers.");
            }
        }
    }

    private static void onEstablishedConnection(Object eventSource, EstablishConnectionEventArgs args) {
        Socket client = args.getClient();
        System.out.printf("Accepted incoming connection from [%s:%s]\n", client.getInetAddress().getHostAddress(), client.getPort());
        try (InputStream inputStream = client.getInputStream()) {
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
