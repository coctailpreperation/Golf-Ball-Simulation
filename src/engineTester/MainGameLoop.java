package engineTester;

import Models.TexturedModel;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import entities.*;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import Models.RawModel;
import renderEngine.MasterRenderer;
import terrains.Terrain;

import java.util.ArrayList;
import java.util.Random;

public class MainGameLoop {
    public static void main(String[] args){

        /************/

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        ModelData bodyData = OBJFileLoader.loadOBJ( "/ball");
        RawModel bodyRawModel = loader.loadToVAO(bodyData.getVertices(),bodyData.getTextureCoords(),bodyData.getNormals(),bodyData.getIndices());
        TexturedModel bodyModel= new TexturedModel(bodyRawModel,new ModelTexture(loader.loadTexture("/ball")).setHasTransparency(false).setShineDamper(100).setReflectivity(1));
        Player golfBall = new Player(bodyModel,new Vector3f(30,1,30),1,0,0,0);



        ModelData grassData = OBJFileLoader.loadOBJ("/environment/grass");
        RawModel grassModel = loader.loadToVAO(grassData.getVertices(),grassData.getTextureCoords(),grassData.getNormals(),grassData.getIndices());
        ModelData fernData = OBJFileLoader.loadOBJ("/environment/fern");
        RawModel fernModel = loader.loadToVAO(fernData.getVertices(),fernData.getTextureCoords(),fernData.getNormals(),fernData.getIndices());
        ModelData lowPolyTreeData = OBJFileLoader.loadOBJ("/environment/lowPolyTree");
        RawModel lowPolyTreeModel = loader.loadToVAO(lowPolyTreeData.getVertices(),lowPolyTreeData.getTextureCoords(),lowPolyTreeData.getNormals(),lowPolyTreeData.getIndices());


        TexturedModel fernTexturedModel = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("/environment/fern")));
        TexturedModel grassTexturedModel = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("/environment/grass")));
        TexturedModel lowPolyTreeTexturedModel = new TexturedModel(lowPolyTreeModel,new ModelTexture(loader.loadTexture("/environment/lowPolyTree")));

        grassTexturedModel.getTexture().setHasTransparency(true);
        grassTexturedModel.getTexture().setHasFakeLighting(true);
        fernTexturedModel.getTexture().setHasTransparency(true);
        Entity[] fern = new Entity[1000];
        Entity[] grass = new Entity[2000];
        Entity[] lowPolyTree = new Entity[100];
        Random random = new Random();
        for(int i=0;i<2000;i++){
            grass[i] = new Entity(grassTexturedModel, new Vector3f(random.nextInt(780)+10,0,random.nextInt(780)+10),1,0,0,0);

        }
        for(int i=0;i<100;i++) {
            lowPolyTree[i] = new Entity(lowPolyTreeTexturedModel, new Vector3f(random.nextInt(780)+10,0,random.nextInt(780)+10),0.5f,0,0,0);
        }

        for(int i=0;i<1000;i++){
            fern[i] = new Entity(fernTexturedModel, new Vector3f(random.nextInt(780)+10,0,random.nextInt(780)+10),1,0,0,0);
        }

        Light light = new Light(new Vector3f(0,2000,0), new Vector3f(1,1,1));

       /*TerrainTexture Stuff*/

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("/terrain/ra"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("/terrain/dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("/terrain/mud"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("/terrain/path"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("/terrain/blendMap"));

        //*****************************//

        ArrayList<Terrain> terrains = new ArrayList<>();

        //Terrain terrain = new Terrain(0,0,loader,texturePack,blendMap,"/terrain/heightMap");
        //Terrain terrain1 = new Terrain(-1,-1,loader,texturePack,blendMap,"/terrain/heightMap");
        for(int i = 0 ; i < 5 ; i ++){
            Terrain terrain = new Terrain(0,i,loader,texturePack,blendMap,"/terrain/heightMap");
            terrains.add(terrain);
        }


        Camera camera = new Camera(golfBall);

        MasterRenderer renderer = new MasterRenderer();




        while(!Display.isCloseRequested()){


            camera.move();

            golfBall.move();



            for(Terrain terrain : terrains)
                renderer.processTerrain(terrain);

            renderer.processEntity(golfBall);
            for(Entity grassObject:grass){
                renderer.processEntity(grassObject);
            }
           for(Entity fernObject:fern){
                renderer.processEntity(fernObject);
            }
            for(Entity lowPolyTreeObject:lowPolyTree){
                renderer.processEntity(lowPolyTreeObject);
            }



            renderer.render(light,camera);
            DisplayManager.updateDisplay();
        }
        renderer.cleanUP();
        loader.cleanUP();
        DisplayManager.closeDisplay();

    }
}
