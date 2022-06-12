package bcsd.todo.repository;

import bcsd.todo.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 사용자 리포지토리 클래스.
 */
@Repository
public interface UserRepository {
    /**
     * 사용자로부터 아이디와 비밀번호를 입력 받아 새로운 사용자 계정 생성.
     *
     * @param id 사용자 아이디
     * @param password 사용자 비밀번호
     * @return 사용자 계정 생성 여부
     */
    Integer createUser(@Param("id") String id, @Param("password") String password);

    /**
     * 사용자의 고유 번호를 이용해 사용자 정보 불러오기.
     *
     * @param idUniq 사용자 고유 번호
     * @return 사용자 정보 또는 null
     */
    User getUserByIdUniq(@Param("id_uniq") Integer idUniq);

    /**
     * 사용자의 아이디를 이용해 사용자 정보 불러오기.
     *
     * @param id 사용자 아이디
     * @return 사용자 정보 리스트 또는 빈 리스트
     */
    List<User> getUserById(@Param("id") String id);

    /**
     * 사용자 계정 삭제하기. (Soft Delete)
     *
     * @param idUniq 사용자 고유 번호
     */
    Integer deleteUserByIdUniq(@Param("id_uniq") Integer idUniq);

    /**
     * 사용자 계정 삭제하기. (Hard Delete)
     *
     * @param idUniq 사용자 고유 번호
     */
    Integer deleteUserByIdUniqHard(@Param("id_uniq") Integer idUniq);

    /**
     * 사용자 계정 복구하기.
     * 사용자 계정 삭제가 Soft Delete로 수행됐을 때만 가능하다.
     *
     * @param idUniq 사용자 고유 번호
     */
    Integer restoreUserByIdUniq(@Param("id_uniq") Integer idUniq);

    /**
     * 사용자 비밀번호 변경하기.
     *
     * @param idUniq 사용자 고유 번호
     * @param password 사용자 비밀번호
     * @return 비밀번호 변경 여부
     */
    Integer updatePasswordByIdUniq(@Param("id_uniq") Integer idUniq, @Param("password") String password);
}
