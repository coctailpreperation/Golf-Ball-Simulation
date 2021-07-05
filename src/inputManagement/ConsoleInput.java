package inputManagement;

import java.util.Scanner;

public class ConsoleInput extends InputSource{
    final Thread inputThread;
    Reader reader;
    public ConsoleInput(){
        reader = new Reader();
        inputThread = new Thread(this, "console");
        InputManager.setThread(inputThread);
        inputThread.setDaemon(true);
        inputThread.start();
    }
    Scanner scanner = new Scanner(System.in);
    @Override
    public void run() {
        while (true) {
            reader.read();
        }
    }
    private class Reader{
        public void read(){
            try {
                synchronized (inputThread) {
                    inputThread.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(InputManager.getInputState() == InputState.Mass) {
                InputManager.setValue(scanner.nextFloat());
                InputManager.setInputState(InputState.initialized);
            }
        }
    }
}
