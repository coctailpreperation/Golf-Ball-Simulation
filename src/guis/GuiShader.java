package guis;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;

public class GuiShader extends ShaderProgram {

    private static final String vertexFile = "src/guis/guiVertexShader.txt";
    private static final String fragmentFile = "src/guis/guiFragmentShader.txt";

    private int location_transformationMatrix;

    public GuiShader() {
        super(vertexFile, fragmentFile);
    }


    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = getUniformLocation("transformationMatrix");

    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix,matrix);
    }

}
