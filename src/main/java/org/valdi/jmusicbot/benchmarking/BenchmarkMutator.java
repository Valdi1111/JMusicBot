package org.valdi.jmusicbot.benchmarking;

import java.util.List;

/**
 * Mutator object for a List of {@link Benchmark}s.
 *
 * @author Rsl1122
 */
public class BenchmarkMutator {
    private final List<Benchmark> benchmarks;

    public BenchmarkMutator(List<Benchmark> benchmarks) {
        this.benchmarks = benchmarks;
    }

    public Benchmark average() {
        if (benchmarks.isEmpty()) {
            return new Benchmark(0, 0);
        }
        long totalNs = 0;
        long totalMem = 0;
        int size = benchmarks.size();
        for (Benchmark benchmark : benchmarks) {
            totalNs += benchmark.getNs();
            totalMem += benchmark.getUsedMemory();
        }
        return new Benchmark(benchmarks.get(0).getName(), totalNs / size, totalMem / size);
    }
}
