package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class VacancyDescription {

    public String retrieveDescription(String link) throws Exception {

        Document doc =  Jsoup
                .connect(link)
                .get();

        Elements divElement = doc.getElementsByAttributeValue("class", "style-ugc");
        return divElement.text();
    }
}
