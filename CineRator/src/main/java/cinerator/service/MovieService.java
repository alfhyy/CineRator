package cinerator.service;

import cinerator.storage.ExcelStorage;

public class MovieService {

    private final ExcelStorage storage;

    public MovieService(ExcelStorage storage) {
        this.storage = storage;
    }
}
