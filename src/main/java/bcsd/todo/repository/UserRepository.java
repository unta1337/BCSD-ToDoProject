package bcsd.todo.repository;

import bcsd.todo.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {
    List<User> getUserByIdUniq(@Param("id_uniq") Integer idUniq);
    User getUserById(@Param("id") String id);
    User getValidUserById(@Param("id") String id);
}
