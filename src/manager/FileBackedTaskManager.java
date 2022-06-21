package manager;

import manager.exceptions.ManagerSaveException;
import org.json.JSONArray;
import org.json.JSONObject;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final static String saveTasksPath = "tasks.json";

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        saveTasks();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        saveTasks();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        saveTasks();
    }

    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        super.removeSubtaskById(subtaskId);
    }

    private String mapToJsonString(Map<Integer, Task> map) {
        List<Task> tasksList = new ArrayList<>(map.values());
        JSONArray tasksJson = new JSONArray(tasksList);
        return tasksJson.toString();
    }

    private void saveTasks() {
        try {
            Path save = Path.of(saveTasksPath);
            if (!Files.exists(save)) Files.createFile(save);
            try (PrintWriter saver = new PrintWriter(saveTasksPath)) {
                saver.write(mapToJsonString(super.getTasks()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTasksFromFile(Path path) {
        try {
            String jsonString = Files.readString(path);
            super.setTasks(jsonStringToTaskMap(jsonString));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке задач из файла");
        }
    }

//    private static void loadEpicsFromFile(Path path) {
//        try {
//            String jsonString = Files.readString(path);
//            super.setEpics(jsonStringToEpicMap(jsonString));
//        } catch (IOException e) {
//            throw new ManagerSaveException("Ошибка при загрузке задач из файла");
//        }
//    }

    private static void loadSubtasksFromFile(Path path) {

    }

//    private Map<Integer, Epic> jsonStringToEpicMap(String jsonString){
//        Map<Integer, Epic> epicMap = new HashMap<>();
//        JSONArray jsonArray = new JSONArray(jsonString);
//        for (Object element : jsonArray) {
//            JSONObject jsonElement = (JSONObject) element;
//            String name = jsonElement.getString("name");
//            String desc = jsonElement.getString("desc");
//            int id = jsonElement.getInt("id");
//            Status status = jsonElement.getEnum(Status.class, "status");
//
//            epicMap.put(id, new Epic(name, desc, status, id, subtasks));
//        }
//        return epicMap;
//    }

    private Map<Integer, Task> jsonStringToTaskMap(String jsonString) {
        Map<Integer, Task> taskMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (Object element : jsonArray) {
            JSONObject jsonElement = (JSONObject) element;
            String name = jsonElement.getString("name");
            String desc = jsonElement.getString("desc");
            int id = jsonElement.getInt("id");
            Status status = jsonElement.getEnum(Status.class, "status");
            taskMap.put(id, new Task(name, desc, status, id));
        }
        return taskMap;
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        saveTasks();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        saveTasks();
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return super.getSubtaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
    }
}
