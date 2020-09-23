package bt.utils;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 * @author &#8904
 *
 */
public final class ScreenUtils
{
    /**
     * Gets the dimensions in pixels of the screen device with the given index.
     * 
     * @param screenIndex
     * @return
     */
    public static Dimension getScreenDimensions(int screenIndex)
    {
        GraphicsDevice device = getScreens()[screenIndex];
        DisplayMode mode = device.getDisplayMode();
        return new Dimension(mode.getWidth(),
                             mode.getHeight());
    }

    /**
     * Gets the dimensions in pixels of the default screen device.
     * 
     * @return
     */
    public static Dimension getScreenDimensions()
    {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode mode = device.getDisplayMode();
        return new Dimension(mode.getWidth(),
                             mode.getHeight());
    }

    /**
     * Gets an array of all available screen devices.
     * 
     * @return
     */
    public static GraphicsDevice[] getScreens()
    {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return environment.getScreenDevices();
    }
}