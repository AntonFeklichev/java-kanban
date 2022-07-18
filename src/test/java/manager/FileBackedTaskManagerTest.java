package manager;

import org.junit.jupiter.api.BeforeEach;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
   @Override
    public FileBackedTaskManager createManager(){
       return new FileBackedTaskManager();
   }
}
