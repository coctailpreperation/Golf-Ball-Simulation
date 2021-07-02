package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class StaticShader extends ShaderProgram{

    private static final String vertexFile = "src/shaders/vertexShader.txt";
    private static final String fragmentFile = "src/shaders/fragmentShader.txt";

    private int location_transformMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColour;


    public StaticShader() {
        super(vertexFile, fragmentFile);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColour = super.getUniformLocation("lightColour");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColour=super.getUniformLocation("skyColour");
    }

    public void loadShineVariables(float reflectivity, float shineDamper){
        super.loadFloat(location_shineDamper,shineDamper);
        super.loadFloat(location_reflectivity,reflectivity);
    }

    public void loadSkyColour(float r,float g,float b){
        super.loadVector(location_skyColour,new Vector3f(r,g,b));
    }

    public void loadViewMatrix(Camera camera){

        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix,viewMatrix);

    }

    public void loadFakeLighting(boolean useFake){
        super.loadBoolean(location_useFakeLighting,useFake);
    }

    public void loadTransformMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformMatrix,matrix);
    }

    public void loadLight(Light light){
        super.loadVector(location_lightPosition,light.getPosition());
        super.loadVector(location_lightColour, light.getColour());
    }


    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix,projection);
    }


    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoordinates");
        super.bindAttribute(2,"normal");
    }

}
