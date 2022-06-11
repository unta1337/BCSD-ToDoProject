package bcsd.todo.util.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 사용자 서비스 제공 시 인증 및 인가를 위한 AOP 컴포넌트 클래스.
 */
@Aspect
@Component
public class AuthUtil {
    /**
     * 사용자 비밀번호 유효성 검사.
     *
     * @param joinPoint 사용자 서비스 메소드 정보
     */
    @Before("@annotation(bcsd.todo.annotation.AuthPassword)")
    public void authPassword(JoinPoint joinPoint) {
    }
}
