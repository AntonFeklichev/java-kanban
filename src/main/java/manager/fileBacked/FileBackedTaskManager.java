package manager.fileBacked;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import gsonTypeAdapters.ZonedDateTimeAdapter;
import manager.exceptions.ManagerSaveException;
import manager.inMemory.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private Gson gson;
    private String saveDir = "save";

    public FileBackedTaskManager(String saveDir) {
        this.saveDir = saveDir;
        gson = createGson();
        loadData();
    }

    private Gson createGson() {
        return gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter()).create();
    }

    private void loadData() {
        loadTasks();
        loadEpics();
        loadSubtasks();
        loadHistory();
    }

    private void loadTasks() {
        String path = saveDir + "/tasks.json";
        try {
            if (!Files.exists(Path.of(path))) return;
            try (Reader reader = new FileReader(path)) {
                Type type = new TypeToken<Map<Integer, Task>>() {
                }.getType();
                Map<Integer, Task> loadedTasks = gson.fromJson(reader, type);
                setTasks(loadedTasks == null ? new HashMap<>() : loadedTasks);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadEpics() {
        String path = saveDir + "/epics.json";
        try {
            if (!Files.exists(Path.of(path))) return;
            try (Reader reader = new FileReader(path)) {
                Type type = new TypeToken<Map<Integer, Epic>>() {
                }.getType();
                Map<Integer, Epic> loadedEpics = gson.fromJson(reader, type);
                setEpics(loadedEpics == null ? new HashMap<>() : loadedEpics);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSubtasks() {
        String path = saveDir + "/subtasks.json";
        try {
            if (!Files.exists(Path.of(path))) return;
            try (Reader reader = new FileReader(path)) {
                Type type = new TypeToken<Map<Integer, Subtask>>() {
                }.getType();
                Map<Integer, Subtask> loadedSubtasks = gson.fromJson(reader, type);
                setSubtasks(loadedSubtasks == null ? new HashMap<>() : loadedSubtasks);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadHistory() {
        String path = saveDir + "/history.json";
        try {
            if (!Files.exists(Path.of(path))) return;
            try (Reader reader = new FileReader(path)) {
                JsonArray historyArray = JsonParser.parseReader(reader).getAsJsonArray();
                for (JsonElement element : historyArray) {
                    if (element.getAsJsonObject().get("type") == null) {
                        Task def = gson.fromJson(element, Task.class);
                        getHistoryManager().add(def);
                    } else {

                        switch (element.getAsJsonObject().get("type").getAsString()) {
                            case "TASK":
                                Task task = gson.fromJson(element, Task.class);
                                getHistoryManager().add(task);
                                break;
                            case "EPIC":
                                Epic epic = gson.fromJson(element, Epic.class);
                                getHistoryManager().add(epic);
                                break;
                            case "SUBTASK":
                                Subtask subtask = gson.fromJson(element, Subtask.class);
                                getHistoryManager().add(subtask);
                                break;
                            default:
                                Task def = gson.fromJson(element, Task.class);
                                getHistoryManager().add(def);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveData() {
        saveTasks();
        saveEpics();
        saveSubtasks();
        saveHistory();
    }

    private void saveTasks() {
        String path = saveDir + "/tasks.json";
        try {
            if (!Files.exists(Path.of(path))) Files.createFile(Path.of(path));
            try (Writer writer = new FileWriter(path)) {
                String jsonTasks = gson.toJson(getTasks());
                writer.write(jsonTasks);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("error while saving tasks");
        }
    }

    private void saveEpics() {
        String path = saveDir + "/epics.json";
        try {
            if (!Files.exists(Path.of(path))) Files.createFile(Path.of(path));
            try (Writer writer = new FileWriter(path)) {
                String jsonEpics = gson.toJson(getEpics());
                writer.write(jsonEpics);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("error while saving epics");
        }
    }

    private void saveSubtasks() {
        String path = saveDir + "/subtasks.json";
        try {
            if (!Files.exists(Path.of(path))) Files.createFile(Path.of(path));
            try (Writer writer = new FileWriter(path)) {
                String jsonSubtasks = gson.toJson(getSubtasks());
                writer.write(jsonSubtasks);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("error while saving subtasks");
        }
    }

    public void saveHistory() {
        String path = saveDir + "/history.json";
        try {
            if (!Files.exists(Path.of(path))) Files.createFile(Path.of(path));
            try (Writer writer = new FileWriter(path)) {
                String jsonHistory = gson.toJson(getHistory());
                writer.write(jsonHistory);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("error while saving history");
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        saveTasks();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        saveEpics();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        saveSubtasks();
        saveEpics();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        saveTasks();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        saveEpics();
        saveSubtasks();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        saveSubtasks();
        saveEpics();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        saveHistory();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        saveHistory();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        saveHistory();
        return subtask;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        saveTasks();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        saveEpics();
        saveSubtasks();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        saveSubtasks();
        saveEpics();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        saveTasks();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        saveSubtasks();
        saveEpics();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        saveEpics();
        saveSubtasks();
    }

    @Override
    public void removeAll(){
        super.removeAll();
        saveData();
    }

}
