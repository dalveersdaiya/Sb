package in.ajm.sb.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by ajm on 06/10/17.
 */

public class IntegerTypeAdapter extends TypeAdapter<Integer> {
    private int defaultValue = 0;

    public IntegerTypeAdapter() {
    }

    public IntegerTypeAdapter(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Integer read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return this.defaultValue;
        }
        String stringValue = reader.nextString();
        try {
            return Integer.valueOf(stringValue);
        } catch (NumberFormatException e) {
            return this.defaultValue;
        }
    }

    @Override
    public void write(JsonWriter writer, Integer value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }
}