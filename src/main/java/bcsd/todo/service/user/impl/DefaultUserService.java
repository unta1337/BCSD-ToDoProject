package bcsd.todo.service.user.impl;

import bcsd.todo.annotation.Authenticate;
import bcsd.todo.annotation.Authorize;
import bcsd.todo.domain.User;
import bcsd.todo.repository.UserRepository;
import bcsd.todo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
     * 현재 접속 중인 세션.
     */
    @Autowired
    private HttpSession session;

    /**
     * 유효한 사용자 아이디의 정규 표현식.
     * 20자 이내여야 함.
     * 영문 대소문자와 숫자 및 '_'로 구성돼야 함.
     * 첫 글자는 영문 대소문자여야 함.
     */
    private final String validIdPattern = "^[A-Za-z]\\w{0,19}$";
    /**
     * 유효한 사용자 비밀번호 정규 표현식.
     * 기존의 비밀번호와 달라야 함. (이는 정규식에서 검사하지 않음)
     * 8자리 이상이어야 함.
     */
    private final String validPasswordPattern = "^.{8,}$";

    /**
     * 로그인.
     *
     * @param user 로그인을 시도하려는 사용자 정보
     * @return 로그인 성공 여부
     */
    @Authenticate
    public Boolean signIn(User user) {
        return true;
    }

    /**
     * 로그아웃.
     *
     * @return 로그아웃 성공 여부
     */
    public Boolean signOut() {
        session.invalidate();

        return true;
    }

    /**
     * 사용자 정보 조회.
     * 비밀번호 등 민감한 정보를 제외한 사용자 정보를 반환한다.
     *
     * @param id 조회할 사용자의 아이디
     * @return 사용자 정보 또는 null
     */
    public User getUserInfo(String id) {
        User user = getUserById(id);

        if (user == null) {
            return null;
        }

        user.setIdUniq(null);
        user.setPassword(null);

        return user;
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
        if (!isValidId(id) || !isValidPassword(password)) {
            return false;
        }

        User oldUser = getUserById(id);

        // 동일한 아이디를 가진 기존 사용자가 있으면 계정 생성 실패.
        if (oldUser != null) {
            return false;
        }

        // 비밀번호 암호화.
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
        // 사용자 목록 조회.
        List<User> users = getUsersById(id);

        if (users.size() == 0) {
            return null;
        }

        Optional<User> userOptional = users.stream().filter(User::getValid).findFirst();
        return userOptional.orElse(null);
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
        return hard ? userRepository.deleteUserByIdUniqHard(idUniq) > 0 : userRepository.deleteUserByIdUniq(idUniq) > 0;
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

    /**
     * 사용자 인증 Aspect의 메소드.
     *
     * @return 사용자 인증 여부
     */
    @Authenticate
    public Boolean getAuthenticate() {
        return true;
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
     * 사용자 유효성 검사.
     *
     * @param id 사용자 아이디
     * @return 사용자 유효성
     */
    public Boolean isValidUser(String id) {
        return getUserById(id) != null;
    }

    /**
     * 사용자 아이디 유효성 검사.
     *
     * @param id 사용자 아이디
     * @return 사용자 아이디 유효성 여부
     */
    public Boolean isValidId(String id) {
        // 정규식에 부합하는지 검사.
        return Pattern.matches(validIdPattern, id);
    }

    /**
     * 사용자 비밀번호 유효성 검사.
     *
     * @param password 사용자 비밀번호
     * @return 사용자 비밀번호 유효성 여부
     */
    public Boolean isValidPassword(String password) {
        boolean validity = true;

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser != null) {
            validity = BCrypt.checkpw(password, sessionUser.getPassword());
        }

        // 정규식에 부합하는지 검사.
        return validity && Pattern.matches(validPasswordPattern, password);
    }
}
