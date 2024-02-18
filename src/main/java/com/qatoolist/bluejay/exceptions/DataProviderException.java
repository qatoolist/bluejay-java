package com.qatoolist.bluejay.exceptions;

/**
 * DataProviderException represents an exception that occurs during data retrieval
 * by a data provider (e.g.,  ExcelDataProvider).
 */
public class DataProviderException extends RuntimeException {

    public DataProviderException(String message) {
        super(message);
    }

    public DataProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}

