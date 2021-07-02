package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

public class DisplayManager {
    private static final int width = 1280;
    private static final int height = 720;
    private static final int fpsCap = 120;

    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay(){
        ContextAttribs attribs=new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);

        try
        {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create(new PixelFormat(),attribs);
            Display.setTitle("Out First Display");
        }catch(LWJGLException e){
            e.printStackTrace();
        }
        GL11.glViewport(0,0, width, height);
        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay(){
        Display.sync(fpsCap);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds(){
        return delta;
    }

    public static void closeDisplay(){
        Display.destroy();
    }

    private static long getCurrentTime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }

}
