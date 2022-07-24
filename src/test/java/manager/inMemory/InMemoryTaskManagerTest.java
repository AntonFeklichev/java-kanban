package manager.inMemory;

import manager.TaskManagerTest;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    public InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}
