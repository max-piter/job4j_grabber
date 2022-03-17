package ru.job4j.grabber.utils;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class CareerHabrDateTimeParser implements DateTimeParser {

    @Override
    public DateTime parse(String parse) {
        org.joda.time.format.DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
        return parser2.parseDateTime(parse);
    }
}
