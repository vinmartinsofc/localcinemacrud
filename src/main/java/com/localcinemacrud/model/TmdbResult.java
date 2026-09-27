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
    
    // Novos campos para dados mais completos
    private int duration; // em minutos
    private String director; // para filmes
    private String genres; // gêneros separados por vírgula
    private String country; // país de origem

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

    // Getters e Setters
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

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getGenres() { return genres; }
    public void setGenres(String genres) { this.genres = genres; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDisplayName() {
        return title != null ? title : name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[TMDB] %s (%s)%n", 
                getDisplayName(),
                releaseDate != null ? releaseDate.split("-")[0] : "N/A"));
        
        if (genres != null && !genres.isEmpty()) {
            sb.append(String.format("  Genre: %s%n", genres));
        }
        
        if (duration > 0) {
            if ("movie".equalsIgnoreCase(mediaType)) {
                sb.append(String.format("  Duration: %d minutes%n", duration));
            } else if ("tv".equalsIgnoreCase(mediaType)) {
                sb.append(String.format("  Avg episode: ~%d minutes%n", duration));
            }
        }
        
        if (director != null && !director.isEmpty()) {
            sb.append(String.format("  Director: %s%n", director));
        }
        
        if (creator != null && !creator.isEmpty()) {
            sb.append(String.format("  Creator: %s%n", creator));
        }
        
        sb.append(String.format("  Rating: %.1f/10%n", voteAverage));
        sb.append(String.format("  Type: %s", mediaType != null ? mediaType.toUpperCase() : "UNKNOWN"));
        
        return sb.toString();
    }
}
