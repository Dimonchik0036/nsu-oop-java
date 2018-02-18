package com.github.dimonchik0036.java2018.impl.dimonchik0036.task05;

import com.google.gson.Gson;

public class Message {
    private static final Gson SERIALIZER = new Gson();

    public String login = null;
    public String text = null;
    public boolean config = false;

    public static class MessageBuilder {
        private Message body;

        public MessageBuilder() {
            body = new Message();
        }

        public MessageBuilder applyLogin(final String login) {
            body.login = login;
            return this;
        }

        public MessageBuilder applyMessage(final String text) {
            body.text = text;
            return this;
        }

        public MessageBuilder applyConfig(final boolean config) {
            body.config = config;
            return this;
        }

        public Message build() {
            Message message = body;
            body = null;
            return message;
        }
    }

    public static Message fromJson(final String json) {
        return SERIALIZER.fromJson(json, Message.class);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (login != null) {
            builder.append('[').append(login).append("]: ");
        }

        if (text != null) {
            builder.append(text);
        }

        return builder.toString();
    }

    public String toJson() {
        return SERIALIZER.toJson(this);
    }
}
