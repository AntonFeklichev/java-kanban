package manager.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class TasksHandler extends AbstractHandler {
    public TasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath().toLowerCase();
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        Optional<Map<String, String>> queryMap = Optional.ofNullable(queryToMap(query));
        logger.info("handling request\n" +
                "method: " + method +
                "\npath: " + path);
        switch (method) {
            case "GET":
                response = gson.toJson(manager.getTasksOfAllTypes());
                rCode = 200;
                break;
            case "DELETE":
                manager.removeAll();
                rCode = 202;
                break;
        }
        logger.info("response sent " +
                "\nrCode: " + rCode);
        exchange.sendResponseHeaders(rCode, responseLength);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
