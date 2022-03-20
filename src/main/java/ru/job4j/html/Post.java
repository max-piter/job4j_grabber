package ru.job4j.html;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Objects;

public class Post {
    private int id;
    private String title;
    private String link;
    private String discription;
    private DateTime created;

    public Post() {
    }

    public Post(int id, String title, String link, String discription, DateTime created) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.discription = discription;
        this.created = created;
    }

    private String retrieveDescription(String link) throws Exception {

        Document doc =  Jsoup
                .connect(link)
                .get();

        Elements divElement = doc.getElementsByAttributeValue("class", "style-ugc");
        discription = divElement.text();
        System.out.println(discription);

        return discription;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDiscription() {
        return discription;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        Post post = (Post) o;
        return getId() == post.getId() && Objects.equals(getTitle(),
                post.getTitle()) && Objects.equals(getLink(),
                post.getLink()) && Objects.equals(getCreated(),
                post.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getLink(), getCreated());
    }

    @Override
    public String toString() {
        return "Post{"
              +  "id=" + id
              +  ", title='" + title + '\''
              +  ", link='" + link + '\''
              +  ", discription='" + discription + '\''
              +  ", created=" + created
              +  '}';
    }
}
