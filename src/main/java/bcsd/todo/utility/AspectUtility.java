package bcsd.todo.utility;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;

import java.util.Map;
import java.util.TreeMap;

/**
 * AOP 메소드에서 사용되는 유틸리티 클래스.
 */
public class AspectUtility {
    /**
     * Before 또는 After 어노테이션의 JoinPoint로부터 인수 추출.
     *
     * @param joinPoint AOP 메소드 정보
     * @return AOP 메소드의 JoinPoint의 인수 목록
     */
    public static Map<String, Object> getArgumentMap(JoinPoint joinPoint) {
        Map<String, Object> args = new TreeMap<String, Object>();

        String[] keys = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        Object[] values = joinPoint.getArgs();

        for (int i = 0; i < keys.length; i++) {
            args.put(keys[i], values[i]);
        }

        return args;
    }
}
