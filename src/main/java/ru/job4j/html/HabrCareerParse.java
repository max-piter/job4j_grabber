package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.CareerHabrDateTimeParser;


import java.util.ArrayList;
import java.util.List;

/**
 * The type HabrCareerParse - парсим HTML страницу
 * ищем вложенные HTML-элементы (children) и собираем по ним необходимую  информацию
 *
 * + парсим строку дату в формате Date
 * + парсим только 5 страниц
 */

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static String des = null;
    private final CareerHabrDateTimeParser dateTimeParser;

    public HabrCareerParse(CareerHabrDateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        return null;
    }

    public String retrieveDescription(String link) throws Exception {

        Document doc =  Jsoup
                .connect(link)
                .get();

        Elements divElement = doc.getElementsByAttributeValue("class", "style-ugc");
        return divElement.text();
    }

    public static void main(String[] args) throws Exception {
        CareerHabrDateTimeParser parser = new CareerHabrDateTimeParser();
        HabrCareerParse description = new HabrCareerParse(parser);

        for (int i = 1; i <= 5; i++) {
            String page = String.valueOf(i);
            Document doc = Jsoup
                    .connect("https://career.habr.com/vacancies/java_developer?page=" + page)
                    .get();
            Elements row = doc.select(".vacancy-card__inner");

            for (Element el : row) {
                System.out.println(el.children().get(2).text());
                des = description.retrieveDescription(SOURCE_LINK + el.children()
                        .get(1).attr("href"));
                System.out.println("Описание вакансии: " + des);
                System.out.println("Ссылка: " + SOURCE_LINK + el.children().get(1).attr("href"));
                System.out.println(parser
                        .parse(el.children().get(0).children().get(0).attr("datetime")));
                System.out.println();
            }
        }
    }
}
