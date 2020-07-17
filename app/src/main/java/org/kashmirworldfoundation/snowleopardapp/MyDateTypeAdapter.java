package org.kashmirworldfoundation.snowleopardapp;

import com.google.firebase.Timestamp;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

public class MyDateTypeAdapter extends TypeAdapter<Timestamp> {


    @Override
    public Timestamp read(JsonReader in) throws IOException {
        if (in != null)
            return new Timestamp((new Date(in.nextLong() * 1000)));
        else
            return null;
    }



    @Override
    public void write(JsonWriter out, Timestamp value) throws IOException {
        if (value == null)
            out.nullValue();
        else

            out.value(value.toDate().getTime() / 1000);
    }
}
