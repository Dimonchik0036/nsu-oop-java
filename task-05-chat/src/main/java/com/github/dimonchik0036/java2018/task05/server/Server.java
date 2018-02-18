package com.github.dimonchik0036.java2018.task05.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;

    public Server(final int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void run() {
        Handler handler = new Handler();
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                Thread thread = new Thread(() -> handler.handle(clientSocket));
                thread.start();
            }
        } catch (IOException ignored) {
            System.out.print(ignored.getMessage());
        }
    }

    public static void main(final String[] args) {
        try {
            new Server(13844).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
