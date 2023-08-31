package com.kutakoff;

import com.pengrad.telegrambot.request.SendSticker;

//класс для приколов
public enum Stickers {

    FUNNY_JIM_CARREY(1011661199L, "CAADBQADiQMAAukKyAPZH7wCI2BwFxYE");

    String stickerId;
    Long chatId;

    Stickers(Long chatId, String stickerId) {
        this.chatId = chatId;
        this.stickerId = stickerId;
    }

    public SendSticker getSendSticker() {
        return new SendSticker(chatId, stickerId);
    }
}