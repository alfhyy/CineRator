package cinerator.model;

public class Rating {
    private String movieId;
    private String userId;
    private int score; // 1–5

    public Rating(String movieId, String userId, int score) {
        this.movieId = movieId;
        this.userId = userId;
        this.score = score;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }
}
