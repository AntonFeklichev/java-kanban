package manager.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import manager.http.handlers.TaskHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class HttpTaskServer {

    private final Logger LOGGER = Logger.getLogger("http task server");
    private Gson gson;
    private TaskManager manager;
    private HttpServer server;


    public HttpTaskServer(int port, int backlog) {
        try {
            manager = Managers.getFileBacked();
            server = HttpServer.create(new InetSocketAddress(port), backlog);
//            server.createContext("/tasks", new TasksHandler(manager));
            server.createContext("/tasks/task", new TaskHandler(manager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        server.start();
        LOGGER.info("server started");
    }

    private class TasksHandler implements HttpHandler {
        private TaskManager manager;

        public TasksHandler(TaskManager manager) {
            this.manager = manager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int rCode = 400;
            int responseLength = 0;
            String response = "";
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            LOGGER.info("handling request\n" +
                    "method: " + method +
                    "\npath: " + path);
            switch (path) {
                case "/tasks":
                    switch (method) {
                        case "GET":
                            response = gson.toJson(manager.getTasksOfAllTypes());
                            rCode = 200;
                            break;
                    }
                    break;
                case "/tasks/epic":
                    switch (method) {
                        case "GET":
                            response = gson.toJson(manager.getEpics());
                            rCode = 200;
                            break;
                    }
                    break;
                case "/tasks/subtask":
                    switch (method) {
                        case "GET":
                            response = gson.toJson(manager.getSubtasks());
                            rCode = 200;
                            break;
                    }
                    break;
                case "/tasks/task":
                    switch (method) {
                        case "GET":
                            response = gson.toJson(manager.getTasks());
                            rCode = 200;
                            break;
                    }

                default:
            }
            exchange.sendResponseHeaders(rCode, responseLength);
            LOGGER.info("response: " + response +
                    "\n rCode: " + rCode);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
