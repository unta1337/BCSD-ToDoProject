package bcsd.todo.utility;

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
public class MethodLogUtility {
    /**
     * 컨트롤러 클래스 포인트 컷.
     */
    @Pointcut("execution(* bcsd.todo.controller..*(..))")
    public void controller() { }

    /**
     * 리포지토리 클래스 포인트 컷.
     */
    @Pointcut("execution(* bcsd.todo.repository..*(..))")
    public void repository() { }

    /**
     * 서비스 클래스 포인트 컷.
     */
    @Pointcut("execution(* bcsd.todo.service..*(..))")
    public void service() { }

    /**
     * MVC 클래스 포인트 컷.
     */
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
        LogUtility.logWithTime("Executing " + proceedingJoinPoint.getSignature().getName() + "...");

        long start = System.currentTimeMillis();
        Object obj = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();

        long duration = end - start;
        LogUtility.logWithTime("Execution Time: " + duration + " milliseconds.");

        return obj;
    }
}
