package pimontecarlo;

import java.time.Duration;
import java.time.Instant;

public class SingleThreadedPiApproximator {
    private static final long totalPoints = 500_000_000;

    public static void main(String args[]){
        Instant start = Instant.now();
        System.out.println("Pi is " + approximatePi());
        Instant end = Instant.now();

        long timeElapsed = Duration.between(start, end).toMillis();

        System.out.println("Time taken: " + timeElapsed + "ms.");
    }

    private static double calculatePi(long inCircle) {
        return ((double) inCircle / totalPoints) * 4;
    }
}
