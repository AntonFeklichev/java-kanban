package tasks;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String desc;
    private int id;
    private Status status;
    private TaskTypes type;
    private ZonedDateTime startTime;
    private long duration;
    private ZonedDateTime endTime;

    public Task(ZonedDateTime startTime, long duration) {
        this.startTime = startTime;
        this.duration = duration;
        endTime = startTime.plusMinutes(duration);
    }

    public Task() {}

    public Task(String name, String desc, Status status) {
        this.name = name;
        this.desc = desc;
        this.status = status;
        type = TaskTypes.TASK;
    }

    public Task(String name, String desc, Status status, TaskTypes type) {
        this.name = name;
        this.desc = desc;
        this.status = status;
        this.type = type;
    }

    public Task(String name, String desc, Status status, int id) {
        this.name = name;
        this.desc = desc;
        this.status = status;
        this.id = id;
        type = TaskTypes.TASK;
    }

    public Task(int id) {
        setId(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "name=" + '\'' + name + '\'' + ", desc=" + '\'' + desc + '\'' + ", id=" + id + ", status=" + status + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, desc);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() == obj.getClass()) {
            Task otherTask = (Task) obj;
            return id == otherTask.id;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskTypes getType() {
        return type;
    }

    public void setType(TaskTypes type) {
        this.type = type;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }
}