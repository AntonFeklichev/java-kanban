package manager.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class TaskHandler extends AbstractHandler {


    public TaskHandler(TaskManager manager) {
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
                response = gson.toJson(manager.getTasks());
                rCode = 200;
                break;
            case "POST":
                try (InputStream is = exchange.getRequestBody()) {
                    Task task = gson.fromJson(new String(is.readAllBytes()), Task.class);
                    manager.addTask(task);
                    rCode = 201;
                }
                break;
            case "DELETE":
                queryMap.ifPresentOrElse((map) -> {
                            if (map.containsKey("id")) {
                                int id = Integer.parseInt(map.get("id"));
                                manager.removeTaskById(id);
                                rCode = 202;
                            }
                        },
                        () -> {
                            manager.removeAllTasks();
                            rCode = 202;
                        });
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
