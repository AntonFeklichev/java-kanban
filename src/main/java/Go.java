import manager.http.HttpTaskServer;
import manager.http.kv.KVServer;
import manager.http.kv.KVTaskClient;

import java.io.IOException;

public class Go {
    public static void main(String[] args) throws IOException {
//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
//                .create();
        HttpTaskServer server = new HttpTaskServer(8080, 0);
        server.start();
        KVServer kvServer = new KVServer();
        kvServer.start();
        KVTaskClient client = new KVTaskClient("http://localhost:8078");
    }
}
