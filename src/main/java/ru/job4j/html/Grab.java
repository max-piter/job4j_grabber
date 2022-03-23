package ru.job4j.html;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * The interface Grab.
 * В этом проекты мы будем использовать Quartz для запуска парсера.
 * Но напрямую мы не будем его использовать.
 * Абстрагирумся через  данный интерфейс
 *
 */
public interface Grab {
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;
}
