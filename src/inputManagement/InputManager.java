package inputManagement;

public class InputManager {
    private static Thread thread;
    private static float value;
    private static boolean accepted;
    private static InputState inputState;

    public static InputState getInputState() {
        return inputState;
    }

    public static void setInputState(InputState inputState) {
        InputManager.inputState = inputState;
    }

    public static void setAccepted(boolean accepted) {
        InputManager.accepted = accepted;
    }

    public static boolean isAccepted() {
        return accepted;
    }

    public static void setValue(float value) {
        InputManager.value = value;
    }

    public static float getValue() {
        return value;
    }

    public static void setThread(Thread thread) {
        InputManager.thread = thread;
    }

    public static Thread getThread() {
        return thread;
    }
}
