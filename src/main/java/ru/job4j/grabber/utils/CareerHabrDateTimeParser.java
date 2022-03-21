package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CareerHabrDateTimeParser implements LocalDateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        return  LocalDateTime.parse(parse, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
