package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Objects;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

/**
 * The type Alert rabbit.
 */
public class AlertRabbit {

    /**
     * Load config properties.
     * Загружаем данные файла rabbit.properties
     *
     * @return the properties
     * @throws Exception the exception
     */
    public static Properties loadConfig(String fileName) throws Exception {
        String rootPath = Objects.requireNonNull(Thread.currentThread()
                .getContextClassLoader().getResource("")).getPath();
        String filePath = rootPath + fileName;
        Properties properties = new Properties();
        properties.load(new BufferedInputStream(new FileInputStream(filePath)));
        return properties;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        int interval = Integer.parseInt(loadConfig("rabbit.properties")
                .getProperty("rabbit.interval"));
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    /**
     * The type Rabbit.
     */
    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ... and there...");
        }
    }
}