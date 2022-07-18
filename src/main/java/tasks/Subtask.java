package tasks;

import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Objects;

@NoArgsConstructor
public class Subtask extends Task {

    private int epicId;


    public Subtask(ZonedDateTime startTime, long duration) {
        super(startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String desc, Status status, Epic epic) {
        super(name, desc, status, TaskTypes.SUBTASK);
        this.epicId = epic.getId();
    }

    public Subtask(String name, String desc, Status status, Epic epic, int id) {
        super(name, desc, status, id);
        setType(TaskTypes.SUBTASK);
        this.epicId = epic.getId();
    }

    public Subtask(String name, String desc, Status status, int epicId, int id) {
        super(name, desc, status, id);
        setType(TaskTypes.SUBTASK);
        this.epicId = epicId;
    }

    public Subtask(Status status, Epic epic) {
        setStatus(status);
        setEpicId(epic.getId());
    }


    public Subtask(int id) {
        setId(id);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDesc(), epicId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() == obj.getClass()) {
            Subtask otherSubtask = (Subtask) obj;
            return super.equals(obj) && epicId == otherSubtask.epicId;
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "name=" + '\'' + getName() + '\'' + ", desc=" + '\'' + getDesc() + '\'' + ", id=" + getId() + ", status=" + getStatus() + ", epicId=" + getEpicId() + "}";
    }
}