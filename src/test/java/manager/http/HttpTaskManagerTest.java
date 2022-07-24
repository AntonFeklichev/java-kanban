package manager.http;

import manager.TaskManagerTest;
import manager.fileBacked.FileBackedTaskManagerTest;

public class HttpTaskManagerTest extends FileBackedTaskManagerTest{
    @Override
    public HttpTaskManager createManager() {
        return new HttpTaskManager("http://localhost:8078");
    }
}
