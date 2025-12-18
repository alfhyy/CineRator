package cinerator.service;

import cinerator.model.Rating;
import cinerator.storage.ExcelStorage;

import java.util.List;

public class RatingService {

    private final ExcelStorage storage;

    public RatingService(ExcelStorage storage) {
        this.storage = storage;
    }

    public void rateMovie(String movieId, String userId, int score) {
        if (score < 1 || score > 5)
            throw new IllegalArgumentException("Rating must be 1–5");

        storage.saveRating(new Rating(movieId, userId, score));
    }

    public double getAverageRating(String movieId) {
        List<Rating> ratings = storage.loadRatings();

        return ratings.stream()
                .filter(r -> r.getMovieId().equals(movieId))
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
    }
}
