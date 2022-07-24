package manager.http;

import manager.fileBacked.FileBackedTaskManagerTest;
import manager.http.kv.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class HttpTaskManagerTest extends FileBackedTaskManagerTest {
    private KVServer kvServer;
    private HttpTaskServer server;

    @BeforeEach
    public void startServers() throws IOException {
        kvServer = new KVServer();
        server = new HttpTaskServer("http://localhost:8078", 8080, 0);
        kvServer.start();
        server.start();
    }

    @AfterEach
    public void stopServers() {
        kvServer.stop(10);
        server.stop(10);
    }

    @Override
    public HttpTaskManager createManager() {
        return new HttpTaskManager("http://localhost:8078");
    }
}
