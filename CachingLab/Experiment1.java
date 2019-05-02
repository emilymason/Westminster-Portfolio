import edu.westminstercollege.cmpt328.memory.*;

public class Experiment1 {

    public static void main(String... args) {

        MainMemory ram = new MainMemory("RAM", // name of memory
                200 // access time in cycles
        );
        MainMemory ram2 = new MainMemory("RAM", // name of memory
                200 // access time in cycles
        );

        Cache cache = Cache.builder().name("Cache") // name of cache
                .drawingFrom(ram) // what memory it pulls from
                .lineCount(256) // how many lines (of 64 bytes)
                .accessTime(20) // access time in cycles
                .directMapping() // use direct mapping
                .build(); // build the cache as configured

        Cache cache2 = Cache.builder().name("Cache") // name of cache
                .drawingFrom(ram2) // what memory it pulls from
                .lineCount(8) // how many lines (of 64 bytes)
                .accessTime(25) // access time in cycles
                .setAssociative(2, ReplacementAlgorithm.LRU) // use two way associative mapping
                .build(); // build the cache as configured

        MemorySystem sysA = new MemorySystem(cache); // provide the highest-level memory (closest to CPU)
        MemorySystem sysB = new MemorySystem(cache2); // provide the highest-level memory (closest to CPU)

        IntArrayValue a = sysB.allocateIntArray(6144);
        IntArrayValue b = sysB.allocateIntArray(6144);
        IntArrayValue c = sysB.allocateIntArray(6144);

        for (int i = 0; i < a.getLength(); i++) {
            b.set(i, a.get(i));

        }
        System.out.printf("\n\nCycles used: %d\n", sysB.getTotalAccessTime());
        System.out.println("Memory system summary");
        sysB.printStatistics();
        sysB.resetMemories();

        for (int i = 0; i < a.getLength(); i++) {
            c.set(i, a.get(i));
        }
        System.out.printf("\n\nCycles used: %d\n", sysB.getTotalAccessTime());
        // System.out.println("\nCache after these accesses:");
        // cache.print();
        // System.out.println("\n");
        // System.out.println("ADDRESS A:" + a.getAddress());
        // System.out.println("ADDRESS B:" + b.getAddress());
        // System.out.println("ADDRESS C:" + c.getAddress());
        System.out.println("Memory system summary");
        sysB.printStatistics();
    }

}