package bcsd.todo.util.aop;

import bcsd.todo.util.LogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 메소드 로깅을 위한 AOP 컴포넌트 클래스.
 */
@Component
@Aspect
public class MethodLogUtil {
    @Pointcut("execution(* bcsd.todo.controller..*(..))")
    public void controller() { }

    @Pointcut("execution(* bcsd.todo.repository..*(..))")
    public void repository() { }

    @Pointcut("execution(* bcsd.todo.service..*(..))")
    public void service() { }

    @Pointcut("controller() || repository() || service()")
    public void all() { }

    /**
     * 현재 실행된 메소드의 실행 시각과 실행 시간 출력.
     *
     * @param proceedingJoinPoint 실행된 메소드 정보
     * @return 실행된 메소드 정보
     * @throws Throwable AOP 실행 중 발생 가능한 예외
     */
    @Around("all()")
    public Object executionTimeCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        LogUtil.logWithTime("Executing " + proceedingJoinPoint.getSignature().getName() + "...");

        long start = System.currentTimeMillis();
        Object obj = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();

        long duration = end - start;
        LogUtil.logWithTime("Execution Time: " + duration + " milliseconds.");

        return obj;
    }
}
