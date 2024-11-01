package pimontecarlo;

public class CircleSimulator {
    private static final long totalPoints = 500_000_000;

    public static void main(String args[]){
        Instant start = Instant.now();
        System.out.println("Pi is " + approximatePi());
        Instant end = Instant.now();

        long timeElapsed = Duration.between(start, end).toMillis();

        System.out.println("Time taken: " + timeElapsed + "ms.");
    }

    private static double approximatePi() {
        long inCircle = 0;

        for (long i = 0; i < totalPoints; i++) {
            // These multiplications should take the same amount of time as using the bounds
            // 0 and 2, but this way I don't have to calculate the difference between points
            // and the circle center, since it is just (0, 0).
            double x = ThreadLocalRandom.current().nextDouble(-1, 1);
            double y = ThreadLocalRandom.current().nextDouble(-1, 1);

            if (pointInCircle(x, y)) {
                inCircle++;
            }
        }

        return calculatePi(inCircle);
    }

    private static boolean pointInCircle(double x, double y) {
        return Math.sqrt(x*x + y*y) <= 1;
    }
}
