package manager;

import manager.exceptions.ManagerSaveException;
import org.json.JSONArray;
import org.json.JSONObject;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

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
    private final static String saveEpicsPath = "epics.json";
    private final static String saveSubtasksPath = "subtasks.json";


    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save(getTasks(), saveTasksPath);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save(getEpics(), saveEpicsPath);
        save(getSubtasks(), saveSubtasksPath);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save(getEpics(), saveEpicsPath);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save(getTasks(), saveTasksPath);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save(getSubtasks(), saveSubtasksPath);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save(getEpics(), saveEpicsPath);
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save(getTasks(), saveTasksPath);
    }

    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
        save(getEpics(), saveEpicsPath);
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        super.removeSubtaskById(subtaskId);
        save(getSubtasks(), saveSubtasksPath);
    }

    private String mapToJsonString(Map<Integer, ? extends Task> map) {
//        List<Task> tasksList = new ArrayList<>(map.values());
        JSONArray tasksJson = new JSONArray(map.values());
        return tasksJson.toString();
    }

    private void save(Map<Integer, ? extends Task> map, String savePath) {
        try {
            Path save = Path.of(savePath);
            if (!Files.exists(save)) Files.createFile(save);
            try (PrintWriter saver = new PrintWriter(savePath)) {
                saver.write(mapToJsonString(map));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTasksFromFile(Path path) {
        try {
            String jsonString = Files.readString(path);
            setTasks(jsonStringToTaskMap(jsonString));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке задач из файла");
        }
    }

    public void loadEpicsFromFile(Path path) {
        try {
            String jsonString = Files.readString(path);
            setEpics(jsonStringToEpicMap(jsonString));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке эпиков из файла");
        }
    }

    public void loadSubtasksFromFile(Path path) {
        try {
            String jsonString = Files.readString(path);
            setSubtasks(jsonStringToSubtaskMap(jsonString));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке подзадач из файла");
        }
    }

    private Map<Integer, Subtask> jsonStringToSubtaskMap(String jsonString) {
        Map<Integer, Subtask> subtaskMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (Object subtask : jsonArray) {
            JSONObject jsonSubtask = (JSONObject) subtask;
            String subtaskName = jsonSubtask.getString("name");
            String subtaskDesc = jsonSubtask.getString("desc");
            int epicId = jsonSubtask.getInt("epicId");
            int subtaskId = jsonSubtask.getInt("id");
            Status subtaskStatus = jsonSubtask.getEnum(Status.class, "status");
            subtaskMap.put(subtaskId, new Subtask(subtaskName, subtaskDesc, subtaskStatus, epicId, subtaskId));
        }
        return subtaskMap;
    }

    private Map<Integer, Epic> jsonStringToEpicMap(String jsonString) {
        Map<Integer, Epic> epicMap = new HashMap<>();
        JSONArray jsonEpics = new JSONArray(jsonString);
        for (Object epic : jsonEpics) {
            JSONObject jsonEpic = (JSONObject) epic;
            String name = jsonEpic.getString("name");
            String desc = jsonEpic.getString("desc");
            int epicId = jsonEpic.getInt("id");
            Status status = jsonEpic.getEnum(Status.class, "status");
            JSONArray jsonSubtasks = jsonEpic.getJSONArray("subtasks");
            List<Subtask> subtasks = new ArrayList<>();
            for (Object subtask : jsonSubtasks) {
                JSONObject jsonSubtask = (JSONObject) subtask;
                String subtaskName = jsonSubtask.getString("name");
                String subtaskDesc = jsonSubtask.getString("desc");
                int subtaskId = jsonSubtask.getInt("id");
                Status subtaskStatus = jsonSubtask.getEnum(Status.class, "status");
                subtasks.add(new Subtask(subtaskName, subtaskDesc, subtaskStatus, epicId, subtaskId));
            }
            epicMap.put(epicId, new Epic(name, desc, status, epicId, subtasks));
        }
        return epicMap;
    }

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
        save(getTasks(), saveTasksPath);
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save(getSubtasks(), saveSubtasksPath);
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save(getTasks(), saveEpicsPath);
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
