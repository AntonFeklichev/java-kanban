package manager.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import manager.http.handlers.*;
import tasks.Subtask;

import java.io.IOException;
import java.net.InetSocketAddress;
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
            server.createContext("/tasks/", new TasksHandler(manager));
            server.createContext("/tasks/epic", new EpicHandler(manager));
            server.createContext("/tasks/task", new TaskHandler(manager));
            server.createContext("/tasks/subtask", new SubtaskHandler(manager));
            server.createContext("/tasks/history", new HistoryHandler(manager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        server.start();
        LOGGER.info("server started");
    }
}
