package entities;

import Models.TexturedModel;
import engineTester.audio.Audio;
import engineTester.audio.State;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Player extends Entity {


    private float baseRotY = 0;
    private Vector3f basePosition = new Vector3f(0, 0, 0);

    private static final float TERRAIN_HEIGHT = 0;

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
    //float clubM; // Club Weight
    Vector3f fShot = new Vector3f(0,0,0); // Newton
    Vector3f fGravity = new Vector3f(0, 0, 0);
    Vector3f a = new Vector3f(0,0,0);
    float dt = 0;
    float launchDelta1 = 30;
    float launchDelta2 = 0;
    float currentTime,previousTime = 0;
    float p = 0.5f;
    Vector3f previousShot = new Vector3f(0,0,0);
    float delta1 = 0;
    Vector3f FD = new Vector3f(0,0,0);
    float kFriction = 0.4f;
    Vector3f fFriction = new Vector3f(0,0,0);
    float delta2 = 0;
    float distance(float x,float z){
        return (float) Math.sqrt(Math.pow(z,2) + Math.pow(x,2));
    }

    public void move() {
        //Camera.angleAroundPlayer = 90;
        if(v.x ==0 && v.y==0 && v.z ==0) {
           launchDelta2 = Camera.angleAroundPlayer - 90;
        }

        System.out.println(launchDelta2);
        checkInputs();
        if (isInTheAir) {
            fGravity.y = 10;
        }

        dt = 0.01f;

        currentTime = Sys.getTime()*1.0f/Sys.getTimerResolution();

        //int t = 1;
       // if(Math.sin(Math.toRadians(launchDelta2)) < 0)
        //    t = -1;
       // int q = 1;
       // if(Math.cos(Math.toRadians(launchDelta2)) < 0)
        //    q = -1;
        FD.x = (float) ((0.5 * 0.47 * 0.001338 * 1.184f * Math.pow(v.x, 2)));
        //* Math.sin(Math.toRadians(launchDelta2))
        FD.y = (float) ((0.5 * 0.47 * 0.001338 * 1.184f * Math.pow(v.y, 2)));
        FD.z = (float) ((0.5 * 0.47 * 0.001338 * 1.184f * Math.pow(v.z, 2)));
        //* Math.cos(Math.toRadians(launchDelta2))


        if(!isInTheAir) {

            fFriction.z = (float) (ballM * 10 * kFriction);

            if(Math.abs(v.z) - Math.abs(fFriction.z) < 0) {
                v.z = 0;
                fFriction.z = 0;
            }

        }
        else fFriction.z = 0;


        if(!isInTheAir){

            fFriction.x = (float) (ballM * 10 * kFriction);

            if(Math.abs(v.x) - Math.abs(fFriction.x) < 0) {
                v.x = 0;
                fFriction.x = 0;
            }


        }
        else fFriction.x = 0;
      //  FD.x = 0;
      //  FD.z = 0;

        fTotal.x = (float) (fShot.x  - FD.x - fFriction.x);
        fTotal.y = (float) (fShot.y - fGravity.y - FD.y);
        fTotal.z = (float) (fShot.z  - FD.z - fFriction.z);

        fShot.x = 0;
        fShot.y = 0;
        fShot.z = 0;

        a.x = fTotal.x / ballM;
        a.y = fTotal.y / ballM;
        a.z = fTotal.z / ballM;

        v.x += a.x * dt;
        v.y += a.y * dt;
        v.z += a.z * dt;


        float dx = (float) (v.x * dt * Math.cos(Math.toRadians(launchDelta1)) * Math.sin(Math.toRadians(launchDelta2)));
        float dy = (float) (v.y * dt * Math.sin(Math.toRadians(launchDelta1)));
        float dz = (float) (v.z * dt * Math.cos(Math.toRadians(launchDelta1)) * Math.cos(Math.toRadians(launchDelta2)));

        float tanAngle = (getPosition().y + basePosition.y) / distance(getPosition().x + basePosition.x, getPosition().z + basePosition.z);
        delta1 = (float) Math.toDegrees(Math.atan(tanAngle));

        float tanAngle1 = (getPosition().x + basePosition.x) / getPosition().z + basePosition.z;
        delta2 = (float) Math.toDegrees(Math.atan(tanAngle1));
        System.out.println(delta2);



        super.increasePosition(dx, dy, dz);
        super.increaseRotation(0.5f,0,0);

        if(super.getPosition().y < TERRAIN_HEIGHT + basePosition.y){

            super.getPosition().y = TERRAIN_HEIGHT + basePosition.y;

            fGravity.y = 0;
            v.y = 0;
            fShot.y = 0;
            fShot.y = previousShot.y * p;
            previousShot.y = fShot.y;

            if(fShot.y < 10) {
                fShot.y = 0;
                previousShot.y = 0;
                fGravity.y = 0;
                isInTheAir = false;
            }
            else isInTheAir = true;
        }

    }


    private void checkInputs() {
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && v.x == 0 && v.z ==0){
            Audio.play(State.ballhit);
            if(launchDelta1 != 0)
            isInTheAir = true;
            previousShot.x = fShot.x = 2000;
            previousShot.y = fShot.y = 2000;
            previousShot.z = fShot.z = 2000;

        }
        if(Keyboard.isKeyDown(Keyboard.KEY_UP) && (currentTime-previousTime) > 0.5) {
            increaseDelta1();

        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && (currentTime-previousTime) > 0.5) {
            decreaseDelta1();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && (currentTime-previousTime) > 0.5)
            increaseDelta2();
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && (currentTime-previousTime) > 0.5)
            decreaseDelta2();

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
        v.x = 0;
        v.y = 0;
        v.z = 0;
        a.x = 0;
        a.y = 0;
        a.z = 0;
        fGravity.y = 0;
    }
    private void increaseDelta1(){
        launchDelta1 += 5;
        System.out.println("Delta 1 = " + launchDelta1);
        previousTime = currentTime;
    }
    private void decreaseDelta1(){
        launchDelta1 -= 5;
        System.out.println("Delta 1 = " + launchDelta1);
        previousTime = currentTime;
    }
    private void increaseDelta2(){
        launchDelta2 +=5;
        System.out.println("Delta 2 = " + launchDelta2);
        previousTime = currentTime;
    }
    private void decreaseDelta2(){
        launchDelta2 -=5;
        System.out.println("Delta 2 = " + launchDelta2);
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
