package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram{

    private static final String vertexFile = "src/shaders/terrainVertexShader.txt";
    private static final String fragmentFile = "src/shaders/terrainFragmentShader.txt";

    private int location_transformMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColour;
    private int location_backgroundTexture;
    private int location_rTexture;
    private int location_bTexture;
    private int location_gTexture;
    private int location_blendMap;


    public TerrainShader() {
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
        location_skyColour = super.getUniformLocation("skyColour");
        location_backgroundTexture = super.getUniformLocation("backgroundTexture");
        location_rTexture = super.getUniformLocation("rTexture");
        location_gTexture = super.getUniformLocation("gTexture");
        location_bTexture = super.getUniformLocation("bTexture");
        location_blendMap = super.getUniformLocation("blendMap");
    }

    public void loadSkyColour(float r,float g,float b){
        super.loadVector(location_skyColour,new Vector3f(r,g,b));
    }

    public void loadShineVariables(float shineDamper, float reflectivity){
        super.loadFloat(location_shineDamper,shineDamper);
        super.loadFloat(location_reflectivity,reflectivity);
    }

    public void connectTextureUnits(){

        super.loadInteger(location_backgroundTexture,0);
        super.loadInteger(location_rTexture,1);
        super.loadInteger(location_gTexture,2);
        super.loadInteger(location_bTexture,3);
        super.loadInteger(location_blendMap,4);

    }

    public void loadViewMatrix(Camera camera){

        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix,viewMatrix);

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
