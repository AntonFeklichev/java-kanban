package manager.http;

import manager.fileBacked.FileBackedTaskManager;

public class HttpTaskManager extends FileBackedTaskManager {


    public HttpTaskManager(String saveDir) {
        super(saveDir);
    }
}
