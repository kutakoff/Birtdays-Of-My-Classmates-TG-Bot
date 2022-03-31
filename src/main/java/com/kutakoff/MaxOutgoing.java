package com.kutakoff;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendSticker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import static com.kutakoff.EventBot.*;

class MaxOutgoing extends Thread {

    private static final String file = "D:\\usersID.txt";

    public void run() {
            while (true) {
                try {
                    sleep(10000);
                    saveToFile();
                } catch (InterruptedException ignored) {
                }
            }

    }

    private void saveToFile() {
        HashMap<Long, ChatInfo> usersID = EventBot.getUsersID();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<Long, ChatInfo> entry : usersID.entrySet()) {
                //String value = ", user_name = " + "@" + chat.username() +", first_name = " + chat.firstName() + ", last_name = " + chat.lastName() + "\n";
                ChatInfo chatInfo = entry.getValue();
                bufferedWriter.write(CHAT + " = " + entry.getKey() +
                        ", " + USER_NAME + " = " + chatInfo.getUserName() +
                        ", " + FIRST_NAME + " = " + chatInfo.getFirstName() +
                        ", " + LAST_NAME + " = " + chatInfo.getLastName() + "\n"
            );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}