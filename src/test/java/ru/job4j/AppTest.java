package ru.job4j;

import org.junit.Assert;
import org.junit.Test;

public class AppTest {

    @Test
    public void whenOneStringEqualsAnother() {
        String str = "Hello Grabber";
        String str1 = App.strings();
        Assert.assertEquals(str, str1);
    }

    @Test
    public void whenOneStringNotEqualsAnother() {
        String str = "";
        String str1 = App.strings();
        Assert.assertNotEquals(str, str1);
    }
}