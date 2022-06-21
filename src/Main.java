import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

//        TaskManager manager = Managers.getDefault();
        FileBackedTaskManager manager = new FileBackedTaskManager();
        System.out.println(manager.getTasks());
        manager.loadTasksFromFile(Path.of("tasks.json"));
        System.out.println(manager.getTasks());
        Task homeworkTask = new Task("do homework", "do math", Status.NEW);
        Task carbonaraTask = new Task("go to the grocery", "buy spaghetti and bacon", Status.NEW);
        manager.addTask(homeworkTask);
        manager.addTask(carbonaraTask);
//
        System.out.println(manager.getEpics());
        manager.loadEpicsFromFile(Path.of("epics.json"));
        System.out.println(manager.getEpics());

        Epic docEpic = new Epic("take care about health", "visit doctors", Status.NEW);
        Epic workEpic = new Epic("find clients", "", Status.NEW);
        manager.addEpic(docEpic);
        manager.addEpic(workEpic);
        Subtask docTask1 = new Subtask("go to surgeon", "get a surgeon consultation", Status.NEW, docEpic);
        Subtask docTask2 = new Subtask("go to dermatologist", "get a dermatologist consultation", Status.NEW, docEpic);
        Subtask workTask1 = new Subtask("find clients", "use profi.ru", Status.NEW, workEpic);
        manager.addSubtask(docTask1);
        manager.addSubtask(docTask2);
        manager.addSubtask(workTask1);
        System.out.println(manager.getSubtasks());
        manager.loadSubtasksFromFile(Path.of("subtasks.json"));
        System.out.println(manager.getSubtasks());
//
//
//        System.out.println("History testing:");
//        System.out.println(manager.getTasks());
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getSubtasks());
//
        System.out.println(manager.getEpicById(4));
        System.out.println(manager.getHistory());
        manager.loadHistoryFromFile(Path.of("history.json"));
        System.out.println(manager.getHistory());

        for (int i = 0; i < 4; i++) {
            System.out.println(manager.getTaskById(1));
        }
//
        System.out.println(manager.getSubtaskById(5));
        Subtask updatedDocTask1 = new Subtask("go to surgeon", "get a surgeon consultation", Status.IN_PROGRESS, docEpic, docTask1.getId());
        manager.updateSubtask(updatedDocTask1);
        System.out.println(manager.getSubtaskById(5));
//
//
//        System.out.println("history: ");
//        System.out.println(manager.getHistory());
//        System.out.println("number of tasks in history: " + manager.getHistory().size());
//        System.out.println();
//        System.out.println("----------------------------------");
//
//        System.out.println("Task methods testing:");
//        System.out.println(manager.getTasks());
//        Task updatedHomeworkTask = new Task("do homework", "do math", Status.IN_PROGRESS, homeworkTask.getId());
//        manager.updateTask(updatedHomeworkTask);
//        Task updatedCarbonaraTask = new Task("go to the grocery", "buy spaghetti and bacon", Status.DONE, carbonaraTask.getId());
//        manager.updateTask(updatedCarbonaraTask);
//        System.out.println(manager.getTasks());
////        manager.removeTaskById(updatedCarbonaraTask.getId());
//        System.out.println(manager.getTasks());
//        System.out.println(manager.getTaskById(1));
////        manager.removeAllTasks();
//        System.out.println(manager.getTasks());
//        System.out.println("----------------------------------");
//
//
//        System.out.println("Epic methods testing:");
//        System.out.println(manager.getEpics());
//        updatedDocTask1 = new Subtask("go to surgeon", "get a surgeon consultation", Status.DONE, docEpic, docTask1.getId());
//        manager.updateSubtask(updatedDocTask1);
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getSubtasks());
//        System.out.println(manager.getSubtaskById(7));
//        Subtask updatedWorkTask1 = new Subtask("find clients", "use profi.ru", Status.DONE, workEpic, workTask1.getId());
//        manager.updateSubtask(updatedWorkTask1);
//        System.out.println(manager.getEpicById(4));
//        System.out.println("----------------------------------");
//
//
//        System.out.println("Subtask methods testing:");
//        Epic updatedWorkEpic = new Epic("find clients", "hahaha", Status.NEW, workEpic.getId());
//        manager.updateEpic(updatedWorkEpic);
//        System.out.println(manager.getEpicById(2));
//        manager.removeEpicById(4);
//        System.out.println(manager.getSubtasks());
//        manager.removeSubtaskById(docTask1.getId());
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getSubtasks());
//        System.out.println(manager.getSubtasksOfEpic(docEpic));
//        manager.removeAllSubtasks();
//        manager.removeAllEpics();
//        manager.removeAllTasks();
//        System.out.println(manager.getTasks());
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getSubtasks());
    }

}