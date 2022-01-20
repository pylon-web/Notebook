package com.saurabh.androidnotes;

import android.util.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

public class Notes implements Serializable {

    private String title;
    private String detail;
    private String millis;
    //    private static int ctr = 1;
//
//    public Notes(){
//        this.title = "Title"+ctr;
//        this.detail ="Details"+ctr;
//        ctr++;
//    }
    public Notes(String title, String detail ,String millis) {
        this.title = title;
        this.detail = detail;
        this.millis = millis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMillis() {
        return millis;
    }

    @Override
    public String toString() {

        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getTitle());
            jsonWriter.name("detail").value(getDetail());
            jsonWriter.name("date").value(getMillis());
            jsonWriter.endObject();
            jsonWriter.close();
            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}
