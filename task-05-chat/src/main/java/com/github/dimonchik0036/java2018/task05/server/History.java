/*
 * NSU Nsk Java 2018
 * Created by Dimonchik0036 on 2018/2/19
 */

package com.github.dimonchik0036.java2018.task05.server;

import com.github.dimonchik0036.java2018.task05.Message;

import java.util.ArrayDeque;
import java.util.Queue;

public class History {
    private final Queue<String> history = new ArrayDeque<>();
    private final int capacity;

    History(final int capacity) {
            this.capacity = capacity;
    }

    synchronized void addMessage(final Message message) {
        history.add(message.toString());
        if (history.size() > capacity) {
            history.poll();
        }
    }

    synchronized String getHistory() {
        String[] messages = new String[history.size()];

        int index = 0;
        for (final String entry : history) {
            messages[index++] = entry;
        }

        return String.join("\n", messages);
    }
}
