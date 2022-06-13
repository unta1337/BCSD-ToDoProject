package bcsd.todo.utility;

/**
 * 콘솔 로그 유틸리티 클래스.
 */
public class LogUtility {
    /**
     * 콘솔에 로그 출력.
     *
     * @param text 로그에 출력할 문자열.
     */
    public static void log(String text) {
        System.out.println(text);
    }

    /**
     * 콘솔에 현재 시각과 함께 로그 출력.
     *
     * @param text 로그에 출력할 문자열.
     */
    public static void logWithTime(String text) {
        System.out.println(TimeKeeper.getCurrentTime() + " " + text);
    }
}
