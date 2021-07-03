package entities;

import Models.TexturedModel;
import engineTester.audio.Audio;
import engineTester.audio.State;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class Player extends Entity {

    public float getCurrentSpeed() {
        return currentSpeed;
    }


    private static final float RUN_SPEED = 1.5f;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;
    private float baseRotY = 0;
    private Vector3f basePosition = new Vector3f(0, 0, 0);

    private static final float TERRAIN_HEIGHT = 0;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInTheAir = false;

    public Player(TexturedModel model, Vector3f position, float scale, float rotX, float rotY, float rotZ) {
        super(model, position, scale, rotX, rotY, rotZ);
        baseRotY = rotY;
        basePosition.x = position.x;
        basePosition.y = position.y;
        basePosition.z = position.z;
    }

    Vector3f fTotal = new Vector3f(0, 0, 0); // Total Force
    Vector3f fHit = new Vector3f(0, 0, 0);
    Vector3f v = new Vector3f(0, 0, 0);
    float ballM = 0.04593f; // Ball Weight
    //float clubM; // Club Weight
    Vector3f fShot = new Vector3f(0,0,0); // Newton
    boolean isShot = false;
    Vector3f vW = new Vector3f(0, 0, 2.7f); //10 km/h = 2.7 m/s //Wind Speed
    Vector3f fW = new Vector3f(0, 0, 0); // Wind Force = -Cw * Vw
    float Cw = 0.5f; // Average Air Drag Coefficient ON Golf Ball
    Vector3f fGravity = new Vector3f(0, 0, 0);
    Vector3f v0 = new Vector3f(0,0,0);
    Vector3f a = new Vector3f(0,0,0);
    float dt = 0;
    float hitAngle = 45;
    float currentTime,previousTime = 0;
    float p = 0.8f;
    Vector3f currentForce = new Vector3f(0,0,0);
    Vector3f previousShot = new Vector3f(0,0,0);
    boolean isBouncing = false;

    public void move() {
        checkInputs();
        //   super.increaseRotation(0, 0.01f*currentSpeed*currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);//FrontWheels
        if (isInTheAir) {
            fGravity.y = 10;
        }

        dt = 0.01f;

        currentTime = Sys.getTime()*1.0f/Sys.getTimerResolution();


        fTotal.y = fShot.y - fGravity.y;
        fTotal.z = fShot.z;

        fShot.y = 0;
        fShot.z = 0;

        a.y = fTotal.y / ballM;
        a.z = fTotal.z / ballM;

        v.y += a.y * dt;
        v.z += a.z * dt;

        //System.out.println(v.z);


        float dy = 0.1f * v.y * dt;
        float dz = 0.1f * v.z * dt;

        super.increasePosition(0, dy, dz);
        super.increaseRotation(0.5f,0,0);

        if(super.getPosition().y < TERRAIN_HEIGHT + basePosition.y){

            super.getPosition().y = TERRAIN_HEIGHT + basePosition.y;
            fShot.y = previousShot.y * p;
            previousShot.y = fShot.y;
            //System.out.println(fShot.y);
            if(fShot.y < 10) {
                fGravity.y = 0;
                isInTheAir = false;
            }
            else isInTheAir = true;
        }

    }


    private void checkInputs() {
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !isShot){
            System.out.println("SHOT");
            isInTheAir = true;
            isShot = true;
            previousShot.y = fShot.y = (float) (5000 * Math.cos(Math.toRadians(hitAngle)));
            previousShot.z = fShot.z = (float) (5000 * Math.sin(Math.toRadians(hitAngle)));
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_UP) && (currentTime-previousTime) > 1) {
            increaseAngle();

        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && (currentTime-previousTime) > 1) {
            decreaseAngle();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_R) && (currentTime-previousTime) > 1){
            reset();
        }
    }
    private void reset(){
        previousTime = currentTime;
        super.getPosition().y = basePosition.y;
        super.getPosition().x = basePosition.x;
        super.getPosition().z = basePosition.z;
        fTotal.y = 0;
        fTotal.z = 0;
        isShot = false;
        v.y = 0;
        v.z = 0;
        a.y = 0;
        a.z = 0;
        fGravity.y = 0;
    }
    private void increaseAngle(){
        hitAngle+= 5;
        System.out.println(hitAngle);
        previousTime = currentTime;
    }
    private void decreaseAngle(){
        hitAngle-= 5;
        System.out.println(hitAngle);
        previousTime = currentTime;
    }

    //   float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY() - baseRotY)));
    //   float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY() - baseRotY)));
    //   super.increasePosition(dx, 0, dz);

    //upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
            /*super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
            if (super.getPosition().getY() < TERRAIN_HEIGHT + basePosition.y) {
                upwardsSpeed = 0;
                super.getPosition().y = TERRAIN_HEIGHT + basePosition.y;
                isInTheAir = false;
            }
             */
    // distance = distance + 0.1f;
    // float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY() - baseRotY)));

}
