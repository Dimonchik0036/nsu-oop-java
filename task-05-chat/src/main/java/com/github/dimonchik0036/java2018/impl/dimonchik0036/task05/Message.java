package com.github.dimonchik0036.java2018.impl.dimonchik0036.task05;

import java.io.Serializable;

public class Message implements Serializable {
    private final String login;
    private final String message;
    private final boolean config;

    public Message(final String login, final String message) {
        this.login = login;
        this.message = message;
        this.config = false;
    }

    public Message(final String login, final String message, final boolean config) {
        this.login = login;
        this.message = message;
        this.config = config;
    }

    public String getLogin() {
        return login;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return "[" + login + "]: " + message;
    }

    public boolean isConfig() {
        return config;
    }
}
