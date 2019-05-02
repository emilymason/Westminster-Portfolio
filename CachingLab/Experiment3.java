
//import sun.security.util.Cache;

import edu.westminstercollege.cmpt328.memory.*;

public class Experiment3 {

    public static void main(String... args) {

        MainMemory ram = new MainMemory("RAM", // name of memory
                200 // access time in cycles
        );
        // MainMemory ram2 = new MainMemory("RAM", // name of memory
        // 200 // access time in cycles
        // );
        // MainMemory ram3 = new MainMemory("RAM", // name of memory
        // 200 // access time in cycles
        // );

        Cache l4Cache = Cache.builder().name("Cache") // name of cache
                .drawingFrom(ram) // what memory it pulls from
                .lineCount(1024) // how many lines (of 64 bytes)
                .accessTime(64) // access time in cycles
                .setAssociative(4, ReplacementAlgorithm.LRU) // use 4 way associative mapping
                .build(); // build the cache as configured

        Cache l3Cache = Cache.builder().name("Cache") // name of cache
                .drawingFrom(ram) // what memory it pulls from
                .lineCount(256) // how many lines (of 64 bytes)
                .accessTime(32) // access time in cycles
                .setAssociative(4, ReplacementAlgorithm.LRU) // use two way associative mapping
                .build(); // build the cache as configured

        Cache l2Cache = Cache.builder().name("Cache") // name of cache
                .drawingFrom(ram) // what memory it pulls from
                .lineCount(64) // how many lines (of 64 bytes)
                .accessTime(16) // access time in cycles
                .setAssociative(4, ReplacementAlgorithm.LRU) // use two way associative mapping
                .build(); // build the cache as configured

        Cache l1Cache = Cache.builder().name("Cache") // name of cache
                .drawingFrom(ram) // what memory it pulls from
                .lineCount(16) // how many lines (of 64 bytes)
                .accessTime(8) // access time in cycles
                .setAssociative(4, ReplacementAlgorithm.LRU) // use two way associative mapping
                .build(); // build the cache as configured

        MemorySystem sysA = new MemorySystem(ram); // provide the highest-level memory (closest to CPU)
        MemorySystem sysB = new MemorySystem(l4Cache); // provide the highest-level memory (closest to CPU)
        MemorySystem sysC = new MemorySystem(l3Cache); // provide the highest-level memory (closest to CPU)
        MemorySystem sysD = new MemorySystem(l2Cache); // provide the highest-level memory (closest to CPU)
        MemorySystem sysE = new MemorySystem(l1Cache); // provide the highest-level memory (closest to CPU)

        IntValue i = sysE.allocateInt();
        DoubleArrayValue list = sysE.allocateDoubleArray(20000);
        for (i.set(0); i.get() < list.getLength(); i.increment()) {
            list.set(i.get(), (Math.random() * 101));
        }

        Quicksort quick = new Quicksort(sysE);
        quick.sort(list);

        System.out.println(sysE.getTotalAccessTime());

    }

}