package com.kutakoff;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static com.kutakoff.EventBot.bot;

public class SendMessageAboutEvent extends Thread {

    public void run() {
        while (true) {
            try {
                if (EventBot.events.get(0).getDate().getDayOfYear() - LocalDate.now().getDayOfYear() < 4 &&
                        EventBot.events.get(0).getDate().getDayOfYear() - LocalDate.now().getDayOfYear() >= 0) {
                    if (LocalDateTime.now().getHour() == 12 && LocalDateTime.now().getMinute() == 0) {
                        for (Map.Entry<Long, ChatInfo> hashMap : EventBot.usersID.entrySet()) {
                            MakeMessageAboutEvent.SEND_TEXT.sendMessage(bot, hashMap.getKey());
                        }
                    }
                    sleep(60000);
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}