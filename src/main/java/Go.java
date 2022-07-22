import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonTypeAdapters.ZonedDateTimeAdapter;
import manager.http.HttpTaskServer;

import java.time.ZonedDateTime;

public class Go {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        HttpTaskServer server = new HttpTaskServer(8080, 0);
        server.start();
    }
}
