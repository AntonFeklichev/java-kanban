package manager.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import gsonTypeAdapters.ZonedDateTimeAdapter;
import manager.TaskManager;

import java.io.IOException;
import java.time.ZonedDateTime;
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
}
