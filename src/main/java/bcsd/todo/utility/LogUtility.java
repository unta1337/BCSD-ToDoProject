package bcsd.todo.utility;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 콘솔 및 브라우저 관련 로그 유틸리티 클래스.
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

    /**
     * 브라우저에서 alert 출력.
     *
     * @param text alert에서 출력할 문자열
     * @param response 브라우저 응답
     * @throws IOException 브라우저에서 발생할 수 있는 IOException
     */
    public static void browserAlert(String text, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("<script>alert('" + text + "'); history.go(-1)</script>");
        response.getWriter().flush();
    }
}
