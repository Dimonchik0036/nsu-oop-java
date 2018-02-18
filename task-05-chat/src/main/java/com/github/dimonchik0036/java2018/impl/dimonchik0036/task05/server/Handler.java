package com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.server;

import com.github.dimonchik0036.java2018.impl.dimonchik0036.task05.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Handler {
    private final ConcurrentHashMap<UserHandler, Boolean> usersMap = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void handle(final Socket socket) {
        UserHandler userHandler = null;
        try {
            userHandler = addNewUser(socket);
            handleUser(userHandler);
        } catch (IOException | ClassNotFoundException e) {
            System.out.print(e.getMessage());
        }

        if (userHandler != null) {
            usersMap.remove(userHandler);
        }
        userHandler.close();
    }

    private UserHandler addNewUser(final Socket socket) throws IOException, ClassNotFoundException {
        UserHandler userHandler = new UserHandler(socket);
        userHandler.init();

        return userHandler;
    }

    private void handleUser(final UserHandler userHandler) {
        String login = userHandler.getUser().getLogin();
        System.out.println("New user: " + login);
        usersMap.put(userHandler, true);
        sendOthers(userHandler, new Message("Admin", login + " connected."));

        try {
            while (userHandler.isValid()) {
                Message message = userHandler.readMessage();
                System.out.println(message.toString());
                sendOthers(userHandler, message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        String alert = login + " disconnected";
        System.out.println(alert);
        sendOthers(userHandler, new Message("Admin", alert));
    }

    private void sendOthers(final UserHandler current, final Message message) {
        usersMap.forEach((userHandler, aBoolean) -> {
            if (userHandler != current) {
                executorService.submit(() -> userHandler.sendMessage(message));
            }
        });
    }
}
