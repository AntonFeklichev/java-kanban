package manager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{
    @Override
    public InMemoryTaskManager createManager(){
        return new InMemoryTaskManager();
    }
}
