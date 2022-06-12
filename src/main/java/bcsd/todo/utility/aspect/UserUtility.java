package bcsd.todo.utility.aspect;

import bcsd.todo.domain.User;
import bcsd.todo.enumerator.AuthenticationResult;
import bcsd.todo.enumerator.AuthorizationResult;
import bcsd.todo.service.user.impl.DefaultUserService;
import bcsd.todo.utility.AspectUtility;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 사용자 서비스 제공 시 인증 및 인가를 위한 AOP 컴포넌트 클래스.
 */
@Aspect
@Component
public class UserUtility {
    @Autowired
    private DefaultUserService userService;
    @Autowired
    private HttpSession session;

    /**
     * 사용자 인증.
     *
     * @param joinPoint 사용자 서비스 메소드 정보
     */
    @Before("@annotation(bcsd.todo.annotation.Authenticate)")
    public void authenticateUser(JoinPoint joinPoint) {
        Map<String, Object> args = AspectUtility.getArgumentMap(joinPoint);

        // 로그인을 시도하려는 사용자 조회.
        String id = (String) args.get("id");
        List<User> targetUsers = userService.getUserById(id);
        User body = (User) args.get("body");

        // 사용자가 존재하지 않으면 관련 페이지 연결.
        if (targetUsers.size() == 0) {
            session.setAttribute("authenticationResult", AuthenticationResult.NO_SUCH_USER);
            return;
        }

        // 사용자가 존재하지 않으면 관련 페이지 연결.
        User targetUser = null;
        for (User tu : targetUsers) {
            if (tu.getValid()) {
                targetUser = tu;
                break;
            }
        }
        if (targetUser == null) {
            session.setAttribute("authenticationResult", AuthenticationResult.NO_SUCH_USER);
            return;
        }

        // 사용자 비밀번호가 일치하지 않으면 관련 페이지 연결.
        if (!BCrypt.checkpw(body.getPassword(), targetUser.getPassword())) {
            session.setAttribute("authenticationResult", AuthenticationResult.INCORRECT_PASSWORD);
            return;
        }

        // 현재 세션에 사용자 정보 등록.
        session.setAttribute("sessionUser", targetUser);

        // 정상 인증.
        session.setAttribute("authenticationResult", AuthenticationResult.AUTHENTICATED);
    }

    /**
     * 사용자 인가.
     *
     * @param joinPoint 사용자 서비스 메소드 정보
     */
    @Before("@annotation(bcsd.todo.annotation.Authorize)")
    public void authorizeUser(JoinPoint joinPoint) {
        Map<String, Object> args = AspectUtility.getArgumentMap(joinPoint);

        // 현재 세션에 등록된 사용자 조회.
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 조회를 시도하려는 사용자 조회.
        String id = (String) args.get("id");
        List<User> targetUsers = userService.getUserById(id);

        // 사용자가 존재하지 않으면 관련 페이지 연결.
        if (targetUsers.size() == 0) {
            session.setAttribute("authorizationResult", AuthorizationResult.NO_SUCH_USER);
            return;
        }

        // 사용자가 존재하지 않으면 관련 페이지 연결.
        User targetUser = null;
        for (User tu : targetUsers) {
            if (tu.getValid()) {
                targetUser = tu;
                break;
            }
        }
        if (targetUser == null) {
            session.setAttribute("authorizationResult", AuthorizationResult.NO_SUCH_USER);
            return;
        }

        // 사용자 아이디가 일치하지 않으면 관련 페이지 연결.
        // TODO: JWT를 이용한 인가로 변경.
        if (!id.equals(sessionUser.getId())) {
            session.setAttribute("authorizationResult", AuthorizationResult.INCORRECT_ID);
            return;
        }

        // 정상 인가.
        session.setAttribute("authorizationResult", AuthorizationResult.AUTHORIZED);
    }
}
