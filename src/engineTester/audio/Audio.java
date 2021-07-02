package engineTester.audio;

public class Audio {


    private static Source[] sources = new Source[12];
    private static int [] tracks = new int[12];

    public static void run(){

        AudioMaster.init();

        AudioMaster.setListenerData(0,0,0);
        tracks[0] = AudioMaster.loadSound("engineTester/audio/start.wav");
        tracks[1] = AudioMaster.loadSound("engineTester/audio/standby.wav");
        tracks[2] = AudioMaster.loadSound("engineTester/audio/reverse.wav");


        tracks[3] = AudioMaster.loadSound("engineTester/audio/first.wav");
        tracks[4] = AudioMaster.loadSound("engineTester/audio/firstrolling.wav");


        tracks[6] = AudioMaster.loadSound("engineTester/audio/second.wav");
        tracks[7] = AudioMaster.loadSound("engineTester/audio/secondrolling.wav");

        tracks[9] = AudioMaster.loadSound("engineTester/audio/third.wav");

        for(int i=0;i<12;i++)
            sources[i] = new Source();


        // source1 standby
        // source0 start
        // source2 first
        // source3 firststandby

        sources[1].setLooping(true);
    }

  /*  public static boolean isPlaying(int i){
        switch (i) {
            case 1: {
                source.play(x);
                break;
            }
            case 2:{
                source1.play(y);
                break;
            }
            case 3:{
                source2.play(z);
                break;
            }
        }
    }

   */

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
