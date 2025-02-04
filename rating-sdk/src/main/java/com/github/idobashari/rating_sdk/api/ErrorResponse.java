package com.github.idobashari.rating_sdk.api;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("error")
    private String message;

    @SerializedName("status_code")
    private int statusCode;

    public ErrorResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }


    public boolean isAuthError() {
        return statusCode == 401 || statusCode == 403;
    }

    public boolean isValidationError() {
        return statusCode == 400;
    }

    public boolean isNotFoundError() {
        return statusCode == 404;
    }

    public boolean isConflictError() {
        return statusCode == 409;
    }

    public boolean isServerError() {
        return statusCode >= 500;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}