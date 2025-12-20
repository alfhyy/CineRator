package cinerator.model;

public class Rating {
    private String userId;
    private String movieId; // Changed from title to ID for better linking
    private double rating;   // Renamed from 'value' to 'rating' to match getter
    private String comment; // Added for the review text

    public Rating(String userId, String movieId, double rating, String comment) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUserId() { return userId; }
    public String getMovieId() { return movieId; }
    public double getRating() { return rating; }
    public String getComment() { return comment; }
}