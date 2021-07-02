package Textures;

public class ModelTexture {
    private int textureID;

    private float shineDamper = 1;
    private float reflectivity = 0;

    //change to decorator

    private boolean hasTransparency = false;
    private boolean hasFakeLighting = false;

    public ModelTexture(int id){
        this.textureID=id;
    }

    public boolean isHasFakeLighting() {
        return hasFakeLighting;
    }

    public void setHasFakeLighting(boolean hasFakeLighting) {
        this.hasFakeLighting = hasFakeLighting;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public ModelTexture setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
        return this;
    }

    public int getTextureID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public ModelTexture setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
        return this;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public ModelTexture setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
        return this;
    }
}
