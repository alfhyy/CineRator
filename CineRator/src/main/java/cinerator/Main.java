package cinerator;

import cinerator.model.Movie;
import cinerator.service.*;
import cinerator.storage.ExcelStorage;

public class Main {

    public static void main(String[] args) {

        ExcelStorage storage = new ExcelStorage();

        MovieService movieService = new MovieService(storage);
        UserService userService = new UserService(storage);
        RatingService ratingService = new RatingService(storage);

        AppController controller =
                new AppController(userService, movieService, ratingService);

        controller.login("alice");

        // seed one movie if empty
        if (storage.loadMovies().isEmpty()) {
            storage.saveMovie(new Movie("1", "Interstellar", "Sci-Fi"));
        }

        controller.searchMovies("inter")
                .forEach(m -> {
                    controller.rateMovie(m.getId(), 5);
                    System.out.println(
                            m.getTitle() + " avg=" +
                                    controller.getAverageRating(m.getId())
                    );
                });
    }
}
