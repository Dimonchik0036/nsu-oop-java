package com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.server;

import com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.Message;

import java.io.*;
import java.net.Socket;

class UserHandler {
    private final Socket socket;
    private final ObjectInput objectInput;
    private final ObjectOutput objectOutput;
    private volatile boolean valid;

    private User user = null;

    public UserHandler(final Socket socket) throws IOException {
        this.socket = socket;
        objectInput = new ObjectInputStream(socket.getInputStream());
        objectOutput = new ObjectOutputStream(socket.getOutputStream());
        valid = true;
    }

    synchronized public String init() throws IOException, ClassNotFoundException {
        Message message = (Message) objectInput.readObject();
        String login = message.getLogin();
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

    public Message readMessage() throws IOException, ClassNotFoundException {
        synchronized (objectInput) {
            return (Message) objectInput.readObject();
        }
    }

    public void sendMessage(final Message message) {
        synchronized (objectOutput) {
            try {
                objectOutput.writeObject(message);
            } catch (IOException e) {
                valid = false;
            }
        }
    }

    public boolean isValid() {
        return valid;
    }
}
