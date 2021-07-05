package inputManagement;

import javax.swing.*;
import java.util.Scanner;

public class GUI extends InputSource{

    final Thread inputThread;
    Reader reader;
    public GUI(){
        reader = new Reader();
        inputThread = new Thread(this, "gui");
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

            try {
                String x = JOptionPane.showInputDialog("Enter Value");
                float value = Float.parseFloat(x);
                InputManager.setValue(value);
            } catch (Exception ignored){

            }
            switch (InputManager.getInputState()){
                case MassPending:
                    InputManager.setInputState(InputState.Mass);
                    break;
                case GravityPending:
                    InputManager.setInputState(InputState.Gravity);
                    break;
                case ForcePending:
                    InputManager.setInputState(InputState.Force);
                    break;
                case AnglePending:
                    InputManager.setInputState(InputState.Angle);
                    break;

            }
        }
    }
}
