import java.io.IOException;

import edu.westminstercollege.cmpt328.memory.*;

public class Experiment4 {

    public static void main(String... args) throws IOException {

        MemorySystem sys = new MemorySystem.CoreI7();
        MemorySystem.setDefault(sys);

        Image i = Image.load("pokemon.png");
        IntValue x = sys.allocateInt();
        IntValue y = sys.allocateInt();

        for (y.set(0); y.get() < i.getHeight(); y.increment()) {
            for (x.set(0); x.get() < i.getWidth(); x.increment()) {
                Pixel p = i.getPixelAt(x.get(), y.get());
                int b = p.getBlue();
                int r = p.getRed();
                int g = p.getGreen();

                int avg = (r + b + g) / 3;

                p.setRed(avg);
                p.setBlue(avg);
                p.setBlue(avg);
            }

        }
        i.save("newpoke.png");

        System.out.println(sys.getTotalAccessTime());

    }
}