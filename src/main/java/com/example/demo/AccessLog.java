package com.example.demo;
// JSON name convention is camelCase
public class AccessLog {
    //
    private long   id_column;
    private String pageName;
    private String accessDate;
    private String ipValue;
    //
    public AccessLog(long id_column, String pageName, String accessDate, String ipValue) {
        this.id_column      = id_column;
        this.pageName       = pageName;
        this.accessDate     = accessDate;
        this.ipValue        = ipValue;
    }
    // Getters and Setters
    public long getid_column() {
        return id_column;
    }

    public void setID_columnn(long id_column) {
        this.id_column = id_column;
    }

    public String getpageName() {
        return pageName;
    }

    public void setpageName(String pageName) {
        this.pageName = pageName;
    }

    public String getaccessDate() {
        return accessDate;
    }

    public void setaccessDate(String accessDate) {
        this.accessDate = accessDate;
    }

    public String getipValue() {
        return ipValue;
    }

    public void setIpValue(String ipValue) {
        this.ipValue = ipValue;
    }


}
