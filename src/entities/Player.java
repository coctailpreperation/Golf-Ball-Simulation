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
    Vector3f fShot = new Vector3f(0,0,0); // Newton
    Vector3f fGravity = new Vector3f(0, 0, 0);
    Vector3f a = new Vector3f(0,0,0);
    float dt = 0;
    float launchDelta1 = 0;
    float launchDelta2 = 0;
    float currentTime,previousTime = 0;
    float p = 0.5f;
    Vector3f previousShot = new Vector3f(0,0,0);
    Vector3f FD = new Vector3f(0,0,0);
    float kFriction = 0.4f;
    Vector3f fFriction = new Vector3f(0,0,0);
    float dx,dy,dz;
    Vector3f w = new Vector3f(0,0,0);
    Vector3f u = new Vector3f(0,0,0);

    public void move() {

        if(v.x ==0 && v.y==0 && v.z ==0) {
           launchDelta2 = Camera.angleAroundPlayer - 90;
        }
        System.out.println(v.y);

        checkInputs();

        if (isInTheAir) {
            fGravity.y = 10;
        }

        dt = 0.04f;

        currentTime = Sys.getTime()*1.0f/Sys.getTimerResolution();

        FD.x = (float) ((0.5 * 0.47 * 0.0004338 * 1.184f * Math.pow(v.x, 2)));
        FD.y = (float) ((0.5 * 0.47 * 0.0004338 * 1.184f * Math.pow(v.y, 2)));
        FD.z = (float) ((0.5 * 0.47 * 0.0004338 * 1.184f * Math.pow(v.z, 2)));


        if(!isInTheAir) {
            fFriction.z = u.z * ballM * 10 * kFriction;

            if(v.z < 0) {
                v.z = 0;
                fFriction.z = 0;
            }
            if(v.z ==0)
                fFriction.z = 0;

        }
        else fFriction.z = 0;


        if(!isInTheAir){

            fFriction.x = u.x * ballM * 10 * kFriction;

            if(v.x < 0) {
                v.x = 0;
                fFriction.x = 0;
            }
            if(v.x == 0)
                fFriction.x = 0;


        }
        else fFriction.x = 0;

        if(v.x < 0)
            u.x = -1;
        else u.x = 1;

        if(v.z < 0)
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

        if(v.x == 0 && v.z ==0 && launchDelta1 ==0)
            v.y = 0;


        System.out.println(v.x);
        System.out.println(v.z);

        dx = (float) (v.x * dt * Math.cos(Math.toRadians(launchDelta1)) * Math.sin(Math.toRadians(launchDelta2)));
        dy = (float) (v.y * dt * Math.sin(Math.toRadians(launchDelta1)));
        dz = (float) (v.z * dt * Math.cos(Math.toRadians(launchDelta1)) * Math.cos(Math.toRadians(launchDelta2)));

        w.x = dz / dt * ballRadius;
        w.z = dx / dt * ballRadius;

        super.increasePosition(dx, dy, dz);
        super.increaseRotation(w.x,0,w.z);

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
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && v.x == 0 && v.y == 0 && v.z ==0){
            Audio.play(State.ballhit);
            if(launchDelta1 != 0)
            isInTheAir = true;
            previousShot.x = fShot.x = 1000 / (dt * 100);
            previousShot.y = fShot.y = 1000 / (dt * 100);
            previousShot.z = fShot.z = 1000 / (dt * 100);
          //  String x= JOptionPane.showInputDialog("Enter A Value");
          //  int value=Integer.parseInt(x);
          //  fGravity.y = value;

        }
        if(Keyboard.isKeyDown(Keyboard.KEY_UP) && (currentTime-previousTime) > 0.5) {
            increaseDelta1();

        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && (currentTime-previousTime) > 0.5) {
            decreaseDelta1();
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
        v.x = 0;
        v.y = 0;
        v.z = 0;
        a.x = 0;
        a.y = 0;
        a.z = 0;
        isInTheAir = false;
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

}
