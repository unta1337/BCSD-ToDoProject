package bcsd.todo.service.user.impl;

import bcsd.todo.annotation.Authorize;
import bcsd.todo.domain.User;
import bcsd.todo.repository.UserRepository;
import bcsd.todo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 기본 사용자 서비스 클래스.
 */
@Service
public class DefaultUserService implements UserService {
    /**
     * 사용자 리포지토리 객체.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자 유효성 검사.
     *
     * @param id 사용자 아이디
     * @return 사용자 유효성
     */
    public Boolean isValidUser(String id) {
        return getUserById(id) != null;
    }

    /**
     * 사용자 인가 Aspect의 메소드.
     *
     * @return 사용자 인가 여부
     */
    @Authorize
    public Boolean getAuthorize() {
        return true;
    }

    /**
     * 사용자로부터 아이디와 비밀번호를 입력 받아 새로운 사용자 계정 생성.
     *
     * @param id 사용자 아이디
     * @param password 사용자 비밀번호
     * @return 사용자 계정 생성 여부
     */
    @Override
    public Boolean createUser(String id, String password) {
        List<User> oldUser = getUsersById(id);

        // 동일한 아이디를 가진 기존 사용자가 있으면 계정 생성 실패.
        if (oldUser != null) {
            return false;
        }

        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Integer userCreationTry;

        // 사용자 생성 시도.
        try {
            userCreationTry = userRepository.createUser(id, encryptedPassword);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        return userCreationTry > 0;
    }

    /**
     * 사용자의 고유 번호를 이용해 사용자 정보 조회.
     *
     * @param idUniq 사용자 고유 번호
     * @return 사용자 정보 또는 null
     */
    @Override
    public User getUserByIdUniq(Integer idUniq) {
        return userRepository.getUserByIdUniq(idUniq);
    }

    /**
     * 사용자의 아이디를 이용해 사용자 정보 조회.
     *
     * @param id 사용자 아이디
     * @return 사용자 정보 리스트 또는 빈 리스트
     */
    @Override
    public List<User> getUsersById(String id) {
        return userRepository.getUserById(id);
    }

    /**
     * 사용자의 아이디를 이용해 한 명의 사용자 정보 조회.
     *
     * @param id 사용자 아이디
     * @return 사용자 정보 또는 null
     */
    public User getUserById(String id) {
        List<User> users = getUsersById(id);

        if (users.size() == 0) {
            return null;
        }

        Optional<User> optionalUser = users.stream().filter(User::getValid).findFirst();
        if (optionalUser.isEmpty())
            return null;
        return optionalUser.get();
    }

    /**
     * 사용자 비밀번호 변경.
     *
     * @param idUniq 사용자 고유 번호
     * @param password 사용자 비밀번호
     * @return 비밀번호 변경 여부
     */
    @Override
    @Authorize
    public Boolean updatePasswordByIdUniq(Integer idUniq, String password) {
        User target = getUserByIdUniq(idUniq);

        if (target == null)
            return false;

        String hashedOldPassword = target.getPassword();
        if (BCrypt.checkpw(password, hashedOldPassword)) {
            return false;
        }

        String encrypted = BCrypt.hashpw(password, BCrypt.gensalt());

        return userRepository.updatePasswordByIdUniq(idUniq, encrypted) > 0;
    }

    /**
     * 사용자 계정 삭제.
     *
     * @param idUniq 사용자 고유 번호
     * @param hard Hard Delete 여부
     * @return 사용자 계정 삭제 여부
     */
    @Override
    @Authorize
    public Boolean deleteUserByIdUniq(Integer idUniq, Boolean hard) {
        if (hard)
            return userRepository.deleteUserByIdUniqHard(idUniq) > 0;
        return userRepository.deleteUserByIdUniq(idUniq) > 0;
    }

    /**
     * 사용자 계정 복구.
     *
     * @param idUniq 사용자 고유 번호
     * @return 사용자 계정 복구 여부
     */
    @Override
    @Authorize
    public Boolean restoreUserByIdUniq(Integer idUniq) {
        User targetUser = userRepository.getUserByIdUniq(idUniq);

        if (targetUser == null)
            return false;
        if (targetUser.getValid())
            return true;

        return userRepository.restoreUserByIdUniq(idUniq) > 0;
    }
}
