package ru.job4j.html;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private final Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbs.driver"));
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
                rs.getString("n_name"),
                rs.getString("t_text"),
                rs.getString("link"),
                rs.getTimestamp("created").toLocalDateTime());
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement ps =
                     cnn.prepareStatement("insert into grabber(title, discription, link, created)"
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
        try (PreparedStatement ps = cnn.prepareStatement("select * from grabber")) {
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
        try (PreparedStatement ps = cnn.prepareStatement("select * from grabber where id = ?")) {
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
}
