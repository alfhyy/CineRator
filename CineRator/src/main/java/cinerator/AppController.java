package cinerator;

import cinerator.model.*;
import cinerator.service.*;

import java.util.List;

public class AppController {

    private UserService userService;
    private MovieService movieService;
    private RatingService ratingService;

    private User currentUser;

    public AppController(
            UserService userService,
            MovieService movieService,
            RatingService ratingService
    ) {
        this.userService = userService;
        this.movieService = movieService;
        this.ratingService = ratingService;
    }

    /* ===== AUTH ===== */

    public User login(String username) {
        currentUser = userService.login(username);
        return currentUser;
    }

    /* ===== MOVIES ===== */

    public List<Movie> searchMovies(String keyword) {
        return movieService.searchMovies(keyword);
    }

    /* ===== RATINGS ===== */

    public void rateMovie(String movieId, int score) {
        if (currentUser == null) {
            throw new IllegalStateException("User not logged in");
        }
        ratingService.rateMovie(movieId, currentUser.getId(), score);
    }

    public double getAverageRating(String movieId) {
        return ratingService.getAverageRating(movieId);
    }
}
