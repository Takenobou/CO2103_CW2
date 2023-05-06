package edu.leicester.co2103;

import org.springframework.http.HttpStatus;

public class ErrorInfo {
    public final String message;

    public ErrorInfo(String message) {
        this.message = message;
    }

}
