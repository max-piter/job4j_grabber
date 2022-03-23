package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.CareerHabrDateTimeParser;
import ru.job4j.grabber.utils.LocalDateTimeParser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type HabrCareerParse - парсим HTML страницу
 * ищем вложенные HTML-элементы (children) и собираем по ним необходимую  информацию
 *
 * + парсим строку дату в формате LocalDateTime
 * + парсим описание по ссылке
 * собираем всю инфу по каждой вакансии в одну модель данных Post,
 *  а вакансии собираем в список List<Post> list
 *
 */

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private  static final int PAGES_TO_PARSE = 5;
    private final LocalDateTimeParser dateTimeParser;

    public HabrCareerParse(CareerHabrDateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    /**
     * Retrieve description string. - получаем описание вакансии по ссылке
     *
     * @param link the link
     * @return the string
     *
     */
    public String retrieveDescription(String link) {
        String des = null;
        try {
            Document doc = Jsoup
                    .connect(link)
                    .get();

            Elements divElement = doc.getElementsByAttributeValue("class", "style-ugc");
            des = divElement.text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return des;
    }

    /**
     * Vacancy post формирование всех элементов парсинга
     * одной вакансии в один  пост Post
     *
     * @param el the el (отдельная вакансия, )
     * @return the post (возвращаем Post  с заполнеными полями, со семи элементами)
     *
     */
    public Post vacancy(Element el) {
        Post post =  new Post();
        try {
            String title = el.children().get(2).text();
            String discription = retrieveDescription(SOURCE_LINK + el.children()
                    .get(1).attr("href"));
            LocalDateTime dateTime = dateTimeParser
                    .parse(el.children().get(0).children().get(0).attr("datetime"));
            post.setCreated(dateTime);
            post.setDiscription(discription);
            post.setLink(SOURCE_LINK + el.children().get(1).attr("href"));
            post.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * List list - метод собирает и  возвращает список вакансий, типа Post
     * с результатами парсинга
     * @param link - основная ссылка на страницу с вакансиями
     * @return List<Post> - список вакансий
     */
    @Override
    public List<Post> list(String link) {
        List<Post> list = new ArrayList<>();

        for (int i = 1; i <= PAGES_TO_PARSE; i++) {
            String page = String.valueOf(i);
            try {
                Document doc = Jsoup
                        .connect(link + page)
                        .get();
                Elements rows = doc.select(".vacancy-card__inner");
                for (Element el : rows) {
                    list.add(vacancy(el));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * The entry point of application -проверка метода list, всё работает
     *
     * @param args the input arguments
     *
     */
    public static void main(String[] args) {
        List<Post> post;
        CareerHabrDateTimeParser parser = new CareerHabrDateTimeParser();
        HabrCareerParse vacancy = new HabrCareerParse(parser);
        post =  vacancy.list("https://career.habr.com/vacancies/java_developer?page=");
        System.out.println(post);
    }
}
