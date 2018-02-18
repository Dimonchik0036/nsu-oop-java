/*
 * NSU Nsk Java 2018
 * Created by Dimonchik0036 on 2018/2/18
 */

package com.github.dimonchik0036.java2018.task05.server;

import com.github.dimonchik0036.java2018.task05.Message;
import com.github.dimonchik0036.java2018.task05.Message.MessageBuilder;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Handler {
    private final ConcurrentHashMap<UserHandler, Boolean> usersMap = new ConcurrentHashMap<>();
    private final ConcurrentSkipListSet<String> userList = new ConcurrentSkipListSet<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final History history = new History(20);

    public void handle(final Socket socket) {
        UserHandler userHandler = null;
        try {
            userHandler = addNewUser(socket);
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }

        handleUser(userHandler);
        removeUser(userHandler);
    }

    private UserHandler addNewUser(final Socket socket) throws IOException {
        UserHandler userHandler = new UserHandler(socket);
        if (userHandler.init() == null) {
            throw new IOException("Login is empty");
        }

        String login = userHandler.getUsername();
        System.out.println("New user: " + login);

        usersMap.put(userHandler, true);
        sendToOtherUsers(userHandler, messageFromAdmin("'" + login + "' connected."));

        userList.add(login);
        sendUserList();
        userHandler.sendMessage(
                new MessageBuilder()
                        .applyType(Message.TYPE_REPLY_HISTORY)
                        .applyText(history.getHistory())
                        .build());

        return userHandler;
    }

    private void handleUser(final UserHandler userHandler) {
        if (userHandler == null) {
            return;
        }

        try {
            while (userHandler.isValid()) {
                processingMessage(userHandler);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

    private void removeUser(final UserHandler userHandler) {
        if (userHandler == null) {
            return;
        }
        usersMap.remove(userHandler);

        String login = userHandler.getUsername();

        String alert = "'" + login + "' disconnected";
        System.out.println(alert);

        sendToOtherUsers(userHandler, messageFromAdmin(alert));

        userList.remove(login);
        sendUserList();

        userHandler.close();
    }

    private void sendUserList() {
        String[] users = new String[userList.size()];
        users = userList.toArray(users);

        sendToAllUsers(new MessageBuilder()
                .applyType(Message.TYPE_USER_LIST)
                .applyUsersList(users)
                .build());
    }

    private void processingMessage(final UserHandler userHandler) throws IOException {
        Message message = userHandler.readMessage();
        System.out.println(message.toJson());

        String type = message.type;
        if (type == null) {
            return;
        }

        switch (type) {
            case Message.TYPE_SEND_MESSAGE:
                sendToOtherUsers(userHandler, message);
                break;
            default:
                System.out.println("Undefined type: " + type);
                break;
        }

    }

    private void sendToAllUsers(final Message message) {
        String json = message.toJson();
        sendToAllUsers(json);
    }

    private void sendToAllUsers(final String json) {
        sendToOtherUsers(null, json);
    }

    private void sendToOtherUsers(final UserHandler current, final Message message) {
        String json = message.toJson();
        if (Objects.equals(message.type, Message.TYPE_SEND_MESSAGE)) {
            history.addMessage(message);
        }

        sendToOtherUsers(current, json);
    }

    private void sendToOtherUsers(final UserHandler current, final String json) {
        usersMap.forEach((userHandler, aBoolean) -> {
            if (userHandler != current) {
                executorService.submit(() -> userHandler.sendMessage(json));
            }
        });
    }

    private static Message messageFromAdmin(final String text) {
        return new MessageBuilder()
                .applyLogin("Admin")
                .applyType(Message.TYPE_SEND_MESSAGE)
                .applyText(text)
                .build();
    }
}
