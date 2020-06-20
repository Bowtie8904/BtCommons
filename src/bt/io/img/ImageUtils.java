package bt.io.img;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Utilities for simple image converting/scaling.
 *
 * @author &#8904
 */
public class ImageUtils
{
    /**
     * Converts the given base64 String to a {@link BufferedImgae}.
     *
     * @param base64String
     *            The base64 String to convert.
     * @return The converted image.
     * @throws IOException
     */
    public static Image getImageFromBase64(String base64String) throws IOException
    {
        BufferedImage image = null;
        byte[] imageByte;
        imageByte = Base64.getDecoder().decode(base64String);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        image = ImageIO.read(bis);
        bis.close();
        return image;
    }

    /**
     * Converts the given image to a base64 String.
     *
     * @param image
     *            The image to convert.
     * @param fileEnding
     *            The file ending of the image file.
     * @return The converted base64 String.
     * @throws IOException
     */
    public static String getBase64String(BufferedImage image, String fileEnding) throws IOException
    {
        String base64 = null;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream())
        {
            ImageIO.write(image,
                          fileEnding,
                          out);
            base64 = Base64.getEncoder().encodeToString(out.toByteArray());
        }

        return base64;
    }

    /**
     * Scales the image to keep its height and width within the given maxHeight and maxWidth while maintaining its
     * aspect ratio.
     *
     * @param srcImg
     *            The image to scale.
     * @param maxWidth
     *            The maximum width of the new image.
     * @param maxHeight
     *            The maximum height of the new image.
     * @return The scaled image.
     */
    public static Image scaleImageCloseTo(BufferedImage srcImg, int maxWidth, int maxHeight)
    {
        int width = srcImg.getWidth();
        int height = srcImg.getHeight();

        int distWidth = maxWidth - width;
        int distHeight = maxHeight - height;

        double scale = 2.0;

        if (distWidth < distHeight)
        {
            scale = (double)maxWidth / width;
        }
        else
        {
            scale = (double)maxHeight / height;
        }

        width = (int)(width * scale);
        height = (int)(height * scale);

        BufferedImage resizedImg = new BufferedImage(width,
                                                     height,
                                                     BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.drawImage(srcImg,
                     0,
                     0,
                     width,
                     height,
                     null);
        g2.dispose();

        return resizedImg;
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img
     *            The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage)img;
        }

        BufferedImage bimage = new BufferedImage(img.getWidth(null),
                                                 img.getHeight(null),
                                                 BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img,
                      0,
                      0,
                      null);
        bGr.dispose();

        return bimage;
    }

    /**
     * Creates a new ImageIcon from the given file.
     *
     * @param file
     *            The file to create the icon from.
     * @return The created icon.
     * @throws MalformedURLException
     */
    public static ImageIcon getImageIcon(File file) throws MalformedURLException
    {
        return new ImageIcon(file.toURI().toURL());
    }

    /**
     * Creates a new ImageIcon from the given file.
     *
     * @param stream
     *            The stream to create the icon from.
     * @return The created icon.
     * @throws IOException
     */
    public static ImageIcon getImageIcon(InputStream stream) throws IOException
    {
        return new ImageIcon(ImageIO.read(stream));
    }
}