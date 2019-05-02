import edu.westminstercollege.cmpt328.memory.*;

public class Experiment0 {

    public static void main(String... args) {
        // Make a simple two-level memory system

        // Due to the structure of the code we always start at the bottom (RAM)
        MainMemory ram = new MainMemory(
                "RAM",      // name of memory
                200     // access time in cycles
        );

        // Make a cache to sit on top of the RAM
        // Since there are so many parameters to specify, the Cache class uses the "builder" pattern for construction
        Cache cache = Cache.builder()
                .name("Cache")      // name of cache
                .drawingFrom(ram)   // what memory it pulls from
                .lineCount(8)       // how many lines (of 64 bytes)
                .accessTime(20)     // access time in cycles
                .directMapping()    // use direct mapping
                .build();           // build the cache as configured

        // Create a memory system to help us access these memories
        MemorySystem sys = new MemorySystem(cache); // provide the highest-level memory (closest to CPU)

        // Print out the cache in its current (vacant state)
        System.out.println("Cache before any accesses:");
        cache.print();
        System.out.println("\n");

        // Make some memory accesses (use the MemorySystem to do this)
        IntValue a = sys.allocateInt();
        a.set(0xbeaded);
        System.out.printf("a = %d\n", a.get());  // prints 12,496,365 (base 10 version of 0xbeaded)

        DoubleValue x = sys.allocateDouble();
        x.set(Math.PI);
        x.set(x.get() * x.get());
        System.out.printf("x = %f\n", x.get());  // prints 9.869604 ≈ π²

        IntArrayValue numbers = sys.allocateIntArray(4); // array of 4 ints
        numbers.set(0, 10);  // assign values to the four elements
        numbers.set(1, 20);
        numbers.set(2, 30);
        numbers.set(3, 40);
        System.out.print("numbers =");
        // How to write a for loop controlled by an IntValue
        IntValue i = sys.allocateInt();
        for (i.set(0); i.get() < numbers.getLength(); i.increment())  // prints 10 20 30 40
            System.out.print(" " + numbers.get(i));
        System.out.println();

        // How many CPU cycles did we use on our fictional computer with these memory accesses?
        System.out.printf("\n\nCycles used: %d\n", sys.getTotalAccessTime());

        // Print out the cache again
        System.out.println("\nCache after these accesses:");
        cache.print();

        // The MemorySystem can also print out a summary of accesses, hit ratios, and so on
        System.out.println("\n");
        System.out.println("Memory system summary");
        sys.printStatistics();
    }
}