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

                    // Primeiro, determinar o mediaType REAL (para "multi" type)
                    String detectedMediaType = null;
                    if ("multi".equals(type) && item.has("media_type")) {
                        detectedMediaType = item.getString("media_type");
                    } else if ("movie".equals(type)) {
                        detectedMediaType = "movie";
                    } else if ("tv".equals(type)) {
                        detectedMediaType = "tv";
                    }

                    // Agora processar baseado no mediaType determinado
                    if ("movie".equalsIgnoreCase(detectedMediaType)) {
                        if (item.has("title")) {
                            result.setTitle(item.getString("title"));
                        }
                        result.setMediaType("movie");
                        if (item.has("release_date") && !item.isNull("release_date")) {
                            result.setReleaseDate(item.getString("release_date"));
                        }
                        // Busca detalhes completos do filme
                        enrichMovieDetails(result);

                    } else if ("tv".equalsIgnoreCase(detectedMediaType)) {
                        if (item.has("name")) {
                            result.setName(item.getString("name"));
                        }
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
                        // Busca detalhes completos da série
                        enrichTVDetails(result);
                        
                    } else {
                        // Fallback se não conseguir determinar (trata como movie)
                        if (item.has("title")) {
                            result.setTitle(item.getString("title"));
                        } else if (item.has("name")) {
                            result.setName(item.getString("name"));
                        }
                        result.setMediaType("movie");
                        if (item.has("release_date") && !item.isNull("release_date")) {
                            result.setReleaseDate(item.getString("release_date"));
                        }
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

    /**
     * Busca detalhes completos de um filme (duração, gênero, diretor)
     */
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

                // Duração
                if (json.has("runtime") && json.getInt("runtime") > 0) {
                    result.setDuration(json.getInt("runtime"));
                }

                // Gêneros
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

                // País
                if (json.has("origin_country")) {
                    JSONArray countries = json.getJSONArray("origin_country");
                    if (countries.length() > 0) {
                        result.setCountry(countries.getString(0));
                    }
                }

                conn.disconnect();
                
                // Buscar diretor via créditos
                enrichCredits(result);
            }
        } catch (Exception e) {
            // Silenciosamente falha se não conseguir pegar os detalhes
            System.out.println("Couldn't fetch full movie details for ID " + result.getId());
        }
    }

    /**
     * Busca detalhes completos de uma série (duração do episódio, gênero, criador)
     */
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

                // Duração média do episódio
                if (json.has("episode_run_time")) {
                    JSONArray runtimes = json.getJSONArray("episode_run_time");
                    if (runtimes.length() > 0 && runtimes.getInt(0) > 0) {
                        result.setDuration(runtimes.getInt(0));
                    }
                }

                // Gêneros
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

                // País
                if (json.has("origin_country")) {
                    JSONArray countries = json.getJSONArray("origin_country");
                    if (countries.length() > 0) {
                        result.setCountry(countries.getString(0));
                    }
                }

                // Se creator não foi preenchido, tenta obter
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

    /**
     * Busca detalhes de créditos (diretor para filmes)
     */
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
                        if (genre.getInt("id") == 99) { // 99 é o ID para Documentary
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

    /**
     * Converte um resultado TMDB em um objeto Title para salvar no banco
     */
    public Title convertToTitle(TmdbResult tmdbResult) {
        String mediaType = tmdbResult.getMediaType();
        String name = tmdbResult.getDisplayName();
        String year = tmdbResult.getReleaseDate() != null ?
                tmdbResult.getReleaseDate().split("-")[0] : "0";
        
        // DEBUG
        System.out.println("\n=== DEBUG convertToTitle ===");
        System.out.println("Name: " + name);
        System.out.println("MediaType: '" + mediaType + "'");
        System.out.println("Genres: " + tmdbResult.getGenres());
        System.out.println("Creator: " + tmdbResult.getCreator());
        System.out.println("Director: " + tmdbResult.getDirector());
        System.out.println("Duration: " + tmdbResult.getDuration());
        System.out.println("========================\n");
        
        int releaseYear;
        try {
            releaseYear = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            releaseYear = 0;
        }
        
        // Usa o gênero da API ou "Unknown" como fallback
        String genre = tmdbResult.getGenres() != null && !tmdbResult.getGenres().isEmpty() 
                ? tmdbResult.getGenres().split(",")[0].trim() 
                : "Unknown";

        Title title = null;

        // Determinar tipo baseado em mediaType
        if ("movie".equalsIgnoreCase(mediaType)) {
            System.out.println(">>> Creating MOVIE");
            String director = tmdbResult.getDirector() != null && !tmdbResult.getDirector().isEmpty() 
                    ? tmdbResult.getDirector() 
                    : "Unknown Director";
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
            System.out.println(">>> Creating SERIES");
            String creator = tmdbResult.getCreator() != null && !tmdbResult.getCreator().isEmpty() 
                    ? tmdbResult.getCreator() 
                    : "Unknown Creator";
            
            title = new Series(
                    name,
                    releaseYear,
                    "SERIES",
                    genre,
                    creator,
                    true
            );
        } else if ("documentary".equalsIgnoreCase(mediaType)) {
            System.out.println(">>> Creating DOCUMENTARY");
            String creator = tmdbResult.getCreator() != null && !tmdbResult.getCreator().isEmpty() 
                    ? tmdbResult.getCreator() 
                    : "Unknown Creator";
            int duration = tmdbResult.getDuration() > 0 ? tmdbResult.getDuration() : 90;
            
            title = new Documentary(
                    name,
                    releaseYear,
                    "DOCUMENTARY",
                    genre,
                    creator,
                    duration
            );
        } else {
            // Fallback: se não conseguir determinar, assume Movie
            System.out.println(">>> Unknown mediaType '" + mediaType + "' - Creating MOVIE as fallback");
            String director = tmdbResult.getDirector() != null && !tmdbResult.getDirector().isEmpty() 
                    ? tmdbResult.getDirector() 
                    : "Unknown Director";
            int duration = tmdbResult.getDuration() > 0 ? tmdbResult.getDuration() : 120;
            
            title = new Movie(
                    name,
                    releaseYear,
                    "MOVIE",
                    genre,
                    director,
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
