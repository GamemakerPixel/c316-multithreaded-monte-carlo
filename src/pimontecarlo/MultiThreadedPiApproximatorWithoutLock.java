package pimontecarlo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MultiThreadedPiApproximatorWithoutLock extends PiApproximator{
    private static final long TOTAL_POINTS = 2_000_000_000L;
    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final long BLOCK_SIZE = TOTAL_POINTS / CORES;


    public static void main(String[] args) {
        MultiThreadedPiApproximatorWithoutLock approximator = new MultiThreadedPiApproximatorWithoutLock();

        approximator.approximatePi();
    }

    public MultiThreadedPiApproximatorWithoutLock() {
        super(TOTAL_POINTS);
    }

    @Override
    protected long countAllPointsInside() {
        ExecutorService service = Executors.newFixedThreadPool(CORES);
        long totalPointsInCircle = 0;
        try (service) {
            int blockCount = (int) (TOTAL_POINTS / BLOCK_SIZE);
            Future<Long>[] blockValues = new Future[blockCount];
            System.out.printf("Using %d blocks.\n", blockCount);
            for (int blockIndex = 0; blockIndex < blockCount; blockIndex++) {
                blockValues[blockIndex] = service.submit(() -> simulateCircle(BLOCK_SIZE));
            }

            // If we call .get in the previous for loop, it would force the loop to wait for the thread to return a
            // value, essentially turning this implementation into a worse single-threaded implementation.
            // To avoid this, we just have to submit all the tasks, and then wait for each to finish (which hopefully
            // occurs asynchronously.)
            for (Future<Long> futureBlockValue: blockValues) {
                totalPointsInCircle += futureBlockValue.get();
            }
        } catch (ExecutionException e) {
            System.out.println("Execution exception in a thread");
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception in a thread");
        }

        try {
            service.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException exception) {
            System.out.println("A thread was interrupted while waiting to finish tasks");
        }

        return totalPointsInCircle;
    }


}
