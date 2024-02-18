package com.qatoolist.bluejay.exceptions;

public class UnsupportedBrowserException extends RuntimeException {
    public UnsupportedBrowserException(String message) {
        super(message);
    }

    public UnsupportedBrowserException(String message, Throwable cause) {
        super(message, cause);
    }
}

