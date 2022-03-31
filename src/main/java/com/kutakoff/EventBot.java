package com.kutakoff;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventBot {

    private static final Logger log = Logger.getLogger(EventBot.class.getName());

    public static String CHAT = "Id чата";
    public static String USER_NAME = "user_name";
    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";


    private static final String TOKEN = "5205856249:AAGN957nSTUq4gMCtx991IhL_k62ht2xRCU";

    private static final String file = "D:\\usersID.txt";

    public static HashMap<Long, ChatInfo> usersID = new HashMap<>();
    public static Chat chat;
    private static Long chatId;

    public static HashMap<Long, ChatInfo> getUsersID() {
        return usersID;
    }

    public static void main(String[] args) throws IOException {
        TelegramBot bot = new TelegramBot(TOKEN);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        List<Person> events = new ArrayList<>();
        events.add(new Person("Вова Аббаслы", new Date(122, Calendar.NOVEMBER, 28))); //Вова Аббаслы 0
        events.add(new Person("Алексеев Данил", new Date(122, Calendar.AUGUST, 6))); //Алексеев Данил 1
        events.add(new Person("Алиев Иса", new Date(122, Calendar.APRIL, 26))); //Алиев Иса 2
        events.add(new Person("Андросова Варвара", new Date(122, Calendar.JULY, 10))); //Андросова Варвара 3
        events.add(new Person("Воинов Роман", new Date(122, Calendar.OCTOBER, 3))); //Воинов Роман 4
        events.add(new Person("Геращенко Алёна", new Date(123, Calendar.JANUARY, 29))); //Геращенко Алёна 5
        events.add(new Person("Гущин Даниил", new Date(122, Calendar.JUNE, 16))); //Гущин Даниил 6
        events.add(new Person("Ермакова Софья", new Date(122, Calendar.MAY, 6))); //Ермакова Софья 7
        events.add(new Person("Икрамнжанов Абубакир", new Date(122, Calendar.JUNE, 9))); //Икрамнжанов Абубакир 8
        events.add(new Person("Казанцев Евгений", new Date(123, Calendar.MARCH, 17))); //Казанцев Евгений 9
        events.add(new Person("Карцев Владимир и Климкина Елизавета", new Date(123, Calendar.MARCH, 7))); //Карцев Владимир 10
        events.add(new Person("Качалина Вероника", new Date(122, Calendar.JULY, 7))); //Качалина Вероника 11
        events.add(new Person("Коханская Анна", new Date(122, Calendar.DECEMBER, 16))); //Коханская Анна 13
        events.add(new Person("Кутаков Максим и Кузовкина Ульяна", new Date(122, Calendar.JUNE, 17))); //Кузовкина Ульяна 14
        events.add(new Person("Лалаев Махарам", new Date(122, Calendar.MAY, 11))); //Лалаев Махарам 16
        events.add(new Person("Матмунсаев Бима", new Date(122, Calendar.AUGUST, 9))); //Матмунсаев Бима 17
        events.add(new Person("Мусаев Галандар", new Date(123, Calendar.JANUARY, 19))); //Мусаев Галандар 18
        events.add(new Person("Мухортых Даниил", new Date(122, Calendar.NOVEMBER, 6))); //Мухортых Даниил 19
        events.add(new Person("Розова Варвара", new Date(122, Calendar.DECEMBER, 15))); //Розова Варвара 20
        events.add(new Person("Салимова Аминат", new Date(123, Calendar.JANUARY, 1))); //Салимова Аминат 21
        events.add(new Person("Седов Александр", new Date(123, Calendar.JANUARY, 11))); //Седов Александр 22
        events.add(new Person("Семёнова Анастасия", new Date(123, Calendar.FEBRUARY, 19))); //Семёнова Анастасия 23
        events.add(new Person("Сотников Александр", new Date(122, Calendar.JUNE, 29))); //Сотников Александр 24
        events.add(new Person("Юсупов Михаил", new Date(122, Calendar.JULY, 5))); //Юсупов Михаил 25
        events.add(new Person("Вика Попова", new Date(122, Calendar.NOVEMBER, 8))); //Вика Попова 26
        Collections.sort(events, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        String line;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while ((line = bufferedReader.readLine()) != null) {
                if (!"".equals(line)) {
                    String[] requisites = line.split(",");
                    Long chatId = null;
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

        MaxOutgoing maxOutgoing = new MaxOutgoing();
        maxOutgoing.start();

        bot.setUpdatesListener(updates -> {
                    updates.forEach(update -> {
                        chat = update.message().chat();
                        chatId = update.message().chat().id();
                        String text = update.message().text();
                        if (update != null && update.message() != null && update.message().chat() != null) {
                            try {
                                if ("/start".equalsIgnoreCase(text)) {
                                    bot.execute(new SendMessage(chatId, "Введите:\n/first, чтобы вывести ближайшую дату дня рождения человека.\n/all, чтобы вывести все будующие дни рождения человека."));
                                } else if ("/first".equalsIgnoreCase(text)) {
                                    bot.execute(new SendMessage(chatId, "Ближайшая дата дня рождения: " + simpleDateFormat.format(events.get(0).getDate()) + " --> " + events.get(0).getName()));
                                } else if ("/all".equalsIgnoreCase(text)) {
                                    StringBuilder sb = new StringBuilder("Будущие дни рождения:");
                                    for (Person person : events) {
                                        sb.append("\n").append(simpleDateFormat.format(person.getDate())).append(" --> ").append(person.getName());
                                    }
                                    bot.execute(new SendMessage(chatId, sb.toString()));
                                }
                                //отправить Максу чат того, кто пользовался ботом
                                bot.execute(new SendMessage(1011661199, "Id чата = " + chatId +
                                        (chat.username() != null ? ", user_name = @" + chat.username() : "") +
                                        (chat.firstName() != null ? ", first_name = " + chat.firstName() : "") +
                                        (chat.lastName() != null ? ", last_name = " + chat.lastName() : "")
                                ));
                                if (!usersID.containsKey(chatId)) {
                                    usersID.put(chatId, new ChatInfo(chat.username() != null ? ("@" + chat.username()) : null,
                                            chat.firstName(), chat.lastName()));
                                }
                            } catch (Exception e) {
                                log.log(Level.SEVERE, "ERROR", e);
                                bot.execute(new SendMessage(chatId, "Ошибка в работе BOT-сервиса, обратитесь к разработчику"));
                            }

                        }
                    });
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                }
        );
    }

    @Override
    public String toString() {
        return "Id чата = " + chatId + ", user_name = " + "@" + chat.username() +", first_name = " + chat.firstName() + ", last_name = " + chat.lastName();
    }

    static class Person {
        private String name;
        private Date date;

        public Person(String name, Date date) {
            this.name = name;
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public Date getDate() {
            return date;
        }
    }

}