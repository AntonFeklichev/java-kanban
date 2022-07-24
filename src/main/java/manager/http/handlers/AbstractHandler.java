package manager.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import gsonTypeAdapters.ZonedDateTimeAdapter;
import manager.TaskManager;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractHandler implements HttpHandler {

    protected final Logger logger = Logger.getLogger("handler");
    protected Gson gson;
    protected TaskManager manager;
    protected int rCode = 400;
    protected int responseLength = 0;
    protected String response = "";

    public AbstractHandler(TaskManager manager) {
        this.manager = manager;
        gson = createGson();
    }

    private Gson createGson() {
        return gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter()).create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }

    public Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
