/*
 * NSU Nsk Java 2018
 * Created by Dimonchik0036 on 2018/2/18
 */

package com.github.dimonchik0036.java2018.task05.client;

import javax.swing.JTextPane;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ChatFrame extends JFrame {
    private JTextField messageField;
    private JTextPane messagesArea;
    private JTextPane onlineUsers;
    private JButton sendButton;

    public ChatFrame(final String login) {
        JPanel chatPanel = new JPanel();
        setTitle("Chat - " + "[" + login + "]");
        add(chatPanel);
        messageField = new JTextField();

        chatPanel.setLayout(new GridBagLayout());
        chatPanel.setBackground(new Color(13, 102, 38));
        chatPanel.setEnabled(true);
        chatPanel.setFont(new Font("Comic Sans MS", chatPanel.getFont().getStyle(), chatPanel.getFont().getSize()));

        messagesArea = new JTextPane();
        JScrollPane messagesPane = new JScrollPane(messagesArea);
        messagesPane.setVisible(true);
        messagesArea.setEditable(false);

        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 300;
        gbc.ipady = 380;
        gbc.insets = new Insets(10, 10, 5, 10);
        chatPanel.add(messagesPane, gbc);
        sendButton = new JButton();
        sendButton.setText("Send");
        sendButton.setVerticalAlignment(0);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 0, 10, 10);
        chatPanel.add(sendButton, gbc);
        messageField.setColumns(30);
        messageField.setEditable(true);
        messageField.setEnabled(true);

        JRootPane rootPane = SwingUtilities.getRootPane(sendButton);
        rootPane.setDefaultButton(sendButton);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 50.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 5;
        gbc.insets = new Insets(5, 10, 10, 10);
        chatPanel.add(messageField, gbc);

        final JLabel label1 = new JLabel();
        label1.setEnabled(true);
        label1.setFont(new Font("Comic Sans MS", label1.getFont().getStyle(), 14));
        label1.setForeground(new Color(0, 255, 0));
        label1.setText("Online");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        chatPanel.add(label1, gbc);

        onlineUsers = new JTextPane();
        onlineUsers.setEditable(false);
        JScrollPane onlineUsersPane = new JScrollPane(onlineUsers);
        onlineUsersPane.setVisible(true);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 50.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 5, 10);
        chatPanel.add(onlineUsersPane, gbc);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300, 100, 620, 500);
        chatPanel.setVisible(true);
        setResizable(false);
        setVisible(true);

    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JTextField getTextField() {
        return messageField;
    }

    public JTextPane getMessagesPane() {
        return messagesArea;
    }

    public JTextPane getOnlineUsersList() {
        return onlineUsers;
    }

    public String getText() {
        return messageField.getText();
    }

}
