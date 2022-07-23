package manager.http.kv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String API_TOKEN;
    private HttpClient client;
    private String url;

    public KVTaskClient(String url) {
        client = HttpClient.newHttpClient();
        this.url = url;
        API_TOKEN = register();
    }

    private String register() {
        try {
            URI uri = URI.create(url.concat("/register"));
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void put(String key, String json) {
        try {
            URI uri = URI.create(String.format("%s/save/%s?API_TOKEN=%s", url, key, API_TOKEN));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String load(String key) {
        try {
            URI uri = URI.create(String.format("%s/load/%s?API_TOKEN=%s", url, key, API_TOKEN));
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
