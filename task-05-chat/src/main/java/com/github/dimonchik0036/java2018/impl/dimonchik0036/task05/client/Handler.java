package com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.client;

import com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.Message;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

class Handler {
    private final Socket socket;
    private final BufferedReader reader;
    private final OutputStreamWriter writer;
    private volatile boolean valid;

    Handler(final Socket socket) throws IOException {
        this.socket = socket;
        writer = new OutputStreamWriter(socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(final Message message) {
        sendMessage(message.toJson());
    }

    public void sendMessage(final String message) {
        synchronized (writer) {
            try {
                writer.write(message + "\n");
                writer.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                valid = false;
            }
        }
    }

    public Message readMessage() throws IOException {
        String json;
        try {
            synchronized (reader) {
                json = reader.readLine();
            }
        } catch (IOException e) {
            valid = false;
            throw e;
        }

        return Message.fromJson(json);
    }

    public boolean isValid() {
        return valid;
    }
}
