package engineTester.audio;

public class Audio {


    private static Source[] sources = new Source[2];
    private static int [] tracks = new int[2];

    public static void run(){

        AudioMaster.init();

        AudioMaster.setListenerData(0,0,0);
        tracks[0] = AudioMaster.loadSound("engineTester/audio/backgroundmusic.wav");
        tracks[1] = AudioMaster.loadSound("engineTester/audio/ballhit.wav");

        for(int i=0;i<2;i++)
            sources[i] = new Source();

        sources[0].setVolume(0.05f);
        sources[0].setLooping(true);
        sources[1].setLooping(false);
    }

    public static void play(State state){
        int i = state.ordinal();
                if(!sources[i].isPlaying())
                sources[i].play(tracks[i]);
    }

    public static void pause(State state){
        int i = state.ordinal();
                if(sources[i].isPlaying())
                    sources[i].pause();
    }

    public static boolean isPlaying(State state){
        int i = state.ordinal();
                return sources[i].isPlaying();
    }

    public static void cleanUp(){
        for(Source source:sources)
            source.delete();
        AudioMaster.cleanUP();
    }

}
