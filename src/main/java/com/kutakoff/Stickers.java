package com.kutakoff;

import com.pengrad.telegrambot.request.SendSticker;

public enum Stickers {

    FUNNY_JIM_CARREY(1011661199L, "CAADBQADiQMAAukKyAPZH7wCI2BwFxYE");

    String stickerId;
    Long chatId;

    Stickers(Long chatId, String stickerId) {
        this.chatId = chatId;
        this.stickerId = stickerId;
    }

    public SendSticker getSendSticker() {
        if ("".equals(chatId)) throw new IllegalArgumentException("ChatId cant be null");
        SendSticker sendSticker = new SendSticker(chatId, stickerId);
        return sendSticker;
    }

}
