package cinerator.storage;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class StorageManager {

    public static final File EXCEL_FILE = new File("cinerator.xlsx");

    public static final String MOVIE_SHEET = "Movies";
    public static final String USER_SHEET = "Users";
    public static final String RATING_SHEET = "Ratings";

    public static Workbook openWorkbook() throws IOException {

        Workbook workbook;

        if (EXCEL_FILE.exists()) {
            try (FileInputStream fis = new FileInputStream(EXCEL_FILE)) {
                workbook = WorkbookFactory.create(fis);
            }
        } else {
            workbook = new XSSFWorkbook();
            createSheets(workbook);
            saveWorkbook(workbook);
        }

        ensureSheetsExist(workbook);
        return workbook;
    }

    private static void createSheets(Workbook workbook) {
        createSheetWithHeader(workbook, MOVIE_SHEET,
                "id", "title", "genre");

        createSheetWithHeader(workbook, USER_SHEET,
                "id", "username", "password");

        createSheetWithHeader(workbook, RATING_SHEET,
                "userId", "movieId", "rating");
    }

    private static void ensureSheetsExist(Workbook workbook) {
        if (workbook.getSheet(MOVIE_SHEET) == null)
            createSheetWithHeader(workbook, MOVIE_SHEET,
                    "id", "title", "genre");

        if (workbook.getSheet(USER_SHEET) == null)
            createSheetWithHeader(workbook, USER_SHEET,
                    "id", "username", "password");

        if (workbook.getSheet(RATING_SHEET) == null)
            createSheetWithHeader(workbook, RATING_SHEET,
                    "userId", "movieId", "rating");
    }

    private static void createSheetWithHeader(
            Workbook wb, String name, String... headers) {

        Sheet sheet = wb.createSheet(name);
        Row header = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
    }

    public static void saveWorkbook(Workbook workbook) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(EXCEL_FILE)) {
            workbook.write(fos);
        }
    }
}
