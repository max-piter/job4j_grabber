package ru.job4j.html;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private final Connection cnn;

    public PsqlStore(Properties cfg) {
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("grabber.properties")) {
            cfg.load(in);
            Class.forName(cfg.getProperty("driver_class"));
         cnn = DriverManager.getConnection(
                 cfg.getProperty("url"),
                 cfg.getProperty("username"),
                 cfg.getProperty("password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Post getPostFromResultSet(ResultSet rs) throws SQLException {
        return new Post(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("discription"),
                rs.getString("link"),
                rs.getTimestamp("created").toLocalDateTime());
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement ps =
                     cnn.prepareStatement("insert into post(title, discription, link, created)"
                                    + " values(?, ?, ?, ?)")) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDiscription());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> post = new ArrayList<>();
        try (PreparedStatement ps = cnn.prepareStatement("select * from post")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    post.add(getPostFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement ps = cnn.prepareStatement("select * from post where id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post = getPostFromResultSet(rs);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.now();
        Post post = new Post("java - developer", "Some special thing",
                "http:/ooo.huyovoe.ru", time);
        Post post1 = new Post("Scala-developer", "something", "http:/ooo.huyovoe.com", time);
        Post post2 =  new Post("java-developer", "new project", "http:/ooo.huyovoe", time);
        Properties cfg  = new Properties();
        try (PsqlStore psqlStore = new PsqlStore(cfg)) {
            psqlStore.save(post);
            psqlStore.save(post1);
            psqlStore.save(post2);
            System.out.println(psqlStore.findById(1));
            System.out.println(psqlStore.getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
