package manager.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends AbstractHandler {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
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
                    System.out.println(task);
                    manager.addTask(task);
                    System.out.println(manager.getTasks());
                    rCode = 201;
                }
        }
        exchange.sendResponseHeaders(rCode, responseLength);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
        logger.info("response sent " +
                "\n rCode: " + rCode);
    }
}
