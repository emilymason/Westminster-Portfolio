public class Pixel {

    private final Image image;
    private final int x, y;

    public Pixel(Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public int getRaw() {
        return image.getRawPixel(x, y);
    }

    public int getRed() {
        return (getRaw() >> 16) & 0xff;
    }

    public int getGreen() {
        return (getRaw() >> 8) & 0xff;
    }

    public int getBlue() {
        return getRaw() & 0xff;
    }

    public void setRaw(int raw) {
        image.setRawPixel(x, y, raw);
    }

    public void setRed(int red) {
        int p = getRaw();
        p = (p & 0x00ffff) | ((red << 16) & 0xff0000);
        setRaw(p);
    }

    public void setGreen(int green) {
        int p = getRaw();
        p = (p & 0xff00ff) | ((green << 8) & 0x00ff00);
        setRaw(p);
    }

    public void setBlue(int blue) {
        int p = getRaw();
        p = (p & 0xffff00) | (blue & 0x0000ff);
        setRaw(p);
    }
}