package bcsd.todo.domain;

public class User {
    private Integer idUniq;
    private String id;
    private String password;
    private Boolean isValid;

    public Integer getIdUniq() {
        return idUniq;
    }

    public void setIdUniq(Integer idUniq) {
        this.idUniq = idUniq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
