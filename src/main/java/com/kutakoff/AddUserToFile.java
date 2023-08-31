package com.kutakoff;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import static com.kutakoff.BotConstants.USERS_FILE_PATH;
import static com.kutakoff.EventBot.*;

class AddUserToFile extends Thread {

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
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(USERS_FILE_PATH))) {
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