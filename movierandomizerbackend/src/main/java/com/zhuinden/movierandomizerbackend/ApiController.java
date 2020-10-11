package com.zhuinden.movierandomizerbackend;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RequestMapping("/api")
@Controller
public class ApiController {
    @Autowired
    MovieFileResolver movieFileResolver;

    @ResponseBody
    @RequestMapping(value = "/movies", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<Movie> getMovies() {

        List<Movie> movies = new LinkedList<>();
        try {
            File f = new File(movieFileResolver.getMovieFilePath());
            if(!f.exists()) {
                throw new RuntimeException("The specified file [" + f.getName() + "] at path [" + f.getAbsolutePath() + "] does not exist!");
            }

            try(FileInputStream file = new FileInputStream(f)) {
                Workbook workbook = WorkbookFactory.create(file);
                Sheet sheet = workbook.getSheetAt(0);

                int rowCounter = 0;
                for(int i = 0; i < sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    rowCounter++;
                    if(rowCounter <= 1) {
                        continue;
                    }
                    if(checkIfRowIsEmpty(row)) {
                        break;
                    }
                    Movie movie = new Movie();
                    for(int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        String value = extractValueFromCell(cell);
                        switch(cellIndex) {
                            case 0:
                                movie.setFilmName(value);
                                break;
                            case 1:
                                movie.setGenre(value);
                                break;
                            case 2:
                                movie.setPartOfASeries(parseBoolCell(value));
                                break;
                            case 3:
                                movie.setSeriesName(value);
                                break;
                            case 4:
                                movie.setSeriesNumber(Integer.valueOf(value));
                                break;
                            case 5:
                                movie.setWatched(parseBoolCell(value));
                                break;
                            default:
                                // unknown column
                                break;
                        }
                    }
                    movies.add(movie);
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(movies);
    }

    private boolean parseBoolCell(String value) {
        boolean boolValue = false;
        if("Y".equals(value)) {
            boolValue = true;
        }
        return boolValue;
    }

    private String extractValueFromCell(Cell cell) {
        String value = null;
        if(cell != null) {
            if(cell.getCellTypeEnum() == CellType.STRING) {
                value = cell.getStringCellValue();
            } else if(cell.getCellTypeEnum() == CellType.BOOLEAN) {
                value = "" + cell.getBooleanCellValue();
            } else if(cell.getCellTypeEnum() == CellType.BLANK) {
                value = null;
            } else if(cell.getCellTypeEnum() == CellType.NUMERIC) {
                value = "" + NumberToTextConverter.toText(cell.getNumericCellValue());
            } else if(cell.getCellTypeEnum() == CellType.FORMULA) {
                value = cell.getCellFormula();
            } else if(cell.getCellTypeEnum() == CellType.ERROR) {
                value = "" + cell.getErrorCellValue();
            }
        }
        return value;
    }

    private boolean checkIfRowIsEmpty(Row row) {
        if(row == null) {
            return true;
        }
        if(row.getLastCellNum() <= 0) {
            return true;
        }
        boolean isEmptyRow = true;
        for(int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if(cell != null && cell.getCellTypeEnum() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                isEmptyRow = false;
            }
        }
        return isEmptyRow;
    }
}
