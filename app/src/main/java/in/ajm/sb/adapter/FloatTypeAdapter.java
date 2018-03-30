package in.ajm.sb.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by DSD on 06/10/17.
 */

public class FloatTypeAdapter extends TypeAdapter<Float>
{
	private float defaultValue = 0.00f;

	public FloatTypeAdapter()
	{
	}

	public FloatTypeAdapter(float defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	@Override
	public Float read(JsonReader reader) throws IOException
	{
		if (reader.peek() == JsonToken.NULL)
		{
			reader.nextNull();
			return this.defaultValue;
		}
		String stringValue = reader.nextString();
		try
		{
			return Float.valueOf(stringValue);
		} catch (NumberFormatException e)
		{
			return this.defaultValue;
		}
	}

	@Override
	public void write(JsonWriter writer, Float value) throws IOException
	{
		if (value == null)
		{
			writer.nullValue();
			return;
		}
		writer.value(value);
	}
}