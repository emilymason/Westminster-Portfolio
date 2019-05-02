import java.util.Random;

import edu.westminstercollege.cmpt328.memory.*;

public class Quicksort {

    private final MemorySystem sys;
    private final DoubleValue tmp;
    private final Random random = new Random();

    public Quicksort(MemorySystem sys) {
        this.sys = sys;
        this.tmp = sys.allocateDouble();
    }

    public void sort(DoubleArrayValue data) {
        sort(data, 0, data.getLength());
    }

    public void sort(DoubleArrayValue data, int a, int b) {
        int n = b - a;
        if (n < 2)
            return;

        IntValue piv = sys.allocateInt(a + random.nextInt(n));
        IntValue i = sys.allocateInt(a);
        swap(data, piv.get(), i.get());
        piv.set(a);

        for (i.set(a + 1); i.get() < b; i.increment()) {
            if (data.get(i) < data.get(piv)) {
                swap(data, i.get(), piv.get() + 1);
                swap(data, piv.get(), piv.get() + 1);
                piv.increment();
            }
        }

        sort(data, a, piv.get());
        sort(data, piv.get() + 1, b);
    }

    private void swap(DoubleArrayValue array, int a, int b) {
        tmp.set(array.get(a));
        array.set(a, array.get(b));
        array.set(b, tmp);
    }
}