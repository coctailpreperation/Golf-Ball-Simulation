package entities;

import Models.TexturedModel;
import engineTester.audio.Audio;
import engineTester.audio.State;
import gui.InputManager;
import gui.InputState;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Player extends Entity {

    private Vector3f basePosition = new Vector3f(0, 0, 0);

    private static final float TERRAIN_HEIGHT = 0;

    private boolean isInTheAir = false;

    public Player(TexturedModel model, Vector3f position, float scale, float rotX, float rotY, float rotZ) {
        super(model, position, scale, rotX, rotY, rotZ);
        basePosition.x = position.x;
        basePosition.y = position.y;
        basePosition.z = position.z;
    }

    Vector3f fTotal = new Vector3f(0, 0, 0); // Total Force
    Vector3f v = new Vector3f(0, 0, 0);
    float ballM = 0.04593f; // Ball Weight
    float ballRadius = 0.2f;
    //float clubM; // Club Weight
    float gravity = 10;
    Vector3f fShot = new Vector3f(0,0,0); // Newton
    Vector3f fGravity = new Vector3f(0, 0, 0);
    Vector3f a = new Vector3f(0,0,0);
    float dt = 0;
    float verticalAngle = 0;
    float horizontalAngle = 0;
    float currentTime,previousTime = 0;
    float p = 0.5f;
    Vector3f previousShot = new Vector3f(0,0,0);
    Vector3f FD = new Vector3f(0,0,0);
    float kFriction = 0.4f;
    Vector3f fFriction = new Vector3f(0,0,0);
    float dx,dy,dz;
    Vector3f w = new Vector3f(0,0,0);
    Vector3f u = new Vector3f(0,0,0);
    boolean finished = false;
    float fShotValue = 1000;
    boolean angleIsInversed = false;
    boolean treeInversed = false;
    int score = 0;
    float reboundAngle = 0;
    float cd = 0.47f;
    float A = 0.0004338f;
    float ro = 1.184f;


    public void move() {

        if(Keyboard.isKeyDown(Keyboard.KEY_R) && (currentTime-previousTime) > 1){
            reset();
        }

        if(Math.abs(getPosition().x % 400) <= 7 && Math.abs(getPosition().z % 400) <= 7 && Math.abs(getPosition().y) <= 12) {

            float v1x = ((ballM - 10f) / (ballM + 10f)) * v.x;

            float v1z = ((ballM - 10f) / (ballM + 10f)) * v.z;
            v.x = Math.abs(v1x);
            v.z = Math.abs(v1z);

            System.out.println(verticalAngle);
            if(v.x - v1x > 0 && !treeInversed) {
                reboundAngle = 180;
                treeInversed = true;//
            }
            System.out.println(verticalAngle);




        }


            if(Math.abs(getPosition().x % 500) <= 10 && Math.abs(getPosition().z % 500) <= 10 && getPosition().y == basePosition.y){
            getPosition().y = -1f;
            finished = true;
            score++;
                System.out.println(score);
        }

        else if(!finished){

            if (v.x == 0 && v.y == 0 && v.z == 0) {
                horizontalAngle = Camera.angleAroundPlayer - 90;
                if(treeInversed){
                    treeInversed = false;//
                    reboundAngle = 0;
                }
            }

//                System.out.println(v.x);
//                System.out.println(v.y);
//                System.out.println(v.z);

            checkInputs();

            if (isInTheAir) {
                fGravity.y = gravity;
            }

            dt = 0.04f;

            currentTime = Sys.getTime() * 1.0f / Sys.getTimerResolution(); // 1000

            FD.x = (float) ((0.5 * cd * A * ro * Math.pow(v.x, 2)));
            FD.y = (float) ((0.5 * cd * A * ro * Math.pow(v.y, 2)));
            FD.z = (float) ((0.5 * cd * A * ro * Math.pow(v.z, 2)));


            if (!isInTheAir) {
                fFriction.z = u.z * ballM * gravity * kFriction;

                if (v.z < 0) {
                    v.z = 0;
                    fFriction.z = 0;
                }
                if (v.z == 0)
                    fFriction.z = 0;

            } else fFriction.z = 0;


            if (!isInTheAir) {

                fFriction.x = u.x * ballM * gravity * kFriction;

                if (v.x < 0) {
                    v.x = 0;
                    fFriction.x = 0;
                }
                if (v.x == 0)
                    fFriction.x = 0;


            } else fFriction.x = 0;

            if (v.x < 0)
                u.x = -1;
            else u.x = 1;

            if (v.z < 0)
                u.z = -1;
            else u.z = 1;


            fTotal.x = fShot.x - FD.x - fFriction.x;
            fTotal.y = fShot.y - ballM * fGravity.y - FD.y;
            fTotal.z = fShot.z - FD.z - fFriction.z;

            fShot.x = 0;
            fShot.y = 0;
            fShot.z = 0;

            a.x = fTotal.x / ballM;
            a.y = fTotal.y / ballM;
            a.z = fTotal.z / ballM;



            v.x += a.x * dt;
            v.y += a.y * dt;
            v.z += a.z * dt;

            if (v.x == 0 && v.z == 0 && (verticalAngle % 180) == 0)
                v.y = 0;




            dx = (float) (v.x * dt * Math.cos(Math.toRadians(verticalAngle + reboundAngle)) * Math.sin(Math.toRadians(horizontalAngle)));
            dy = (float) (v.y * dt * Math.sin(Math.toRadians(verticalAngle)));
            dz = (float) (v.z * dt * Math.cos(Math.toRadians(verticalAngle + reboundAngle)) * Math.cos(Math.toRadians(horizontalAngle)));


            w.x = dz / dt * ballRadius;
            w.z = dx / dt * ballRadius;

            super.increasePosition(dx, dy, dz);
            super.increaseRotation(w.x, 0, w.z);


            if (super.getPosition().y < TERRAIN_HEIGHT + basePosition.y) {

                if(verticalAngle < 0 && !angleIsInversed) {
                    verticalAngle += 180;
                    angleIsInversed = true; //
                }

                super.getPosition().y = TERRAIN_HEIGHT + basePosition.y;

                fGravity.y = 0;
                v.y = 0;
                fShot.y = 0;

                fShot.y = previousShot.y * p;
                previousShot.y = fShot.y;

                if (fShot.y < 5) {
                    fShot.y = 0;
                    previousShot.y = 0;
                    fGravity.y = 0;
                    isInTheAir = false;
                } else isInTheAir = true;
            }
        }

    }


    private void checkInputs() {
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && v.x == 0 && v.y == 0 && v.z ==0){
            Audio.play(State.ballhit);
            if(verticalAngle != 0)
            isInTheAir = true;
            reboundAngle = 0;
            angleIsInversed = false;
            treeInversed = false;
            previousShot.x = fShot.x = fShotValue / (dt * 100);
            previousShot.y = fShot.y = fShotValue / (dt * 100);
            previousShot.z = fShot.z = fShotValue / (dt * 100);

        }

        if(Keyboard.isKeyDown(Keyboard.KEY_M) && InputManager.getInputState() != InputState.MassPending){
            if(InputManager.getThread() != null)
                synchronized (InputManager.getThread()) {
                    InputManager.getThread().notify();
                }
            InputManager.setInputState(InputState.MassPending);

        }

        if(Keyboard.isKeyDown(Keyboard.KEY_G) && InputManager.getInputState() != InputState.GravityPending){
            if(InputManager.getThread() != null)
                synchronized (InputManager.getThread()) {
                    InputManager.getThread().notify();
                }
            InputManager.setInputState(InputState.GravityPending);
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_F) && InputManager.getInputState() != InputState.ForcePending){
            if(InputManager.getThread() != null)
                synchronized (InputManager.getThread()) {
                    InputManager.getThread().notify();
                }
            InputManager.setInputState(InputState.ForcePending);
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A) && InputManager.getInputState() != InputState.AnglePending){
            if(InputManager.getThread() != null)
                synchronized (InputManager.getThread()) {
                    InputManager.getThread().notify();
                }
            InputManager.setInputState(InputState.AnglePending);
        }

        if(InputManager.getInputState() == InputState.Gravity) {
            gravity = InputManager.getValue();
            InputManager.setInputState(InputState.None);
        }

        if(InputManager.getInputState() == InputState.Mass) {
            ballM = InputManager.getValue();
            InputManager.setInputState(InputState.None);
        }

        if(InputManager.getInputState() == InputState.Force) {
            fShotValue = InputManager.getValue();
            InputManager.setInputState(InputState.None);
        }

        if(InputManager.getInputState() == InputState.Angle) {
            verticalAngle = InputManager.getValue();
            InputManager.setInputState(InputState.None);
        }

    }
    private void reset(){
        previousTime = currentTime;
        super.getPosition().y = basePosition.y;
        super.getPosition().x = basePosition.x;
        super.getPosition().z = basePosition.z;
        fTotal.y = 0;
        fTotal.z = 0;
        v.x = 0;
        v.y = 0;
        v.z = 0;
        a.x = 0;
        a.y = 0;
        a.z = 0;
        treeInversed = false;
        angleIsInversed = false;
        isInTheAir = false;
        finished = false;
        fGravity.y = 0;
        fTotal.x = 0;
        fTotal.y = 0;
        fTotal.z = 0;
        fShot.x = 0;
        fShot.y = 0;
        fShot.z = 0;
        FD.x = 0;
        FD.y = 0;
        FD.z = 0;
        fFriction.x = 0;
        fFriction.y = 0;
        fFriction.z = 0;
    }

}
