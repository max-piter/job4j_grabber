package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
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
     * Для загрузки Properties удобней всего использовать ClassLoader.
     * Все ресурсные файлы храним в папке resources по пути src/main/resources.
     * ClassLoader будет искать файлы в этой папке и прописывать путь не нужно.
     *
     * @return the properties
     * @throws Exception the exception
     */
    public static Properties loadConfig(String fileName) throws Exception {
        Properties prop =  new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  prop;
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