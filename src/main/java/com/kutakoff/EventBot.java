package com.kutakoff;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.kutakoff.BotConstants.NAME_AND_BIRTHDAYS;
import static com.kutakoff.BotConstants.USERS_FILE_PATH;

public class EventBot {
    private static final String TOKEN = "5205856249:AAGN957nSTUq4gMCtx991IhL_k62ht2xRCU";
    public static TelegramBot bot = new TelegramBot(TOKEN);

    private static final Logger log = Logger.getLogger(EventBot.class.getName());

    public static HashMap<Long, ChatInfo> usersID = new HashMap<>();
    public static List<Person> events = new ArrayList<>();

    public static Chat chat;
    public static Long chatId;

    public static String CHAT = "Id чата";
    public static String USER_NAME = "user_name";
    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";

    public static HashMap<Long, ChatInfo> getUsersID() {
        return usersID;
    }

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        getBirthdays();

        events.sort(Comparator.comparing(Person::getDate));
        //операции с id пользователя в файле
        addUsersIdToFile();
        AddUserToFile addUserToFile = new AddUserToFile();
        addUserToFile.start();

        //отправка сообщении о событии
        SendMessageAboutEvent sendMessageAboutEvent = new SendMessageAboutEvent();
        sendMessageAboutEvent.start();

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                chat = update.message().chat();
                chatId = update.message().chat().id();
                String text = update.message().text();
                if (update.message() != null && update.message().chat() != null) {
                    try {
                        //проверка что отправил пользователь
                        EventBot.checkUserIn(text, bot, formatter, events);
                        //отправить Максу чат того, кто пользовался ботом
                        EventBot.sendUsersIdToMe(bot);
                        //положить id пользователей в HashMap
                        EventBot.putUsersIdToHashMap();
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "ERROR", e);
                        bot.execute(new SendMessage(chatId, "Ошибка в работе BOT-сервиса, обратитесь к разработчику на почту: kutakovmax@gmail.com"));
                    }
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static void checkUserIn(String text, TelegramBot bot, DateTimeFormatter formatter, List<Person> events) {
        if ("/start".equalsIgnoreCase(text)) {
            bot.execute(new SendMessage(chatId, "Введите:\n/first, чтобы вывести ближайшую дату дня рождения человека.\n/all, чтобы вывести все будующие дни рождения человека."));
        } else if ("/first".equalsIgnoreCase(text)) {
            bot.execute(new SendMessage(chatId, "Ближайшая дата дня рождения: " + formatter.format(events.get(0).getDate()) + " --> " + events.get(0).getName()));
        } else if ("/all".equalsIgnoreCase(text)) {
            StringBuilder sb = new StringBuilder("Будущие дни рождения:");
            for (Person person : events) {
                sb.append("\n").append(formatter.format(person.getDate())).append(" --> ").append(person.getName());
            }
            bot.execute(new SendMessage(chatId, sb.toString()));
        } else {
            bot.execute(new SendMessage(chatId, "Введите:\n/first, чтобы вывести ближайшую дату дня рождения человека.\n/all, чтобы вывести все будующие дни рождения человека."));
        }

    }

    private static void sendUsersIdToMe(TelegramBot bot) {
        bot.execute(new SendMessage(1011661199, "Id чата = " + chatId +
                (chat.username() != null ? ", user_name = @" + chat.username() : "") +
                (chat.firstName() != null ? ", first_name = " + chat.firstName() : "") +
                (chat.lastName() != null ? ", last_name = " + chat.lastName() : "")
        ));
    }

    private static void addUsersIdToFile() {
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(USERS_FILE_PATH))) {
            while ((line = bufferedReader.readLine()) != null) {
                if (!"".equals(line)) {
                    String[] requisites = line.split(",");
                    String userName = null;
                    String firstName = null;
                    String lastName = null;
                    for (String item : requisites) {
                        String[] keyValue = item.split("=");
                        if (CHAT.equals(keyValue[0].trim())) {
                            chatId = Long.parseLong(keyValue[1].trim());
                        } else if (USER_NAME.equals(keyValue[0].trim())) {
                            userName = keyValue[1].trim();
                        } else if (FIRST_NAME.equals(keyValue[0].trim())) {
                            firstName = keyValue[1].trim();
                        } else if (LAST_NAME.equals(keyValue[0].trim())) {
                            lastName = keyValue[1].trim();
                        }
                    }
                    usersID.put(chatId, new ChatInfo(userName, firstName, lastName));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void putUsersIdToHashMap() {
        if (!usersID.containsKey(chatId)) {
            usersID.put(chatId, new ChatInfo(chat.username() != null ? ("@" + chat.username()) : null,
                    chat.firstName(), chat.lastName()));
        }
    }

    private static void getBirthdays() {
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(NAME_AND_BIRTHDAYS))) {
            int whileCount = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] requisites = line.split(",");
                String[] dates = requisites[1].split("-");
                int year = Integer.parseInt(dates[0]);
                int month = Integer.parseInt(dates[1]);
                int day = Integer.parseInt(dates[2]);
                LocalDate lineLocalDate = LocalDate.of(year, month, day);
                events.add(new Person(requisites[0], lineLocalDate));
                if (LocalDate.now().isAfter(lineLocalDate)) {
                    events.set(whileCount, new Person(requisites[0], lineLocalDate.plusYears(1)));
                }
                whileCount++;
            }
            writeNameAndBirthdayToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeNameAndBirthdayToFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(NAME_AND_BIRTHDAYS))) {
            for (Person person : events) {
                bufferedWriter.write(person.getName() + "," + person.getDate());
                bufferedWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}