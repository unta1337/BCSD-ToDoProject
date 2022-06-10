package bcsd.todo.domain;

public class Task {
    private Integer idUniq;
    private String task;
    private Integer authorIdUniq;
    private Boolean isDone;
    private Boolean isValid;

    public Integer getIdUniq() {
        return idUniq;
    }

    public void setIdUniq(Integer idUniq) {
        this.idUniq = idUniq;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Integer getAuthorIdUniq() {
        return authorIdUniq;
    }

    public void setAuthorIdUniq(Integer authorIdUniq) {
        this.authorIdUniq = authorIdUniq;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
