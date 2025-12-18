package cinerator.storage;

import cinerator.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelStorage {

    public ExcelStorage() {
        initFile();
    }

    /* ================= INIT ================= */

    private void initFile() {
        if (StorageManager.EXCEL_FILE.exists()) return;

        try (Workbook wb = new XSSFWorkbook()) {
            wb.createSheet(StorageManager.MOVIE_SHEET);
            wb.createSheet(StorageManager.USER_SHEET);
            wb.createSheet(StorageManager.RATING_SHEET);

            try (FileOutputStream fos = new FileOutputStream(StorageManager.EXCEL_FILE)) {
                wb.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Workbook openWorkbook() throws IOException {
        return new XSSFWorkbook(new FileInputStream(StorageManager.EXCEL_FILE));
    }

    /* ================= USERS ================= */

    public User findUserByUsername(String username) {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.USER_SHEET);
            for (Row row : sheet) {
                if (row.getCell(1) != null &&
                        row.getCell(1).getStringCellValue().equals(username)) {
                    return new User(
                            row.getCell(0).getStringCellValue(),
                            row.getCell(1).getStringCellValue()
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUser(User user) {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.USER_SHEET);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getUsername());

            saveWorkbook(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ================= MOVIES ================= */

    public List<Movie> loadMovies() {
        List<Movie> movies = new ArrayList<>();

        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.MOVIE_SHEET);
            for (Row row : sheet) {
                if (row.getCell(0) == null) continue;

                movies.add(new Movie(
                        row.getCell(0).getStringCellValue(),
                        row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public void saveMovie(Movie movie) {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.MOVIE_SHEET);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            row.createCell(0).setCellValue(movie.getId());
            row.createCell(1).setCellValue(movie.getTitle());
            row.createCell(2).setCellValue(movie.getGenre());

            saveWorkbook(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ================= RATINGS ================= */

    public List<Rating> loadRatings() {
        List<Rating> ratings = new ArrayList<>();

        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.RATING_SHEET);
            for (Row row : sheet) {
                if (row.getCell(0) == null) continue;

                ratings.add(new Rating(
                        row.getCell(0).getStringCellValue(),
                        row.getCell(1).getStringCellValue(),
                        (int) row.getCell(2).getNumericCellValue()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    public void saveRating(Rating rating) {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.RATING_SHEET);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            row.createCell(0).setCellValue(rating.getMovieId());
            row.createCell(1).setCellValue(rating.getUserId());
            row.createCell(2).setCellValue(rating.getScore());

            saveWorkbook(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ================= UTIL ================= */

    private void saveWorkbook(Workbook wb) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(StorageManager.EXCEL_FILE)) {
            wb.write(fos);
        }
    }
}
