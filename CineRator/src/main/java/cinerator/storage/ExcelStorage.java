package cinerator.storage;

import cinerator.model.Movie;
import cinerator.model.Rating;
import cinerator.model.User;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelStorage {

    private Workbook openWorkbook() throws IOException {
        return StorageManager.openWorkbook();
    }

    // --- USER METHODS ---

    // [FIXED] Added this method back
    public void saveUser(User user) {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.USER_SHEET);
            if (sheet == null) return;

            int lastRow = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(lastRow);

            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getUsername());
            row.createCell(2).setCellValue(user.getPassword());

            StorageManager.saveWorkbook(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User findUserByUsername(String username) {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.USER_SHEET);
            if (sheet == null) return null;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Cell userCell = row.getCell(1);
                if (userCell != null && userCell.getStringCellValue().equals(username)) {
                    return new User(
                            getCellValue(row, 0),
                            getCellValue(row, 1),
                            getCellValue(row, 2)
                    );
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    // --- MOVIE METHODS ---

    public void saveMovie(String title, String genre, String imageUrl, String initialRating) {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.MOVIE_SHEET);
            int lastRow = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(lastRow);

            String id = String.valueOf(System.currentTimeMillis());

            row.createCell(0).setCellValue(id);
            row.createCell(1).setCellValue(title);
            row.createCell(2).setCellValue(genre);
            row.createCell(3).setCellValue(imageUrl);
            row.createCell(4).setCellValue(initialRating);

            StorageManager.saveWorkbook(wb);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public List<Movie> loadAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.MOVIE_SHEET);
            if (sheet == null) return movies;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                movies.add(new Movie(
                        getCellValue(row, 0), // id
                        getCellValue(row, 1), // title
                        getCellValue(row, 2), // genre
                        getCellValue(row, 3), // imageUrl
                        getCellValue(row, 4)  // global rating
                ));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return movies;
    }

    // --- RATING METHODS ---

    public void saveRating(String userId, String movieId, double ratingVal, String comment) {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.RATING_SHEET);
            boolean updated = false;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String rUser = getCellValue(row, 0);
                String rMovieId = getCellValue(row, 1);

                if (rUser.equals(userId) && rMovieId.equals(movieId)) {
                    // Update existing row
                    row.getCell(2).setCellValue(ratingVal);
                    Cell commentCell = row.getCell(3);
                    if (commentCell == null) commentCell = row.createCell(3);
                    commentCell.setCellValue(comment);
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                // Create new row
                int lastRow = sheet.getLastRowNum() + 1;
                Row row = sheet.createRow(lastRow);
                row.createCell(0).setCellValue(userId);
                row.createCell(1).setCellValue(movieId);
                row.createCell(2).setCellValue(ratingVal);
                row.createCell(3).setCellValue(comment);
            }

            StorageManager.saveWorkbook(wb);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public Rating getRating(String userId, String movieId) {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.RATING_SHEET);
            if (sheet == null) return null;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String rUser = getCellValue(row, 0);
                String rMovieId = getCellValue(row, 1);

                if (rUser.equals(userId) && rMovieId.equals(movieId)) {
                    double val = row.getCell(2).getNumericCellValue();
                    String comment = getCellValue(row, 3);
                    return new Rating(userId, movieId, val, comment);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    public List<String> getUserRatedMovieIds(String userId) {
        List<String> ratedIds = new ArrayList<>();
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.RATING_SHEET);
            if (sheet == null) return ratedIds;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String rUser = getCellValue(row, 0);
                if (rUser.equals(userId)) {
                    ratedIds.add(getCellValue(row, 1));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return ratedIds;
    }

    public void deleteRating(String userId, String movieId) {
        try (Workbook wb = openWorkbook()) {
            Sheet sheet = wb.getSheet(StorageManager.RATING_SHEET);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String rUser = getCellValue(row, 0);
                    String rMovieId = getCellValue(row, 1);

                    if (rUser.equals(userId) && rMovieId.equals(movieId)) {
                        sheet.removeRow(row);
                        if (i < sheet.getLastRowNum()) {
                            sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                        }
                        StorageManager.saveWorkbook(wb);
                        return;
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // Helper to safely get cell values as String
    private String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return cell.getStringCellValue();
    }
}