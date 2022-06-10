package bcsd.todo.util;

import java.time.LocalDateTime;

/**
 * 시간 관련 유틸리티 클래스.
 */
public class TimeKeeper {
    /**
     * 현재 시각 불러오기.
     *
     * @return 현재 시각
     */
    public static String getCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();

        return "[" + currentTime + ']';
    }
}
