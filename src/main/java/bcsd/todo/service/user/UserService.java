package bcsd.todo.service.user;

import bcsd.todo.domain.User;

import java.util.List;

public interface UserService {
    List<User> getUserByIdUniq(Integer idUniq);
    Boolean createUser(String id, String password);
}
