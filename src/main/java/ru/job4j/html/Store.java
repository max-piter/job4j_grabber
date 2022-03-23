package ru.job4j.html;

import java.util.List;

/**
 * The interface Store. - интерфейс для связи с Базой Данных
 */
public interface Store {
    void save(Post post);

    List<Post> getAll();

    Post findById(int id);
}
