package com.kutakoff;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import java.time.LocalDate;

public enum MakeMessageAboutEvent {

    SEND_TEXT(1011661199L, "Пример текста");

    final String sendText;
    final Long chatId;

    MakeMessageAboutEvent(Long chatId, String sendText) {
        this.chatId = chatId;
        this.sendText = sendText;
    }

    public void sendMessage(TelegramBot bot, Long chatId) {
        int difference = (EventBot.events.get(0).getDate().getDayOfYear() - LocalDate.now().getDayOfYear());
        if (difference > 0) {
            bot.execute(new SendMessage(chatId, ("У " + EventBot.events.get(0).getName() + " день рождение через " + difference + " дня(-ей)!")));
        } else if (difference == 0) {
            bot.execute(new SendMessage(chatId, ("У " + EventBot.events.get(0).getName() + " сегодня день рождение!")));
        }
    }
}