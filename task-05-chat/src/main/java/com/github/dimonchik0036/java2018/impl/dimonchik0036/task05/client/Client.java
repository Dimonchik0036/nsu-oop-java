package com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.client;

import com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.Message;
import com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.Message.MessageBuilder;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private String login;
    private final Handler handler;
    private ChatFrame frame;
    private String text = "";

    public Client(final Socket socket) throws IOException {
        if (socket == null) {
            throw new NullPointerException();
        }

        handler = new Handler(socket);
    }

    public void run() throws IOException {
        if (!init()) {
            return;
        }

        handle();
    }

    private boolean init() throws IOException {
        try {
            login = loginRequest("Enter login:");
        } catch (IOException ignored) {
            login = loginRequest("Incorrect login! Login must be longer than 0 symbols!\nTry again!");
        }

        if (login.isEmpty()) {
            return false;
        }

        frame = new ChatFrame(login);
        handler.sendMessage(new MessageBuilder()
                .applyLogin(login)
                .applyConfig(true)
                .build());

        return true;
    }

    private String loginRequest(final String text) throws IOException {
        String login = JOptionPane.showInputDialog(text);
        if (login == null) {
            return "";
        }

        login = login.trim();
        if (login.isEmpty()) {
            throw new IOException("Incorrect login!");
        }

        return login;
    }

    private void handle() {
        frame.getSendButton().addActionListener(l -> {
            String textField = frame.getText().trim();
            if (!textField.isEmpty()) {
                Message message = new MessageBuilder()
                        .applyLogin(login)
                        .applyMessage(textField)
                        .build();

                handler.sendMessage(message);
                updateMessages(message.toString());
            }

            frame.getTextField().setText(null);

        });

        try {
            while (true) {
                Message message = handler.readMessage();
                updateMessages(message.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateMessages(final String newMessage) {
        synchronized (handler) {
            text += newMessage + '\n';
            frame.getMessagesPane().setText(text);
        }
    }

    public static void main(String[] args) {
        Socket socket;
        try {
            socket = new Socket("127.0.0.1", 13844);
            new Client(socket).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
