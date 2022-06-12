package bcsd.todo.utility;

public class LogUtility {
    public static void log(String text) {
        System.out.println(text);
    }

    public static void logWithTime(String text) {
        System.out.println(TimeKeeper.getCurrentTime() + " " + text);
    }
}
