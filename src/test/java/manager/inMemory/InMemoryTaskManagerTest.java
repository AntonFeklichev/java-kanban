package manager.inMemory;

import manager.TaskManagerTest;
import manager.inMemory.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    public InMemoryTaskManager createManager(){
        return new InMemoryTaskManager();
    }
}
