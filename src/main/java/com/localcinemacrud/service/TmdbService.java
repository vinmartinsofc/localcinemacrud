package com.localcinemacrud.service;

import com.localcinemacrud.model.TmdbResult;
import com.localcinemacrud.model.Title;
import com.localcinemacrud.model.Movie;
import com.localcinemacrud.model.Series;
import com.localcinemacrud.model.Documentary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class TmdbService {
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w200";
    private final String apiKey;

    public TmdbService() {
        this.apiKey = System.getenv("TMDB_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            System.out.println("Warning: TMDB_KEY environment variable not set!");
        }
    }

    public List<TmdbResult> searchMulti(String query) {
        return search(query, "multi");
    }

    public List<TmdbResult> searchMovies(String query) {
        return search(query, "movie");
    }

    public List<TmdbResult> searchTVShows(String query) {
        return search(query, "tv");
    }

    public List<TmdbResult> searchDocumentaries(String query) {
        List<TmdbResult> results = search(query, "tv");
        List<TmdbResult> documentaries = new ArrayList<>();

        for (TmdbResult result : results) {
            if (isDocumentary(result.getId())) {
                result.setMediaType("documentary");
                documentaries.add(result);
            }
        }
        return documentaries;
    }

    private List<TmdbResult> search(String query, String type) {
        List<TmdbResult> results = new ArrayList<>();

        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("TMDB_KEY not configured. Please set TMDB_KEY environment variable.");
            return results;
        }

        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String endpoint;

            if ("multi".equals(type)) {
                endpoint = "/search/multi";
            } else if ("movie".equals(type)) {
                endpoint = "/search/movie";
            } else if ("tv".equals(type)) {
                endpoint = "/search/tv";
            } else {
                return results;
            }

            String urlString = BASE_URL + endpoint + "?api_key=" + apiKey + "&query=" + encodedQuery;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(content.toString());
                JSONArray resultsArray = json.getJSONArray("results");

                for (int i = 0; i < resultsArray.length() && i < 20; i++) {
                    JSONObject item = resultsArray.getJSONObject(i);
                    TmdbResult result = new TmdbResult();

                    result.setId(item.getInt("id"));

                    if ("movie".equals(type) || ("multi".equals(type) && item.has("title"))) {
                        result.setTitle(item.getString("title"));
                        result.setMediaType("movie");
                        if (item.has("release_date") && !item.isNull("release_date")) {
                            result.setReleaseDate(item.getString("release_date"));
                        }
                        enrichMovieDetails(result);

                    } else if ("tv".equals(type) || ("multi".equals(type) && item.has("name"))) {
                        result.setName(item.getString("name"));
                        result.setMediaType("tv");
                        if (item.has("first_air_date") && !item.isNull("first_air_date")) {
                            result.setReleaseDate(item.getString("first_air_date"));
                        }

                        if (item.has("created_by") && !item.isNull("created_by")) {
                            JSONArray creators = item.getJSONArray("created_by");
                            if (creators.length() > 0) {
                                result.setCreator(creators.getJSONObject(0).getString("name"));
                            }
                        }
                        enrichTVDetails(result);
                    }

                    if (item.has("overview") && !item.isNull("overview")) {
                        result.setOverview(item.getString("overview"));
                    }

                    if (item.has("vote_average") && !item.isNull("vote_average")) {
                        result.setVoteAverage(item.getDouble("vote_average"));
                    }

                    if (item.has("poster_path") && !item.isNull("poster_path")) {
                        result.setPosterPath(IMAGE_BASE_URL + item.getString("poster_path"));
                    }

                    if ("multi".equals(type) && item.has("media_type")) {
                        String mediaType = item.getString("media_type");
                        result.setMediaType(mediaType);
                    }

                    results.add(result);
                }
            } else {
                System.out.println("TMDB API error: " + responseCode);
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream())
                );
                String errorLine;
                StringBuilder errorContent = new StringBuilder();
                while ((errorLine = errorReader.readLine()) != null) {
                    errorContent.append(errorLine);
                }
                errorReader.close();
                System.out.println("Error details: " + errorContent.toString());
            }

            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Error searching TMDB: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    private void enrichMovieDetails(TmdbResult result) {
        try {
            String urlString = BASE_URL + "/movie/" + result.getId() + "?api_key=" + apiKey;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(content.toString());

                if (json.has("runtime") && json.getInt("runtime") > 0) {
                    result.setDuration(json.getInt("runtime"));
                }

                if (json.has("genres")) {
                    JSONArray genres = json.getJSONArray("genres");
                    StringBuilder genresStr = new StringBuilder();
                    for (int i = 0; i < genres.length(); i++) {
                        if (i > 0) genresStr.append(", ");
                        genresStr.append(genres.getJSONObject(i).getString("name"));
                    }
                    if (genresStr.length() > 0) {
                        result.setGenres(genresStr.toString());
                    }
                }

                if (json.has("origin_country")) {
                    JSONArray countries = json.getJSONArray("origin_country");
                    if (countries.length() > 0) {
                        result.setCountry(countries.getString(0));
                    }
                }

                conn.disconnect();
                
                enrichCredits(result);
            }
        } catch (Exception e) {
            System.out.println("Couldn't fetch full movie details for ID " + result.getId());
        }
    }

    private void enrichTVDetails(TmdbResult result) {
        try {
            String urlString = BASE_URL + "/tv/" + result.getId() + "?api_key=" + apiKey;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(content.toString());

                if (json.has("episode_run_time")) {
                    JSONArray runtimes = json.getJSONArray("episode_run_time");
                    if (runtimes.length() > 0 && runtimes.getInt(0) > 0) {
                        result.setDuration(runtimes.getInt(0));
                    }
                }

                if (json.has("genres")) {
                    JSONArray genres = json.getJSONArray("genres");
                    StringBuilder genresStr = new StringBuilder();
                    for (int i = 0; i < genres.length(); i++) {
                        if (i > 0) genresStr.append(", ");
                        genresStr.append(genres.getJSONObject(i).getString("name"));
                    }
                    if (genresStr.length() > 0) {
                        result.setGenres(genresStr.toString());
                    }
                }

                if (json.has("origin_country")) {
                    JSONArray countries = json.getJSONArray("origin_country");
                    if (countries.length() > 0) {
                        result.setCountry(countries.getString(0));
                    }
                }

                if (result.getCreator() == null || result.getCreator().isEmpty()) {
                    if (json.has("created_by")) {
                        JSONArray creators = json.getJSONArray("created_by");
                        if (creators.length() > 0) {
                            result.setCreator(creators.getJSONObject(0).getString("name"));
                        }
                    }
                }

                conn.disconnect();
            }
        } catch (Exception e) {
            System.out.println("Couldn't fetch full TV details for ID " + result.getId());
        }
    }

    private void enrichCredits(TmdbResult result) {
        try {
            String endpoint = "movie".equalsIgnoreCase(result.getMediaType()) ? "/movie/" : "/tv/";
            String urlString = BASE_URL + endpoint + result.getId() + "/credits?api_key=" + apiKey;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(content.toString());

                if ("movie".equalsIgnoreCase(result.getMediaType()) && json.has("crew")) {
                    JSONArray crew = json.getJSONArray("crew");
                    StringBuilder directorsStr = new StringBuilder();
                    for (int i = 0; i < crew.length(); i++) {
                        JSONObject person = crew.getJSONObject(i);
                        if ("Director".equals(person.getString("job"))) {
                            if (directorsStr.length() > 0) directorsStr.append(", ");
                            directorsStr.append(person.getString("name"));
                        }
                    }
                    if (directorsStr.length() > 0) {
                        result.setDirector(directorsStr.toString());
                    }
                }

                conn.disconnect();
            }
        } catch (Exception e) {
            System.out.println("Couldn't fetch credits for ID " + result.getId());
        }
    }

    private boolean isDocumentary(int tvId) {
        try {
            String urlString = BASE_URL + "/tv/" + tvId + "?api_key=" + apiKey;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(content.toString());
                if (json.has("genres")) {
                    JSONArray genres = json.getJSONArray("genres");
                    for (int i = 0; i < genres.length(); i++) {
                        JSONObject genre = genres.getJSONObject(i);
                        if (genre.getInt("id") == 99) {
                            conn.disconnect();
                            return true;
                        }
                    }
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            // Silently fail
        }
        return false;
    }

    public Title convertToTitle(TmdbResult tmdbResult) {
        String mediaType = tmdbResult.getMediaType();
        String name = tmdbResult.getDisplayName();
        String year = tmdbResult.getReleaseDate() != null ?
                tmdbResult.getReleaseDate().split("-")[0] : "0";
        int releaseYear = Integer.parseInt(year);
        
        String genre = tmdbResult.getGenres() != null && !tmdbResult.getGenres().isEmpty()
                ? tmdbResult.getGenres().split(",")[0].trim() 
                : "Unknown";

        Title title = null;

        if ("movie".equalsIgnoreCase(mediaType)) {
            String director = tmdbResult.getDirector() != null ? tmdbResult.getDirector() : "Unknown Director";
            int duration = tmdbResult.getDuration() > 0 ? tmdbResult.getDuration() : 120;
            
            title = new Movie(
                    name,
                    releaseYear,
                    "MOVIE",
                    genre,
                    director,
                    duration
            );
        } else if ("tv".equalsIgnoreCase(mediaType)) {
            String creator = tmdbResult.getCreator() != null ? tmdbResult.getCreator() : "Unknown Creator";
            
            title = new Series(
                    name,
                    releaseYear,
                    "SERIES",
                    genre,
                    creator,
                    true
            );
        } else if ("documentary".equalsIgnoreCase(mediaType)) {
            String creator = tmdbResult.getCreator() != null ? tmdbResult.getCreator() : "Unknown Creator";
            int duration = tmdbResult.getDuration() > 0 ? tmdbResult.getDuration() : 90;
            
            title = new Documentary(
                    name,
                    releaseYear,
                    "DOCUMENTARY",
                    genre,
                    creator,
                    duration
            );
        }

        return title;
    }

    /**
     * Exibe os resultados da busca de forma formatada e legível
     */
    public void displaySearchResults(List<TmdbResult> results) {
        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("TMDB SEARCH RESULTS");
        System.out.println("=".repeat(80));
        
        for (int i = 0; i < results.size(); i++) {
            TmdbResult result = results.get(i);
            System.out.printf("%n%d. %s%n", i + 1, result);
            
            if (result.getOverview() != null && !result.getOverview().isEmpty()) {
                String overview = result.getOverview();
                if (overview.length() > 150) {
                    overview = overview.substring(0, 150) + "...";
                }
                System.out.println("  Overview: " + overview);
            }
            
            if (result.getPosterPath() != null) {
                System.out.println("  Poster: " + result.getPosterPath());
            }
        }
        System.out.println("\n" + "=".repeat(80) + "\n");
    }
}
