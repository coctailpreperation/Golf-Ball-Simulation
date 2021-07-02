package entities;

import Models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class MovingObject extends Entity{
    private static final float RUN_SPEED = 1.5f;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;
    private float baseRotY = 0;
    private Vector3f basePosition = new Vector3f(0,0,0);

    private static final float TERRAIN_HEIGHT = 0;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInTheAir = false;

    public MovingObject(TexturedModel model, Vector3f position, float scale, float rotX, float rotY, float rotZ) {
        super(model, position, scale, rotX, rotY, rotZ);
        baseRotY = rotY;
        basePosition.x=position.x;
        basePosition.y=position.y;
        basePosition.z=position.z;
    }
    public void rotateY(){
        checkInputs();
        if(getRotY() - baseRotY + currentTurnSpeed*DisplayManager.getFrameTimeSeconds() <30 && getRotY() - baseRotY + currentTurnSpeed*DisplayManager.getFrameTimeSeconds() >-30)
        super.increaseRotation(0,currentTurnSpeed*DisplayManager.getFrameTimeSeconds(),0);

    }

    public Vector3f getBasePosition() {
        return basePosition;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public void rotateX(){
        checkInputs();
        super.increaseRotation(currentSpeed * DisplayManager.getFrameTimeSeconds() * 10,0,0);
    }

    public void move(){
            checkInputs();
            float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
            float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY() - baseRotY)));
            float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY() - baseRotY)));
            super.increasePosition(dx, 0, dz);
            upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
            super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);

            if (super.getPosition().getY() < TERRAIN_HEIGHT + basePosition.y) {
                upwardsSpeed = 0;
                super.getPosition().y = TERRAIN_HEIGHT + basePosition.y;
                isInTheAir = false;
            }
    }

    private void jump(){
        if(!isInTheAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInTheAir = true;
        }

    }

    private void checkInputs(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed = currentSpeed+RUN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = currentSpeed-RUN_SPEED;
        }else {
            if(currentSpeed>0)
                this.currentSpeed = currentSpeed-RUN_SPEED;
            else if(currentSpeed<0)
                this.currentSpeed = currentSpeed+RUN_SPEED;
            else currentSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentTurnSpeed = -TURN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentTurnSpeed = TURN_SPEED;
        }else {
            this.currentTurnSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }
    }
}
