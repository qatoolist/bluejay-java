package com.qatoolist.bluejay.core.utils.providers;

import com.qatoolist.bluejay.core.exceptions.DataProviderException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelDataProvider implements the IDataProvider interface to dynamically retrieve test data
 * from Microsoft Excel sheets. It interacts with the Apache POI library for spreadsheet parsing.
 */
public class ExcelDataProvider implements IDataProvider {

    /**
     * Fetches test data from an Excel spreadsheet annotated with @DataFile. Data from the first sheet
     * is parsed and converted into a list of Object arrays for use in data-driven tests.
     *
     * @param testMethod The test method requesting data (used to fetch the @DataFile annotation)
     * @return A List of Object arrays, each array representing a row of data from the Excel sheet.
     * @throws DataProviderException if file errors, formatting issues, or incompatible data are encountered.
     */
    @Override
    public List<Object[]> fetchData(Method testMethod) {
        String fileName = getFileNameFromAnnotation(testMethod);

        List<Object[]> testData = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(fileName));
             Workbook workbook = WorkbookFactory.create(file)) { // Ensure resources are closed

            Sheet sheet = workbook.getSheetAt(0);  // Focus on the first sheet

            for (Row row : sheet) {
                List<Object> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(getCellValue(cell)); // Extract cell value for simpler logic
                }
                testData.add(rowData.toArray());
            }
        } catch (Exception e) {
            throw new DataProviderException("Error reading Excel data: " + fileName, e);
        }
        return testData;
    }

    /**
     * Helper method to retrieve the filename from the test method's @DataFile annotation.
     *
     * @param testMethod The test method
     * @throws DataProviderException if the annotation is missing or the file is not specified.
     * @returns The filename specified in the @DataFile annotation
     */
    private String getFileNameFromAnnotation(Method testMethod) {
        DataFile dataFileAnnotation = testMethod.getAnnotation(DataFile.class);

        if (dataFileAnnotation == null) {
            throw new DataProviderException("Test method is missing the @DataFile annotation");
        }

        String fileName = dataFileAnnotation.value();
        if (fileName.isEmpty()) {
            throw new DataProviderException("The @DataFile annotation is missing a file name");
        }

        return fileName;
    }

    /**
     * Helper method to extract the cell's value and convert it to the appropriate data type.
     *
     * @param cell The Excel cell
     * @return The extracted cell value
     */
    private Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        if(cellType == CellType.FORMULA) {
            // For formulas, evaluate the type of the formula result
            cellType = cell.getCachedFormulaResultType();
        }

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)) {
                    // Return as java.util.Date if the numeric value is formatted as a date
                    return cell.getDateCellValue();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    // Check if the number is an integer or has a decimal part
                    if((numericValue == Math.floor(numericValue)) && !Double.isInfinite(numericValue)) {
                        // Convert to long first to handle large numbers correctly
                        long longValue = (long) numericValue;
                        // Convert to int if it fits, otherwise return as long to avoid data loss
                        return (int) longValue == longValue ? (int) longValue : longValue;
                    } else {
                        return numericValue; // Return as Double if it has a decimal part
                    }
                }
            default:
                return null; // Or you might want to handle error or add more case for CELL_TYPE_ERROR, CELL_TYPE_BLANK
        }
    }
}
