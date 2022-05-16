package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;


public interface TaskManager {
    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, Subtask> getSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    void generateAndSetTaskId(Task task);

    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void removeTaskById(int id);

    void removeEpicById(int epicId);

    void removeSubtaskById(int subtaskId);

    ArrayList<Subtask> getSubtasksOfEpic(Epic epic);

    Status calculateStatus(Epic epic);

    ArrayList<Task> getHistory();
}
