package bt.types.sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import bt.utils.num.NumberUtils;

/**
 * 
 * @author &#8904
 */
public class Sound
{
    private SoundSupplier supplier;
    private Clip clip;
    private float volume = 1;

    public Sound(SoundSupplier supplier)
    {
        this.supplier = supplier;
    }

    /**
     * Sets the volume of the sound.
     * 
     * @param volume
     *            A volume value between 0 (no volume) and 1 (highest volume). Values that are below 0 or above 1 will
     *            be clamped to their clostest bound, i. e. -5 becomes 0 and 14 becomes 1.
     */
    public void setVolume(float volume)
    {
        volume = NumberUtils.clamp(volume, 0, 1);
        this.volume = volume;
        
        if (this.clip != null)
        {
            FloatControl masterGain = (FloatControl)this.clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = masterGain.getMaximum() - masterGain.getMinimum();
            float gain = (range * volume) + masterGain.getMinimum();
            masterGain.setValue(gain);
        }
    }

    private void setupClip()
    {
        if (this.clip != null)
        {
            this.clip.stop();
        }

        this.clip = this.supplier.getClip();
        setVolume(this.volume);
    }

    /**
     * Plays the sound once.
     */
    public void start()
    {
        setupClip();
        this.clip.start();
    }

    /**
     * Plays the sound in a continous loop.
     */
    public void loop()
    {
        loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Plays the sound <i>count + 1</i> times.
     * 
     * @param count
     */
    public void loop(int count)
    {
        setupClip();
        this.clip.loop(count);
    }

    public void stop()
    {
        if (this.clip != null)
        {
            this.clip.stop();
        }
    }
}