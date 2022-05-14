package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String desc, Status status) {
        super(name, desc, status);
    }
    public Epic(String name, String desc, Status status, int id) {
        super(name, desc, status, id);
    }


    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "name=" + '\'' + getName() + '\'' + ", desc=" + '\'' + super.getDesc() + '\'' + ", id=" + getId() + ", status=" + getStatus() + ", subtasks=" + subtasks + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() == obj.getClass()) {
            Epic otherEpic = (Epic) obj;
            return super.equals(obj) && subtasks.equals(otherEpic.getSubtasks());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDesc(), getSubtasks());
    }
}
