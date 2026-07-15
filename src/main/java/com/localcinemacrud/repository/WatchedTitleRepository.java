package com.localcinemacrud.repository;

import com.localcinemacrud.model.WatchedTitle;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WatchedTitleRepository {

    private Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        return DriverManager.getConnection(url, user, password);
    }

    public void save(WatchedTitle watched) {
        String sql = "INSERT INTO watched_titles (title_id, watched_date, rating, comment) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, watched.getTitleId());
            stmt.setDate(2, Date.valueOf(watched.getWatchedDate()));
            stmt.setDouble(3, watched.getRating());
            stmt.setString(4, watched.getComment());

            stmt.executeUpdate();
            System.out.println("Watched entry saved successfully");

        } catch (SQLException e) {
            System.out.println("Error saving watched entry: " + e.getMessage());
        }
    }

    public List<WatchedTitle> getAll() {
        List<WatchedTitle> list = new ArrayList<>();
        String sql = "SELECT w.id, w.title_id, w.watched_date, w.rating, w.comment, t.name " +
                "FROM watched_titles w JOIN titles t ON t.id = w.title_id " +
                "ORDER BY w.watched_date DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching watched titles: " + e.getMessage());
        }

        return list;
    }

    public void delete(int id) {
        String sql = "DELETE FROM watched_titles WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows == 0 ? "There's no such id" : "Deleted successfully");

        } catch (SQLException e) {
            System.out.println("Error deleting watched entry: " + e.getMessage());
        }
    }

    private WatchedTitle mapRow(ResultSet rs) throws SQLException {
        WatchedTitle w = new WatchedTitle();
        w.setId(rs.getInt("id"));
        w.setTitleId(rs.getInt("title_id"));
        w.setTitleName(rs.getString("name"));
        w.setWatchedDate(rs.getDate("watched_date").toLocalDate());
        w.setRating(rs.getDouble("rating"));
        w.setComment(rs.getString("comment"));
        return w;
    }
}