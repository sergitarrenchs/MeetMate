package com.lasalle.meet.exceptions.apierrors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIError {

    @SerializedName("Error")
    @Expose
    private String error;
    @SerializedName("stackTrace")
    @Expose
    private StackTrace stackTrace;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public StackTrace getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTrace stackTrace) {
        this.stackTrace = stackTrace;
    }
}