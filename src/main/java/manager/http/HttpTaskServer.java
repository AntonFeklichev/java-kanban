package manager.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import manager.http.handlers.*;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class HttpTaskServer {

    private final Logger LOGGER = Logger.getLogger("http task server");
    private Gson gson;
    private TaskManager manager;
    private HttpServer server;


    public HttpTaskServer(String url, int port, int backlog) {
        try {
            manager = Managers.getHttp(url);
            server = HttpServer.create(new InetSocketAddress(port), backlog);
            server.createContext("/tasks/", new TasksHandler(manager));
            server.createContext("/tasks/epic", new EpicHandler(manager));
            server.createContext("/tasks/task", new TaskHandler(manager));
            server.createContext("/tasks/subtask", new SubtaskHandler(manager));
            server.createContext("/tasks/history", new HistoryHandler(manager));
        } catch (BindException e) {
            throw new RuntimeException("cannot bind to the requested address or the server is already bound");
        } catch (IOException e) {
            throw new RuntimeException("cannot create server");
        }
    }

    public void start() {
        server.start();
        LOGGER.info("server started");
    }

    public void stop(int delay) {
        server.stop(delay);
        LOGGER.info("server stopped");
    }
}
