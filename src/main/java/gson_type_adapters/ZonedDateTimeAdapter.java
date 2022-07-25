package gson_type_adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
    private final DateTimeFormatter formatterWriter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private final DateTimeFormatter formatterReader = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    public void write(JsonWriter jsonWriter, ZonedDateTime zonedDateTime) throws IOException {
        if (zonedDateTime != null) {
            jsonWriter.value(zonedDateTime.format(formatterWriter));
        } else {
            jsonWriter.value("null");
        }
    }

    @Override
    public ZonedDateTime read(JsonReader jsonReader) throws IOException {
        String zonedDateTime = jsonReader.nextString();
        if (!zonedDateTime.equals("null")) {
            return ZonedDateTime.parse(zonedDateTime, formatterReader);
        } else return null;
    }

}