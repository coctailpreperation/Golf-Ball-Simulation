package entities;

import Models.TexturedModel;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

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
    Vector3f v = new Vector3f(0, 0, 0);
    float ballM = 0.04593f; // Ball Weight
    Vector3f fShot = new Vector3f(0,0,0); // Newton
    boolean isShot = false;
    Vector3f fGravity = new Vector3f(0, 0, 0);
    Vector3f a = new Vector3f(0,0,0);
    float dt = 0;
    float verticalHitAngle = 45;
    float horizontalHitAngle = 30;
    float currentTime,previousTime = 0;
    float p = 0.8f;
    Vector3f FD = new Vector3f(0,0,0);
    Vector3f FF = new Vector3f(0,0,0);
    float gravity = 10;
    Vector3f W = new Vector3f(0,0,0);
    Vector3f U = new Vector3f(0,0,0);
    float verticalAngle = 0;
    float horizontalAngle = 0;

    float distance(float x,float y){
        return (float) Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    public void move() {
        checkInputs();

        horizontalHitAngle = Camera.angleAroundPlayer - 90;

        dt = 0.01f;

        currentTime = Sys.getTime()*10f/Sys.getTimerResolution();


        W.x = gravity * ballM * (float) Math.cos(Math.toRadians(90));
        W.y = gravity * ballM * (float) Math.sin(Math.toRadians(90));
        W.z = gravity * ballM * (float) Math.cos(Math.toRadians(90));

        FD.x = 0.5f * 0.47f * 1.2f * 0.0004f * (float) Math.pow(v.x , 2)
                * (float) Math.cos(Math.toRadians(verticalAngle)) * (float) Math.sin(Math.toRadians(horizontalAngle + 90));



        FD.y = 0.5f * 0.47f * 1.2f * 0.00009f * (float) Math.pow(v.y , 2)
                * (float) Math.sin(Math.toRadians(verticalAngle));
        FD.z = 0.5f * 0.47f * 1.2f * 0.00009f * (float) Math.pow(v.z , 2)
                * (float) Math.cos(Math.toRadians(verticalAngle)) * (float) Math.cos(Math.toRadians(horizontalAngle + 90));

        if(!isInTheAir) {
            FF.z = U.z * ballM * gravity * 0.4f * (float) Math.cos(Math.toRadians(0));
            if(v.z < 0)
                FF.z = 0;
        }
        else FF.z = 0;

        if(!isInTheAir) {
            FF.x = U.x * ballM * gravity * 0.4f * (float) Math.cos(Math.toRadians(0));
            if(v.x < 0)
                FF.x = 0;
        }
        else FF.x = 0;

        if(v.z < 0)
            U.z = -1;
        else U.z = 1;

        if(v.x < 0)
            U.x = -1;
        else U.x = 1;



        fTotal.x = fShot.x - FD.x - W.x - FF.x;
        fTotal.y = fShot.y - FD.y - W.y;
        fTotal.z = fShot.z - FD.z - W.z - FF.z;

        if(isShot){
            fShot.x = 0;
            fShot.y = 0;
            fShot.z = 0;
        }

        a.x = fTotal.x / ballM;
        a.y = fTotal.y / ballM;
        a.z = fTotal.z / ballM;

        v.x += a.x * dt;
        v.y += a.y * dt;
        v.z += a.z * dt;

     //   System.out.println(v.z);


        float dx = 0.5f * v.x * dt;
        float dy = 0.5f * v.y * dt;
        float dz = 0.5f * v.z * dt;

        float vTanAngle =  getPosition().y /  distance(basePosition.x - getPosition().x,basePosition.z - getPosition().z);
        verticalAngle = (float) Math.toDegrees(Math.atan(vTanAngle));

     //   System.out.println(basePosition.x - getPosition().x);
        float hTanAngle = basePosition.x - getPosition().x / basePosition.z - getPosition().z;
        horizontalAngle = (float) Math.toDegrees(Math.atan(hTanAngle));

       // System.out.println(horizontalAngle + 90);

        super.increasePosition(dx, dy, dz);
        super.increaseRotation(0.5f,0,0);

        if(super.getPosition().getY() < TERRAIN_HEIGHT + basePosition.y){
            fGravity.y = 0;
            super.getPosition().y = TERRAIN_HEIGHT + basePosition.y;
            isInTheAir = false;

        }
        if (isInTheAir) {
            fGravity.y = 10;
        }
    }


    private void checkInputs() {
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !isShot){
            System.out.println("SHOT");
            isInTheAir = true;
            isShot = true;
            fShot.x = (float) (400 * Math.cos(Math.toRadians(verticalHitAngle)) * Math.sin(Math.toRadians(verticalHitAngle)));
            fShot.y = (float) (400 * Math.sin(Math.toRadians(verticalHitAngle)));
            fShot.z = (float) (400 * Math.cos(Math.toRadians(verticalHitAngle)) * Math.cos(Math.toRadians(horizontalHitAngle)));
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_UP) && (currentTime-previousTime) > 1) {
            increaseAngle();

        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && (currentTime-previousTime) > 1) {
            decreaseAngle();
        }
    }
    private void increaseAngle(){
        verticalHitAngle += 5;
        System.out.println(verticalHitAngle);
        previousTime = currentTime;
    }
    private void decreaseAngle(){
        verticalHitAngle -= 5;
        System.out.println(verticalHitAngle);
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
