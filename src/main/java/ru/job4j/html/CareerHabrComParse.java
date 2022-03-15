package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The type CareerHabrComParse - парсим HTML страницу
 * ищем вложенные HTML-элементы (children) и собираем по ним необходимую  информацию
 */
public class CareerHabrComParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup
                .connect("https://career.habr.com/vacancies/java_developer")
                .get();
        Elements row = doc.select(".vacancy-card__inner");

        for (Element el : row) {
            System.out.println(el.children().get(1).attr("href"));
            System.out.println(el.children().get(2).text());
            System.out.println(el.children().get(0).text());

        }

    }
}
