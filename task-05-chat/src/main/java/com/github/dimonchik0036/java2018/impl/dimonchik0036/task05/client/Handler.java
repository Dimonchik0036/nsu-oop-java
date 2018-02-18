package com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.client;

import com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.Message;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class Handler {
    private final Socket socket;
    private final ObjectInput objectInput;
    private final ObjectOutput objectOutput;
    private volatile boolean valid;

    Handler(final Socket socket) throws IOException {
        this.socket = socket;
        objectOutput = new ObjectOutputStream(socket.getOutputStream());
        objectInput = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(final Message message) {
        try {
            objectOutput.writeObject(message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            valid = false;
        }
    }

    public Message readMessage() throws ClassNotFoundException, IOException {
        try {
            return (Message) objectInput.readObject();
        } catch (IOException e) {
            valid = false;
            throw e;
        }
    }

    public boolean isValid() {
        return valid;
    }
}
