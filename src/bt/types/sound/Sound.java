package bt.types.sound;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;

import bt.utils.log.Logger;

/**
 * An audio data holder that supplies {@link Clips}s without having to reload any resources.
 * 
 * @author &#8904
 */
public class Sound
{
    private AudioFormat af;
    private int size;
    private byte[] audio;
    private DataLine.Info info;

    /**
     * Attempts to load audio data from the given file and store it in a byte array.
     * 
     * @param file
     */
    public Sound(File file)
    {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file))
        {
            this.af = audioInputStream.getFormat();
            this.size = (int)(this.af.getFrameSize() * audioInputStream.getFrameLength());
            this.audio = new byte[this.size];
            this.info = new DataLine.Info(Clip.class, this.af, this.size);
            audioInputStream.read(this.audio, 0, this.size);
        }
        catch (Exception e)
        {
            Logger.global().print(e);
        }
    }

    /**
     * Gets a clip of the stored audio data.
     * 
     * <p>
     * The clip is set to close as soon as {@link LineEvent.Type#STOP} is received.
     * </p>
     * 
     * @return
     */
    public Clip getClip()
    {
        Clip clip = null;
        try
        {
            clip = (Clip)AudioSystem.getLine(this.info);
            clip.open(this.af, this.audio, 0, this.size);
            clip.addLineListener((e) ->
            {
                if (e.getType().equals(LineEvent.Type.STOP))
                {
                    Line soundClip = e.getLine();
                    soundClip.close();
                }
            });
        }
        catch (LineUnavailableException e)
        {
            Logger.global().print(e);
        }
        return clip;
    }
}