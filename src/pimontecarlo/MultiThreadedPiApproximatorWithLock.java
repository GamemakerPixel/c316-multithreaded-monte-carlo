package pimontecarlo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class MultiThreadedPiApproximatorWithLock extends PiApproximator{
    private static final long TOTAL_POINTS = 2_000_000_000L;
    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final long BLOCK_SIZE = 5000L;

    private long totalPointsInCircle = 0;
    private final ReentrantLock pointsLock = new ReentrantLock();

    public static void main(String[] args) {
        MultiThreadedPiApproximatorWithLock approximator = new MultiThreadedPiApproximatorWithLock();

        approximator.approximatePi();
    }

    public MultiThreadedPiApproximatorWithLock() {
        super(TOTAL_POINTS);
    }

    @Override
    protected long countAllPointsInside() {
        ExecutorService service = Executors.newFixedThreadPool(CORES);
        try (service) {
            long blockCount = TOTAL_POINTS / BLOCK_SIZE;
            System.out.printf("Using %d blocks.\n", blockCount);
            for (long blockIndex = 0; blockIndex < blockCount; blockIndex++) {
                service.submit(() -> {
                    long pointsInCircle = simulateCircle(BLOCK_SIZE);

                    pointsLock.lock();
                    try {
                        totalPointsInCircle += pointsInCircle;
                    }
                    finally {
                        pointsLock.unlock();
                    }
                });
            }
        }
        try {
            service.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException exception) {
            System.out.println("A thread was interrupted");
        }

        return totalPointsInCircle;
    }


}
