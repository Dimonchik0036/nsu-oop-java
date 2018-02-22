/*
 * NSU Nsk Java 2018
 * Created by Dimonchik0036 on 2018/2/18
 */

package com.github.dimonchik0036.java2018.task05;

import com.google.gson.Gson;

import java.io.Reader;

public class Message {
    private static final Gson SERIALIZER = new Gson();

    public static final String TYPE_REGISTRATION = "registration";
    public static final String TYPE_SEND_MESSAGE = "send_message";
    public static final String TYPE_USER_LIST = "user_list";
    public static final String TYPE_REPLY_HISTORY = "reply_history";
    public static final String TYPE_ERROR = "error";

    public String type;
    public String login;
    public String text;
    public String[] users;

    public String getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }

    public String getText() {
        return text;
    }

    public String[] getUsers() {
        return users;
    }

    public static class MessageBuilder {
        private Message body;

        public MessageBuilder() {
            body = new Message();
        }

        public MessageBuilder applyUsersList(final String[] usersList) {
            body.users = usersList;
            return this;
        }

        public MessageBuilder applyType(final String type) {
            body.type = type;
            return this;
        }

        public MessageBuilder applyLogin(final String login) {
            body.login = login;
            return this;
        }

        public MessageBuilder applyText(final String text) {
            body.text = text;
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
