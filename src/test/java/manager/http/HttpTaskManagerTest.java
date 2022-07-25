package manager.http;

import manager.file_backed.FileBackedTaskManagerTest;
import manager.http.kv.KVServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public class HttpTaskManagerTest extends FileBackedTaskManagerTest {
    private static KVServer kvServer;
    private static HttpTaskServer server;

    @BeforeAll
    public static void startServers() throws IOException {
        kvServer = new KVServer("localhost", 8078, 0);
        kvServer.start();
        server = new HttpTaskServer("http://localhost:8078", 8080, 0);
        server.start();
    }

    @AfterAll
    public static void stopServers() {
        kvServer.stop(10);
        server.stop(10);
    }

    @Override
    public HttpTaskManager createManager() {
        return new HttpTaskManager("http://localhost:8078");
    }
}
