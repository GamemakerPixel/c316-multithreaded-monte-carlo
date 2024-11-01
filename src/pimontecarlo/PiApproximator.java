package pimontecarlo;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public abstract class PiApproximator {
    private final int totalPoints;

    public PiApproximator(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void approximatePi() {
        Instant start = Instant.now();
        int circlePoints = countAllPointsInside();
        double pi = calculatePi(circlePoints);
        System.out.println("Pi is " + pi);
        Instant end = Instant.now();

        long timeElapsed = Duration.between(start, end).toMillis();

        System.out.println("Time taken: " + timeElapsed + "ms.");
    }

    abstract int countAllPointsInside();

    private static int simulateCircle(int pointCount) {
        int countInCircle = 0;

        for (int pointIndex = 0; pointIndex < pointCount; pointIndex++) {
            double x = ThreadLocalRandom.current().nextDouble(-1, 1);
            double y = ThreadLocalRandom.current().nextDouble(-1, 1);

            if (pointInCircle(x, y)) {
                countInCircle++;
            }
        }
    }

    private static boolean pointInCircle(double x, double y) {
        return Math.sqrt(x*x + y*y) <= 1;
    }

    private double calculatePi(long inCircle) {
        return ((double) inCircle / totalPoints) * 4;
    }

}
}
