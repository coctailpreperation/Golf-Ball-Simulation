package entities;

import Models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
    private TexturedModel model;
    private Vector3f position;
    private float scale;
    private float rotX,rotY,rotZ;

    public Entity(TexturedModel model, Vector3f position, float scale, float rotX, float rotY, float rotZ) {
        this.model = model;
        this.position = position;
        this.scale = scale;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }




    public void increasePosition(float dx,float dy,float dz){
        this.position.x+=dx;
        this.position.y+=dy;
        this.position.z+=dz;
    }

    public void increaseRotation(float dx,float dy,float dz){
        this.rotX+=dx;
        this.rotY+=dy;
        this.rotZ+=dz;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public float getScale() {
        return scale;
    }

    public TexturedModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
