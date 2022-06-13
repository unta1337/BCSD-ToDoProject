package bcsd.todo.service.user;

import bcsd.todo.domain.User;

import java.util.List;

/**
 * 사용자 서비스 인터페이스.
 * 기본적인 CRUD 메소드 지원.
 */
public interface UserService {
    /**
     * 사용자로부터 아이디와 비밀번호를 입력 받아 새로운 사용자 계정 생성.
     * 사용자 아이디 중복 검사가 필요하다.
     * 사용자 비밀번호는 BCrypt를 이용해 암호화한다.
     *
     * @param id 사용자 아이디
     * @param password 사용자 비밀번호
     * @return 사용자 계정 생성 여부
     */
    Boolean createUser(String id, String password);

    /**
     * 사용자의 고유 번호를 이용해 사용자 정보 불러오기.
     * 사용자의 고유 번호를 이용하므로 반환 자료형으로 단일 User를 사용한다.
     *
     * @param idUniq 사용자 고유 번호
     * @return 사용자 정보 또는 null
     */
    User getUserByIdUniq(Integer idUniq);

    /**
     * 사용자의 아이디를 이용해 사용자 정보 불러오기.
     * 사용자의 아이디는 중복이 가능하므로 리스트를 이용해 결과를 반환한다.
     *
     * @param id 사용자 아이디
     * @return 사용자 정보 리스트 또는 빈 리스트
     */
    List<User> getUsersById(String id);

    /**
     * 사용자 비밀번호 변경하기.
     *
     * @param idUniq 사용자 고유 번호
     * @param password 사용자 비밀번호
     * @return 비밀번호 변경 여부
     */
    Boolean updatePasswordByIdUniq(Integer idUniq, String password);

    /**
     * 사용자 계정 삭제하기.
     *
     * @param idUniq 사용자 고유 번호
     * @param hard Hard Delete 여부
     * @return 사용자 계정 삭제 여부
     */
    Boolean deleteUserByIdUniq(Integer idUniq, Boolean hard);

    /**
     * 사용자 계정 복구하기.
     *
     * @param idUniq 사용자 고유 번호
     * @return 사용자 계정 복구 여부
     */
    Boolean restoreUserByIdUniq(Integer idUniq);
}
