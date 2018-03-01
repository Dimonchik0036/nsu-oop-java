/*
 * NSU Nsk Java 2018
 * Created by Dimonchik0036 on 2018/2/18
 */

package com.github.dimonchik0036.java2018.task05.client;

import com.github.dimonchik0036.java2018.task05.Message;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class Handler {
    private final Socket socket;
    private final JsonReader reader;
    private final OutputStreamWriter writer;
    private volatile boolean valid = true;

    Handler(final Socket socket) throws IOException {
        this.socket = socket;
        writer = new OutputStreamWriter(socket.getOutputStream());
        reader = new JsonReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
    }

    public void sendMessage(final Message message) {
        sendMessage(message.toJson());
    }

    public void sendMessage(final String message) {
        synchronized (writer) {
            try {
                writer.write(message);
                writer.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                valid = false;
            }
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

    public boolean isValid() {
        return valid;
    }
}
