public class Go {
    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer(8080, 0);
        server.start();
    }
}
