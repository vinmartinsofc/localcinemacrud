package com.localcinemacrud.model;

public class TmdbResult {
    private int id;
    private String title;
    private String name;
    private String releaseDate;
    private String overview;
    private double voteAverage;
    private String posterPath;
    private String mediaType;
    private String creator;

    public TmdbResult() {}

    public TmdbResult(int id, String title, String releaseDate, String overview,
                      double voteAverage, String posterPath, String mediaType) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.mediaType = mediaType;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public double getVoteAverage() { return voteAverage; }
    public void setVoteAverage(double voteAverage) { this.voteAverage = voteAverage; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public String getDisplayName() {
        return title != null ? title : name;
    }

    @Override
    public String toString() {
        return String.format("[TMDB] %s (%s) - Rating: %.1f/10 | %s",
                getDisplayName(),
                releaseDate != null ? releaseDate.split("-")[0] : "N/A",
                voteAverage,
                mediaType != null ? mediaType.toUpperCase() : "UNKNOWN");
    }
}