package com.github.dimonchik0036.java2018.task05.server;

import com.github.dimonchik0036.java2018.task05.Message;

import java.io.*;
import java.net.Socket;

class UserHandler {
    private final Socket socket;
    private final BufferedReader reader;
    private final OutputStreamWriter writer;
    private volatile boolean valid;

    private User user = null;

    public UserHandler(final Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new OutputStreamWriter(socket.getOutputStream());
        valid = true;
    }

    synchronized public String init() throws IOException {
        Message message = readMessage();
        String login = message.login;
        user = new User(login);

        return login;
    }

    synchronized public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    public User getUser() {
        return user;
    }

    public Message readMessage() throws IOException {
        synchronized (reader) {
            return Message.fromJson(reader.readLine());
        }
    }

    public void sendMessage(final Message message) {
        sendMessage(message.toJson());
    }

    public void sendMessage(final String json) {
        synchronized (writer) {
            try {
                writer.write(json + "\n");
                writer.flush();
            } catch (IOException e) {
                valid = false;
            }
        }
    }

    public boolean isValid() {
        return valid;
    }
}
