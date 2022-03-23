package ru.job4j.html;

import java.util.List;

/**
 * The interface Parse. - интерфейс для извлечения данных с сайта
 */
public interface Parse {
        List<Post> list(String link);
    }
