import edu.westminstercollege.cmpt328.memory.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class Image {

    private final int width, height;
    private final IntArrayValue data;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = MemorySystem.getDefault().allocateIntArray(width * height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private int index(int x, int y) {
        return x + y * width;
    }

    public int getRawPixel(int x, int y) {
        return data.get(index(x, y));
    }

    public void setRawPixel(int x, int y, int rgb) {
        data.set(index(x, y), rgb);
    }

    public Pixel getPixelAt(int x, int y) {
        return new Pixel(this, x, y);
    }

    public void save(String filename) throws IOException {
        String extension = "jpg";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex + 1 < filename.length())
            extension = filename.substring(dotIndex + 1);
        else
            filename += ".jpg";
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                image.setRGB(x, y, data.get(index(x, y)));
            }
        }

        ImageIO.write(image, extension, new File(filename));
    }

    public static Image load(String filename) throws IOException {
        BufferedImage fileImage = ImageIO.read(new File(filename));
        Image image = new Image(fileImage.getWidth(), fileImage.getHeight());
        for (int y = 0; y < image.height; ++y) {
            for (int x = 0; x < image.width; ++x) {
                image.setRawPixel(x, y, fileImage.getRGB(x, y));
            }
        }

        return image;
    }
}