package manager;

import manager.exceptions.ManagerSaveException;
import org.json.JSONArray;
import org.json.JSONObject;
import tasks.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final String SAVE_TASKS_PATH = "save/tasks.json";
    private static final String SAVE_EPICS_PATH = "save/epics.json";
    private static final String SAVE_SUBTASKS_PATH = "save/subtasks.json";
    private static final String SAVE_HISTORY_PATH = "save/history.json";

    private static Map<Integer, Task> jsonStringToTaskMap(String jsonString) {
        Map<Integer, Task> taskMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (Object element : jsonArray) {
            JSONObject jsonElement = (JSONObject) element;
            Task task = taskFromJson(jsonElement);
            taskMap.put(task.getId(), task);
        }
        return taskMap;
    }

    private static Map<Integer, Epic> jsonStringToEpicMap(String jsonString) {
        Map<Integer, Epic> epicMap = new HashMap<>();
        JSONArray jsonEpics = new JSONArray(jsonString);
        for (Object element : jsonEpics) {
            JSONObject jsonEpic = (JSONObject) element;
            Epic epic = epicFromJson(jsonEpic);
            epicMap.put(epic.getId(), epic);
        }
        return epicMap;
    }

    private static Map<Integer, Subtask> jsonStringToSubtaskMap(String jsonString) {
        Map<Integer, Subtask> subtaskMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (Object element : jsonArray) {
            JSONObject jsonSubtask = (JSONObject) element;
            Subtask subtask = subtaskFromJson(jsonSubtask);
            subtaskMap.put(subtask.getId(), subtask);
        }
        return subtaskMap;
    }

    private static Task taskFromJson(JSONObject jsonTask) {
        Task newTask = new Task();
        Optional.ofNullable(jsonTask.getString("name")).ifPresent(newTask::setName);
        Optional.ofNullable(jsonTask.getString("desc")).ifPresent(newTask::setDesc);
        Optional.ofNullable(jsonTask.getInt("id")).ifPresent(newTask::setId);
        Optional.ofNullable(jsonTask.getEnum(Status.class, "status")).ifPresent(newTask::setStatus);
        Optional.ofNullable(ZonedDateTime.parse(jsonTask.getString("startTime"))).ifPresent(newTask::setStartTime);
        Optional.ofNullable(jsonTask.getLong("duration")).ifPresent(newTask::setDuration);
        Optional.ofNullable(ZonedDateTime.parse(jsonTask.getString("endTime"))).ifPresent(newTask::setEndTime);


        return newTask;
    }

    private static Epic epicFromJson(JSONObject jsonEpic) {
        Epic newEpic = new Epic();
        Optional.ofNullable(jsonEpic.getString("name")).ifPresent(newEpic::setName);
        Optional.ofNullable(jsonEpic.getString("desc")).ifPresent(newEpic::setDesc);
        Optional.ofNullable(jsonEpic.getInt("id")).ifPresent(newEpic::setId);
        Optional.ofNullable(jsonEpic.getEnum(Status.class, "status")).ifPresent(newEpic::setStatus);
        Optional.ofNullable(ZonedDateTime.parse(jsonEpic.getString("startTime"))).ifPresent(newEpic::setStartTime);
        Optional.ofNullable(jsonEpic.getLong("duration")).ifPresent(newEpic::setDuration);
        Optional.ofNullable(ZonedDateTime.parse(jsonEpic.getString("endTime"))).ifPresent(newEpic::setEndTime);
        Optional.ofNullable(jsonEpic.getJSONArray("subtasks")).ifPresent(subtasks -> subtasks.forEach(subtask -> newEpic.getSubtasks().add(subtaskFromJson((JSONObject) subtask))));
        return newEpic;
    }

    private static Subtask subtaskFromJson(JSONObject jsonSubtask) {
        Subtask newSubtask = new Subtask();
        Optional.ofNullable(jsonSubtask.getString("name")).ifPresent(newSubtask::setName);
        Optional.ofNullable(jsonSubtask.getString("desc")).ifPresent(newSubtask::setDesc);
        Optional.ofNullable(jsonSubtask.getInt("id")).ifPresent(newSubtask::setId);
        Optional.ofNullable(jsonSubtask.getEnum(Status.class, "status")).ifPresent(newSubtask::setStatus);
        Optional.ofNullable(ZonedDateTime.parse(jsonSubtask.getString("startTime"))).ifPresent(newSubtask::setStartTime);
        Optional.ofNullable(jsonSubtask.getLong("duration")).ifPresent(newSubtask::setDuration);
        Optional.ofNullable(ZonedDateTime.parse(jsonSubtask.getString("endTime"))).ifPresent(newSubtask::setEndTime);
        Optional.ofNullable(jsonSubtask.getInt("epicId")).ifPresent(newSubtask::setEpicId);
        return newSubtask;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save(getTasks(), SAVE_TASKS_PATH);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save(getEpics(), SAVE_EPICS_PATH);
        save(getSubtasks(), SAVE_SUBTASKS_PATH);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save(getEpics(), SAVE_EPICS_PATH);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save(getTasks(), SAVE_TASKS_PATH);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save(getSubtasks(), SAVE_SUBTASKS_PATH);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save(getEpics(), SAVE_EPICS_PATH);
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save(getTasks(), SAVE_TASKS_PATH);
    }

    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
        save(getEpics(), SAVE_EPICS_PATH);
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        super.removeSubtaskById(subtaskId);
        save(getSubtasks(), SAVE_SUBTASKS_PATH);
    }

    private String mapToJsonString(Map<Integer, ? extends Task> map) {
        return new JSONArray(map.values()).toString();
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

    private void saveHistory() {
        try {
            Path save = Path.of(SAVE_HISTORY_PATH);
            if (!Files.exists(save)) Files.createFile(save);
            try (PrintWriter saver = new PrintWriter(SAVE_HISTORY_PATH)) {
                saver.write(new JSONArray(getHistory()).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        save(getTasks(), SAVE_TASKS_PATH);
        save(getEpics(), SAVE_EPICS_PATH);
        save(getSubtasks(), SAVE_SUBTASKS_PATH);
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

    public void loadHistoryFromFile(Path path) {
        try {
            String jsonString = Files.readString(path);
            JSONArray jsonArray = new JSONArray(jsonString);
            for (Object element : jsonArray) {
                JSONObject jsonElement = (JSONObject) element;
                if (jsonElement.getEnum(TaskTypes.class, "type").equals(TaskTypes.TASK))
                    super.getHistoryManager().add(taskFromJson(jsonElement));
                else if (jsonElement.getEnum(TaskTypes.class, "type").equals(TaskTypes.EPIC))
                    super.getHistoryManager().add(epicFromJson(jsonElement));
                else if (jsonElement.getEnum(TaskTypes.class, "type").equals(TaskTypes.SUBTASK))
                    super.getHistoryManager().add(subtaskFromJson(jsonElement));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке истории из файла");
        }
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
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        saveHistory();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        saveHistory();
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        saveHistory();
        return epic;
    }
}
