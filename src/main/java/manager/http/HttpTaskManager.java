package manager.http;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import manager.fileBacked.FileBackedTaskManager;
import manager.http.kv.KVTaskClient;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

public class HttpTaskManager extends FileBackedTaskManager {

    private final KVTaskClient kvClient;
    private String url;


    public HttpTaskManager(String url) {
        kvClient = new KVTaskClient(url);
        loadData();
    }


    public void loadData() {
        loadTasks();
        loadEpics();
        loadSubtasks();
        loadHistory();
    }

    public void saveData() {
        saveTasks();
        saveEpics();
        saveSubtasks();
        saveHistory();
    }

    @Override
    protected void loadTasks() {
        String loadedJson = kvClient.load("tasks");
        Type type = new TypeToken<Map<Integer, Task>>() {
        }.getType();
        Optional<Map<Integer, Task>> loadedTasks = Optional.ofNullable(gson.fromJson(loadedJson, type));
        loadedTasks.ifPresent(this::setTasks);
    }

    @Override
    protected void saveTasks() {
        String jsonTasks = gson.toJson(getTasks());
        kvClient.put("tasks", jsonTasks);
    }

    @Override
    protected void loadEpics() {
        String loadedJson = kvClient.load("epics");
        Type type = new TypeToken<Map<Integer, Epic>>() {
        }.getType();
        Optional<Map<Integer, Epic>> loadedEpics = Optional.ofNullable(gson.fromJson(loadedJson, type));
        loadedEpics.ifPresent(this::setEpics);
    }

    @Override
    protected void saveEpics() {
        String jsonEpics = gson.toJson(getEpics());
        kvClient.put("epics", jsonEpics);
    }

    @Override
    protected void loadSubtasks() {
        String loadedJson = kvClient.load("subtasks");
        Type type = new TypeToken<Map<Integer, Subtask>>() {
        }.getType();
        Optional<Map<Integer, Subtask>> loadedSubtasks = Optional.ofNullable(gson.fromJson(loadedJson, type));
        loadedSubtasks.ifPresent(this::setSubtasks);
    }

    @Override
    protected void saveSubtasks() {
        String jsonSubtasks = gson.toJson(getSubtasks());
        kvClient.put("subtasks", jsonSubtasks);
    }

    @Override
    protected void loadHistory() {
        String loadedJson = kvClient.load("history");
        Optional<JsonElement> parsedHistory = Optional.ofNullable(JsonParser.parseString(loadedJson));
        parsedHistory.ifPresent(
                (history) -> {
                    if (history.isJsonArray()) {
                        JsonArray historyArray = history.getAsJsonArray();
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
                });
    }

    @Override
    protected void saveHistory() {
        String jsonHistory = gson.toJson(getHistory());
        kvClient.put("history", jsonHistory);
    }
}
