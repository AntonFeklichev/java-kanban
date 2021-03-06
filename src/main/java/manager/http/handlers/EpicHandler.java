package manager.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class EpicHandler extends AbstractHandler {
    public EpicHandler(TaskManager manager) {
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
                queryMap.ifPresentOrElse(
                        (map) -> {
                            if (map.containsKey("id")) {
                                int id = Integer.parseInt(map.get("id"));
                                response = gson.toJson(manager.getEpicById(id));
                                rCode = 200;
                            }
                        },
                        () -> {
                            response = gson.toJson(manager.getEpics());
                            rCode = 200;
                        });
                break;
            case "POST":
                try (InputStream is = exchange.getRequestBody()) {
                    Epic epic = gson.fromJson(new String(is.readAllBytes()), Epic.class);
                    manager.addEpic(epic);
                    rCode = 201;
                }
                break;
            case "DELETE":
                queryMap.ifPresentOrElse(
                        (map) -> {
                            if (map.containsKey("id")) {
                                int id = Integer.parseInt(map.get("id"));
                                manager.removeEpicById(id);
                                rCode = 202;
                            }
                        },
                        () -> {
                            manager.removeAllEpics();
                            rCode = 202;
                        });
                break;
            case "PATCH":
                try (InputStream is = exchange.getRequestBody()) {
                    Epic epic = gson.fromJson(new String(is.readAllBytes()), Epic.class);
                    epic.setEndTime(epic.calculateEndTime());
                    manager.updateEpic(epic);
                    rCode = 204;
                }
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
