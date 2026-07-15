package com.localcinemacrud.repository;

import com.localcinemacrud.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TitleRepository {

    private Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        return DriverManager.getConnection(url, user, password);
    }

    public int save(Title title) {
        String sql = "INSERT INTO titles (name, release_date, category, genre, director, duration, creator, new_seasons) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, title.getName());
            stmt.setInt(2, title.getReleaseDate());
            stmt.setString(3, title.getCategory());
            stmt.setString(4, title.getGenre());

            if (title instanceof Movie movie) {
                stmt.setString(5, movie.getDirector());
                stmt.setInt(6, movie.getDuration());
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.BOOLEAN);

            } else if (title instanceof Series series) {
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.INTEGER);
                stmt.setString(7, series.getCreator());
                stmt.setBoolean(8, series.isNewSeasons());

            } else if (title instanceof Documentary doc) {
                stmt.setNull(5, Types.VARCHAR);
                stmt.setInt(6, doc.getDuration());
                stmt.setString(7, doc.getCreator());
                stmt.setNull(8, Types.BOOLEAN);

            } else {
                throw new IllegalArgumentException("Unsupported title type: " + title.getClass());
            }

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    title.setId(generatedId);
                    System.out.println("Saved successfully with id " + generatedId);
                    return generatedId;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error saving title: " + e.getMessage());
        }

        return -1;
    }

    public List<Title> getAll() {
        List<Title> titles = new ArrayList<>();
        String sql = "SELECT * FROM titles";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                titles.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching titles: " + e.getMessage());
        }

        return titles;
    }

    public Title getById(int id) {
        String sql = "SELECT * FROM titles WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                } else {
                    System.out.println("There's no such id");
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching title: " + e.getMessage());
            return null;
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM titles WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            System.out.println(rows == 0 ? "There's no such id" : "Deleted successfully");

        } catch (SQLException e) {
            System.out.println("Error deleting title: " + e.getMessage());
        }
    }

    private Title mapRow(ResultSet rs) throws SQLException {
        String category = rs.getString("category");

        Title title;

        if ("SERIES".equalsIgnoreCase(category)) {
            Series series = new Series();
            series.setCreator(rs.getString("creator"));
            series.setNewSeasons(rs.getBoolean("new_seasons"));
            title = series;

        } else if ("DOCUMENTARY".equalsIgnoreCase(category)) {
            Documentary doc = new Documentary(rs.getString("creator"));
            doc.setDuration(rs.getInt("duration"));
            title = doc;

        } else {
            Movie movie = new Movie();
            movie.setDirector(rs.getString("director"));
            movie.setDuration(rs.getInt("duration"));
            title = movie;
        }

        title.setId(rs.getInt("id"));
        title.setName(rs.getString("name"));
        title.setReleaseDate(rs.getInt("release_date"));
        title.setCategory(category);
        title.setGenre(rs.getString("genre"));

        return title;
    }

    public List<Title> searchByName(String name) {
        List<Title> titles = new ArrayList<>();
        String sql = "SELECT * FROM titles WHERE name ILIKE ? ORDER BY name";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    titles.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching titles: " + e.getMessage());
        }

        return titles;
    }
}