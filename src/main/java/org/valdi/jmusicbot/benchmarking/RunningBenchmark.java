package org.valdi.jmusicbot.benchmarking;

/**
 * Represents a Benchmark that has not finished running.
 *
 * @author Rsl1122
 */
public class RunningBenchmark {
    private final String name;
    private final long startNs;
    private final long startMemory;

    RunningBenchmark(String name) {
        this.name = name;
        startNs = System.nanoTime();
        startMemory = Runtime.getRuntime().freeMemory();
    }

    Benchmark end() {
        long endNs = System.nanoTime();
        long diffNs = endNs - startNs;

        long endMemory = Runtime.getRuntime().freeMemory();
        long estimatedMemoryUse = endMemory - startMemory;

        return new Benchmark(name, diffNs, estimatedMemoryUse);
    }
}
