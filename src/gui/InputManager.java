package gui;

public class InputManager {
    private static Thread thread;
    private static float value;
    private static InputState inputState;

    public static InputState getInputState() {
        return inputState;
    }

    public static void setInputState(InputState inputState) {
        InputManager.inputState = inputState;
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
