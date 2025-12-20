package cinerator.model;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private String imageUrl; // Added
    private String rating;   // Added (Global average rating)

    public Movie(String id, String title, String genre, String imageUrl, String rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getImageUrl() { return imageUrl; }
    public String getRating() { return rating; }

    @Override
    public String toString() {
        return title + " (" + genre + ")";
    }
}