import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Nnamdi on 12/15/2016.
 */
public class Music {
    public static void music () {
        AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;
        AudioData MD;
        ContinuousAudioDataStream loop = null;
        try {
            BGM = new AudioStream ( new FileInputStream ( "jingleBells.mps" ) );
            MD = BGM.getData ( );
            loop = new ContinuousAudioDataStream ( MD );
        } catch (IOException e) {
        }
        MGP.start ( loop );
    }
}
