/*
 * NSU Nsk Java 2018
 * Created by Dimonchik0036 on 2018/2/18
 */

package com.github.dimonchik0036.java2018.task05.client;

import com.github.dimonchik0036.java2018.task05.Message;

import javax.swing.JOptionPane;
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
        handler.sendMessage(new Message.MessageBuilder()
                .applyLogin(login)
                .applyType(Message.TYPE_REGISTRATION)
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
                Message message = new Message.MessageBuilder()
                        .applyLogin(login)
                        .applyText(textField)
                        .applyType(Message.TYPE_SEND_MESSAGE)
                        .build();

                handler.sendMessage(message);
                updateMessages(message.toString());
            }

            frame.getTextField().setText(null);

        });

        try {
            while (true) {
                Message message = handler.readMessage();
                handleMessages(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMessages(final Message message) {
        if (message == null) {
            System.out.println("Empty message");
            return;
        }

        System.out.println(message.toJson());
        String type = message.type;
        if (type == null) {
            return;
        }

        switch (type) {
            case Message.TYPE_SEND_MESSAGE:
                updateMessages(message.toString());
                break;
            case Message.TYPE_USER_LIST:
                setOnlineUsers(message.users);
                break;
            case Message.TYPE_REPLY_HISTORY:
                updateMessages(message.text);
                break;
            default:
                System.out.println("Undefined type: " + type);
                break;
        }
    }

    private void updateMessages(final String newMessage) {
        if (newMessage == null) {
            System.out.println("Empty message from update");
            return;
        }

        synchronized (handler) {
            text += newMessage + '\n';
            frame.getMessagesPane().setText(text);
        }
    }

    private void setOnlineUsers(final String[] users) {
        if (users == null) {
            System.out.println("Empty user list");
            return;
        }

        String text = String.join("\n", users);
        frame.getOnlineUsersList().setText(text);
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
