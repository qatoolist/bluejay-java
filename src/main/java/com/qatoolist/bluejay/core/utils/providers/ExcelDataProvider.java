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
     * @param testMethod The test method
     * @returns The filename specified in the @DataFile annotation
     * @throws DataProviderException if the annotation is missing or the file is not specified.
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
     * @param cell The Excel cell
     * @return The extracted cell value
     */
    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            // Add handling for other types (Boolean, Date, Formula) as needed
            default:
                return null; // Or throw a DataProviderException for unhandled types
        }
    }
}
