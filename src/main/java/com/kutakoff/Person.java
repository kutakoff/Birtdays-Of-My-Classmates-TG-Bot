package com.kutakoff;

import java.time.LocalDate;

public record Person(String name, LocalDate date) {

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }
}
