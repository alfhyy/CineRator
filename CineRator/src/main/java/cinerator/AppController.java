package cinerator;

import cinerator.model.Movie;
import cinerator.model.Rating;
import cinerator.model.User;
import java.util.UUID;
import cinerator.storage.ExcelStorage;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

public class AppController {
    private final ExcelStorage storage;

    /**
     *
     * @param storage
     */
    public AppController(ExcelStorage storage) {
        this.storage = storage;
    }

    // --- AUTH ---

    /**
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public User login(String username, String password) throws Exception {
        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Please fill in all fields.");
        }
        User user = storage.findUserByUsername(username);
        if (user == null) {
            throw new Exception("Username not found.");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception("Incorrect password.");
        }
        return user;
    }

    /**
     *
     * @param username
     * @param password
     * @throws Exception
     */
    public void registerUser(String username, String password) throws Exception {

        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Fields cannot be empty.");
        }

        // 1. Check if username is taken
        if (storage.findUserByUsername(username) != null) {
            throw new Exception("Username already exists.");
        }

        // 2. Create new user with random ID
        User newUser = new User(UUID.randomUUID().toString(), username, password);

        // 3. Save to Excel
        storage.saveUser(newUser);
    }

    // --- MOVIES ---

    /**
     *
     * @param title
     * @param genre
     * @param imageUrl
     * @param rating
     */
    public void addMovie(String title, String genre, String imageUrl, String rating) {
        storage.saveMovie(title, genre, imageUrl, rating);
        /**
         * Gets all movies from storage.
         *
         * @return list of Movie objects
         */

    }

    /**
     *
     * @return
     */
    public List<Movie> getAllMovies() {
        // Ensure your Storage returns cinerator.model.Movie, not MovieRecord
        return storage.loadAllMovies();
    }

    // --- RATINGS ---

    /**
     * Called by RatedView to show the list of cards.
     * Logic: Fetch all movies, then filter to keep only the ones this user has rated.
     */
    public List<Movie> getRatedMovies(User user) {
        // 1. Get the list of Movie IDs (or Titles) the user has rated
        List<String> ratedIds = storage.getUserRatedMovieIds(user.getId());
        // Note: If your storage uses titles, use getUserRatedMovieTitles(user.getUsername())

        // 2. Get all movies
        List<Movie> allMovies = storage.loadAllMovies();

        // 3. Filter: Keep only movies that are in the 'ratedIds' list
        List<Movie> filtered = new ArrayList<>();
        for (Movie m : allMovies) {
            // Check matching ID (or Title)
            if (ratedIds.contains(m.getId())) {
                filtered.add(m);
            }
        }
        return filtered;
    }

    /**
     *
     * @param query
     * @return
     */
    public List<Movie> searchMovies(String query) {
        if (query == null) query = ""; // avoid null input
        String lowerQuery = query.trim().toLowerCase();

        return getAllMovies().stream()
                .filter(m -> {
                    String title = m.getTitle() != null ? m.getTitle().toLowerCase() : "";
                    String genre = m.getGenre() != null ? m.getGenre().toLowerCase() : "";
                    return title.contains(lowerQuery) || genre.contains(lowerQuery);
                })
                .collect(Collectors.toList());
    }

    /**
     * Called by RatedView to populate the "Edit Rating" dialog
     */
    public Rating getUserRating(String userId, String movieId) {
        // You need to implement getRating in ExcelStorage to find a specific row
        return storage.getRating(userId, movieId);
    }

    /**
     *
     * @param userId
     * @param movieId
     * @param rating
     * @param comment
     */
    public void saveUserRating(String userId, String movieId, double rating, String comment) {
        storage.saveRating(userId, movieId, rating, comment);
    }

    /**
     *
     * @param userId
     * @param movieId
     */
    public void deleteUserRating(String userId, String movieId) {
        storage.deleteRating(userId, movieId);
    }
}