package renderEngine;

import Models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

//Loading Model

public class Loader {

    private List<Integer> vaos= new ArrayList<>();
    private List<Integer> vbos= new ArrayList<>();
    private List<Integer> textures= new ArrayList<>();


    //convert float array to FloatBuffer
    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer= BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip(); //buffer is ready to be used
        return buffer;
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID=GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);

    }

    private IntBuffer storeDataInIntBuffer(int []data){
        IntBuffer buffer=BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void cleanUP(){
        for(int vao:vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo:vbos){
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture:textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    //Load Positions Into VAO and STore it In Memory

    public RawModel loadToVAO(float[] positions,float[] textureCoordinates,float[] normals, int[] indices){
        int vaoID=createVAO();
        bindIndicesBuffer(indices);
        //16 Attribute Lists
        //Attribute List "vbo"
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoordinates);
        storeDataInAttributeList(2,3,normals);
        unBindVAO();
        return new RawModel(vaoID,indices.length);
    }

    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    public int loadTexture(String fileName){
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG",new FileInputStream("res"+fileName+".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_MAX_TEXTURE_LOD_BIAS,-0.4f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert texture != null;
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }



    private void storeDataInAttributeList(int attributeNumber,int coordinateSize,float[] data){
        //vbo is a buffer
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
        FloatBuffer buffer=storeDataInFloatBuffer(data);
        //First Attribute To identify the Buffer Type , Second Attribute is the Location , Third Attribute is The Usage "Editable or Static"
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
        //Putting The VBO "The FloatBuffer" into The VAO
        //First Attribute is the size which is 3 because its 3d Vertices , 2-The Type Of These Vertices,
        // 3-Normalized or Not,Distance Between Vertices
        // 4-Start in the Beginning
        GL20.glVertexAttribPointer(attributeNumber,coordinateSize, GL11.GL_FLOAT,false,0,0);
        // Unbinding The VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);

    }


    private void unBindVAO(){
        GL30.glBindVertexArray(0);
    }



}
