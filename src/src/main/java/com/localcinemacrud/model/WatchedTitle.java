package com.localcinemacrud.model;

import java.time.LocalDate;

public class WatchedTitle {
    private int id;
    private int titleId;
    private String titleName; // preenchido via join, não salvo diretamente
    private LocalDate watchedDate;
    private double rating;
    private String comment;

    public WatchedTitle() {
    }

    public WatchedTitle(int titleId, LocalDate watchedDate, double rating, String comment) {
        this.titleId = titleId;
        this.watchedDate = watchedDate;
        this.rating = rating;
        this.comment = comment;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTitleId() { return titleId; }
    public void setTitleId(int titleId) { this.titleId = titleId; }

    public String getTitleName() { return titleName; }
    public void setTitleName(String titleName) { this.titleName = titleName; }

    public LocalDate getWatchedDate() { return watchedDate; }
    public void setWatchedDate(LocalDate watchedDate) { this.watchedDate = watchedDate; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    @Override
    public String toString() {
        return String.format("[%d] %s - watched on %s | rating: %.1f | \"%s\"",
                id, titleName != null ? titleName : "titleId=" + titleId, watchedDate, rating, comment);
    }
}