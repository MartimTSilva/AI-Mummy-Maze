package utils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * <p>Title: ImageLoader</p> <p>Description: Singleton responsible for the
 * creation of images. <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ImageLoader {

    /**
     * Hash map that shall contain the images.
     */
    private HashMap<String, Image> images;
    /**
     * The singleton
     */
    private static final ImageLoader loader = new ImageLoader();

    /**
     * Private constructor so that no external entities may create ImageLoaders.
     */
    private ImageLoader() {
        images = new HashMap<String, Image>();
    }

    /**
     * Creates an image.
     *
     * @param name String: nome of the image file.
     * @return Image: the image.
     */
    public Image getImage(String name) {

        if (name.equals("")) {
            return null;
        }

        Image img = (Image) images.get(name);

        if (img != null) {
            return img;
        }

        Toolkit t = Toolkit.getDefaultToolkit();
        Class cl = this.getClass();
        MediaTracker media = new MediaTracker(new Component() {
        });

        try {
            img = t.createImage(cl.getResource("/images/" + name));
            if (img == null) {
                throw new Throwable("Image not found: " + name);
            }
            media.addImage(img, 0);
            images.put(name, img);
            media.waitForID(0);
            if (media.isErrorAny()) {
                throw new Exception("Media ERROR");
            }
        } catch (Throwable e) {
            System.err.println("Image not found: " + name);
            throw new RuntimeException(e.toString());
        }

        return img;
    }

    /**
     * Create an icon from the image file name.
     *
     * @param name String: image file name
     * @return Image: the icon
     */
    public ImageIcon getIcon(String name) {
        Image img = (Image) getImage(name);
        return (img != null) ? new ImageIcon(img) : null;
    }

    /**
     * Return the singleton.
     *
     * @return ImageLoader
     */
    public static ImageLoader getLoader() {
        return loader;
    }
}
