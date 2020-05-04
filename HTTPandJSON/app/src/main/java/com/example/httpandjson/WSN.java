package com.example.httpandjson;

public class WSN {

    private String GroupID;
    private String XH;
    private String Value;
    private String Date;

    public WSN(String groupID, String xh, String value, String date) {
        this.GroupID = groupID;
        this.XH = xh;
        this.Value = value;
        this.Date = date;
    }

    public String getGroupID() {
        return GroupID;
    }

    public String getXH() {
        return XH;
    }

    public String getValue() {
        return Value;
    }

    public String getDate() {
        return Date;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public void setXH(String xh) {
        XH = xh;
    }

    public void setValue(String value) {
        Value = value;
    }

    public void setDate(String date) {
        Date = date;
    }
}
