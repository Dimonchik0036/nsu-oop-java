/*
 * NSU Nsk Java 2018
 * Created by Dimonchik0036 on 2018/2/18
 */

package com.github.dimonchik0036.java2018.task05.server;

import com.github.dimonchik0036.java2018.task05.Message;
import com.github.dimonchik0036.java2018.task05.Message.MessageBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class UserHandler {
    private final Socket socket;
    private final JsonReader reader;
    private final OutputStreamWriter writer;
    private volatile boolean valid;

    private String username = null;

    public UserHandler(final Socket socket) throws IOException {
        this.socket = socket;
        reader = new JsonReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        writer = new OutputStreamWriter(socket.getOutputStream());
        valid = true;
    }

    synchronized public String init() throws IOException {
        Message message = readMessage();
        if (!Message.TYPE_REGISTRATION.equals(message.getType())) {
            sendMessage(new MessageBuilder()
                    .applyType(Message.TYPE_ERROR)
                    .applyText("Bad request")
                    .build());
            throw new IOException("Bad request");
        }

        username = message.getLogin();

        return username;
    }

    synchronized public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    public Message readMessage() {
        synchronized (reader) {
            try {
                return Message.fromJson(reader);
            } catch (IOException e) {
                valid = false;

                System.out.println(e.getMessage());
                return Message.EMPTY_MESSAGE;
            }
        }
    }

    public void sendMessage(final Message message) {
        sendMessage(message.toJson());
    }

    public void sendMessage(final String json) {
        synchronized (writer) {
            try {
                writer.write(json);
                writer.flush();
            } catch (IOException e) {
                valid = false;
            }
        }
    }

    public boolean isValid() {
        return valid;
    }

    public String getUsername() {
        return username;
    }
}
