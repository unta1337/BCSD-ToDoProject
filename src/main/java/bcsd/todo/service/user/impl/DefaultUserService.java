package bcsd.todo.service.user.impl;

import bcsd.todo.domain.User;
import bcsd.todo.repository.UserRepository;
import bcsd.todo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getUserByIdUniq(Integer idUniq) {
        return userRepository.getUserByIdUniq(idUniq);
    }

    @Override
    public Boolean createUser(String id, String password) {
        if (id == null || password == null) {
            return false;
        }

        return true;
    }

    public Boolean isExist(String id) {
        User user = userRepository.getValidUserById(id);

        return user != null;
    }
}
