package bcsd.todo.service.user.impl;

import bcsd.todo.domain.User;
import bcsd.todo.repository.UserRepository;
import bcsd.todo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 기본 사용자 서비스 클래스.
 */
@Service
public class DefaultUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자로부터 아이디와 비밀번호를 입력 받아 새로운 사용자 계정 생성.
     *
     * @param id 사용자 아이디
     * @param password 사용자 비밀번호
     * @return 사용자 계정 생성 여부
     */
    @Override
    public Boolean createUser(String id, String password) {
        List<User> oldUsers = getUserById(id);

        // 기존의 동일한 아이디를 가진 사용자가 존재하면 해당 사용자가 유효한지 검사.
        // 동일한 아이디를 가진 유효한 사용자가 있으면 계정 생성 실패.
        if (oldUsers.size() > 0) {
            for (User oldUser : oldUsers) {
                if (oldUser.getValid())
                    return false;
            }
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
     * 사용자의 고유 번호를 이용해 사용자 정보 불러오기.
     *
     * @param idUniq 사용자 고유 번호
     * @return 사용자 정보 또는 null
     */
    @Override
    public User getUserByIdUniq(Integer idUniq) {
        return userRepository.getUserByIdUniq(idUniq);
    }

    /**
     * 사용자의 아이디를 이용해 사용자 정보 불러오기.
     *
     * @param id 사용자 아이디
     * @return 사용자 정보 리스트 또는 빈 리스트
     */
    @Override
    public List<User> getUserById(String id) {
        return userRepository.getUserById(id);
    }

    /**
     * 사용자 비밀번호 변경하기.
     * TODO: JWT 사용자 인증 및 인가 추가.
     *
     * @param idUniq 사용자 고유 번호
     * @param password 사용자 비밀번호
     * @return 비밀번호 변경 여부
     */
    @Override
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
     * 사용자 계정 삭제하기.
     * TODO: JWT 사용자 인증 및 인가 추가.
     *
     * @param idUniq 사용자 고유 번호
     * @param hard Hard Delete 여부
     * @return 사용자 계정 삭제 여부
     */
    @Override
    public Boolean deleteUserByIdUniq(Integer idUniq, Boolean hard) {
        if (hard)
            return userRepository.deleteUserByIdUniqHard(idUniq) > 0;
        return userRepository.deleteUserByIdUniq(idUniq) > 0;
    }

    /**
     * 사용자 계정 복구하기.
     * TODO: JWT 사용자 인증 및 인가 추가.
     *
     * @param idUniq 사용자 고유 번호
     * @return 사용자 계정 복구 여부
     */
    @Override
    public Boolean restoreUserByIdUniq(Integer idUniq) {
        User targetUser = userRepository.getUserByIdUniq(idUniq);

        if (targetUser == null)
            return false;
        if (targetUser.getValid())
            return true;

        return userRepository.restoreUserByIdUniq(idUniq) > 0;
    }
}
