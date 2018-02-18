/*
 * NSU Nsk Java 2018
 * Created by Dimonchik0036 on 2018/2/18
 */

package com.github.dimonchik0036.java2018.task05.client;

import com.github.dimonchik0036.java2018.task05.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
