package com.lasalle.meet.exceptions.apierrors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StackTrace {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("errno")
    @Expose
    private Integer errno;
    @SerializedName("sqlState")
    @Expose
    private String sqlState;
    @SerializedName("sqlMessage")
    @Expose
    private String sqlMessage;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String getSqlState() {
        return sqlState;
    }

    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }

    public String getSqlMessage() {
        return sqlMessage;
    }

    public void setSqlMessage(String sqlMessage) {
        this.sqlMessage = sqlMessage;
    }
}
