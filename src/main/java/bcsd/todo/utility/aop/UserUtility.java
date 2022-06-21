package bcsd.todo.utility.aop;

import bcsd.todo.domain.User;
import bcsd.todo.service.user.impl.DefaultUserService;
import bcsd.todo.utility.AspectUtility;
import bcsd.todo.utility.TokenUtility;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Map;

/**
 * 사용자 서비스 제공 시 인증 및 인가를 위한 AOP 컴포넌트 클래스.
 */
@Component
@Aspect
public class UserUtility {
    /**
     * 사용자 서비스 객체.
     */
    @Autowired
    private DefaultUserService userService;
    /**
     * 현재 접속 중인 세션.
     */
    @Autowired
    private HttpSession session;
    /**
     * 현제 요청된 HTTP Request.
     */
    @Autowired
    private HttpServletRequest request;

    /**
     * 사용자 인증.
     *
     * @param proceedingJoinPoint 사용자 서비스 메소드 정보
     */
    @Around("@annotation(bcsd.todo.annotation.Authenticate)")
    public Object authenticateUser(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Map<String, Object> args = AspectUtility.getArgumentMap(proceedingJoinPoint);

        // 로그인을 시도하는 사용자 조회.
        User user = (User) args.get("user");

        // 존재하지 않는 사용자면 false.
        if (!userService.isValidUser(user.getId()))
            return false;
        User requestUser = userService.getUsersById(user.getId()).stream().filter(User::getValid).findFirst().get();

        // 사용자 비밀번호가 일치하지 않으면 false.
        if (!BCrypt.checkpw(user.getPassword(), requestUser.getPassword())) {
            return false;
        }

        // 현재 세션에 사용자 정보 등록.
        session.setAttribute("sessionUser", requestUser);

        // 정상 인증.
        return proceedingJoinPoint.proceed();
    }

    /**
     * 사용자 인가.
     *
     * @param proceedingJoinPoint 사용자 서비스 메소드 정보
     */
    @Around("@annotation(bcsd.todo.annotation.Authorize)")
    public Object authorizeUser(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 현재 세션에 등록된 사용자 조회.
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 세션에 로그인한 사용자가 없으면 false.
        if (sessionUser == null) {
            return false;
        }

        // 위의 구문을 거쳤으면 사용자가 로그인한 상태임을 보장할 수 있으므로 별도의 예외처리가 필요하지 않음.
        Cookie[] cookies = request.getCookies();
        String token = Arrays.stream(cookies).filter(c -> c.getName().equals("token")).findFirst().get().getValue();

        // 토큰이 유효하지 않으면 false.
        if (!TokenUtility.verifyToken(token)) {
            return false;
        }

        // 정상 인가.
        return proceedingJoinPoint.proceed();
    }
}
