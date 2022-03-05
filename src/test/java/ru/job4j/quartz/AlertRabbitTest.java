package ru.job4j.quartz;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;

public class AlertRabbitTest {


    @Test
    public void whenLoadConfigSuccess() throws Exception {
        File source = new File("/Users/a123/projects/job4j_grabber/src/test/test.properties");
        try (PrintWriter out = new PrintWriter(source)) {
            out.println("line=1");
            out.println("line2=2");
            out.println("line3=3");
        }

        int interval = Integer.parseInt(AlertRabbit.loadConfig("test.properties").getProperty("line")
                + AlertRabbit.loadConfig("test.properties").getProperty("line3"));
        Assert.assertEquals(interval, 13);


    }

}