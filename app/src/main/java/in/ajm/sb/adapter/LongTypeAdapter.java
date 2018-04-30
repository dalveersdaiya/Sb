package in.ajm.sb.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by DSD on 06/10/17.
 */

public class LongTypeAdapter extends TypeAdapter<Long> {
    private long defaultValue = 0L;

    public LongTypeAdapter() {
    }

    public LongTypeAdapter(long defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Long read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return this.defaultValue;
        }
        String stringValue = reader.nextString();
        try {
            return Long.valueOf(stringValue);
        } catch (NumberFormatException e) {
            return this.defaultValue;
        }
    }

    @Override
    public void write(JsonWriter writer, Long value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }
}