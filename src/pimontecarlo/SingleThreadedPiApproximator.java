package pimontecarlo;

public class SingleThreadedPiApproximator extends PiApproximator{
    private static final long totalPoints = 2_000_000_000L;

    public static void main(String[] args) {
        SingleThreadedPiApproximator approximator = new SingleThreadedPiApproximator();

        approximator.approximatePi();
    }

    public SingleThreadedPiApproximator() {
        super(totalPoints);
    }

    @Override
    protected long countAllPointsInside() {
        return simulateCircle(totalPoints);
    }
}
