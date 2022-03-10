package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.*;
import java.sql.*;
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
     * Загружаем данные файла properties
     * Для загрузки Properties удобней всего использовать ClassLoader.
     * Все ресурсные файлы храним в папке resources по пути src/main/resources.
     * ClassLoader будет искать файлы в этой папке и прописывать путь не нужно.
     *
     * @return the properties
     *
     */
    public static Properties loadConfig(String fileName) {
        Properties prop = new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * Execute table.
     * метод для чтения sql файла
     * @param fileName   the file name
     * @param connection the connection
     * @throws Exception the exception
     */
    public static void executeTable(String fileName, Connection connection) throws Exception {
            try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
                String line;
                StringBuilder sb = new StringBuilder();
                String  ls = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    sb.append(ls);
                }
                String sql = sb.toString();
                try (Statement ps = connection.createStatement()) {
                    ps.execute(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    /**
     * Gets connection.
     * метод  подключения к базе данных
     * @param fileName the file name
     * @return the connection
     * @throws Exception the exception
     */
    public static Connection getConnection(String fileName) throws Exception {
        Class.forName(loadConfig(fileName)
                .getProperty("driver_class"));
        return DriverManager.getConnection(
                loadConfig(fileName).getProperty("url"),
                loadConfig(fileName).getProperty("username"),
                loadConfig(fileName).getProperty("password"));
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
        try (Connection connection =  getConnection("rabbit.properties")) {
            executeTable("db/rabbit.sql", connection);

            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(dataMap)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    /**
     * The type Rabbit.
     * Класс  Rabbit,
     */
    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context)  {
            System.out.println("Rabbit runs here ... and there...");
            Connection connection = (Connection) context
                    .getJobDetail()
                    .getJobDataMap()
                    .get("connection");

            try (PreparedStatement ps = connection.prepareStatement(
                    "insert into rabbit(created_date) values(?)"
            )) {
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                ps.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }
}