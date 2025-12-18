package cinerator.service;

import cinerator.model.Movie;
import cinerator.storage.ExcelStorage;

import java.util.List;
import java.util.stream.Collectors;

public class MovieService {

    private final ExcelStorage storage;

    public MovieService(ExcelStorage storage) {
        this.storage = storage;
    }

    public List<Movie> searchMovies(String keyword) {
        return storage.loadMovies().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
