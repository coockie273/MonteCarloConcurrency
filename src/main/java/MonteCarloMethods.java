import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import java.awt.geom.Point2D;
import java.util.concurrent.atomic.DoubleAdder;

import static java.lang.Math.abs;


public class MonteCarloMethods {
    // Function calculates area of the convex shape (set by a set of points > 2), which is inscribed in a square (p1, p2)
    public static double Area(List<Point2D.Double> points, Point2D.Double p1, Point2D.Double p2) {

        if (points.size() < 3) {
            System.out.println("The minimum number of points is 3");
            return -1;
        }

        if (! isShapeInscribedInSquare(points, p1, p2) ) {
            System.out.println("Shape is not inscribed in a square");
            return -1;
        }

        int numPoints = 1000000;
        int numThreads = Runtime.getRuntime().availableProcessors();

        AtomicInteger pointsInsideCircle = new AtomicInteger(0);

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                int localPointsInsideCircle = 0;
                for (int j = 0; j < numPoints / numThreads; j++) {
                    Point2D.Double p = new Point2D.Double();
                    double p1x = Math.min(p1.x, p2.x);
                    double p2x = Math.max(p1.x, p2.x);
                    p.x = ThreadLocalRandom.current().nextDouble(p1x, p2x);
                    double p1y = Math.min(p1.y, p2.y);
                    double p2y = Math.max(p1.y, p2.y);
                    p.y = ThreadLocalRandom.current().nextDouble(p1y, p2y);
                    if (isPointInsidePolygon(p, points)) {
                        localPointsInsideCircle++;
                    }
                }
                pointsInsideCircle.addAndGet(localPointsInsideCircle);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double estimatedArea = abs(p1.x - p2.x) * abs(p1.x - p2.x) * (double)pointsInsideCircle.get() / numPoints;
        return estimatedArea;
    }

    public static double Integration(Function f, double a, double b) {

        boolean opposite = a > b;
        int numSamples = 1000000;
        int numThreads = Runtime.getRuntime().availableProcessors();

        DoubleAdder totalSum = new DoubleAdder();

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                double localSum = 0;
                for (int j = 0; j < numSamples / numThreads; j++) {
                    double x = 0;
                    if (opposite) {
                        x = ThreadLocalRandom.current().nextDouble(b, a);
                    } else {
                        x = ThreadLocalRandom.current().nextDouble(a, b);
                    }
                    localSum += f.apply(x);
                }
                totalSum.add(localSum);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double result = totalSum.sum() / numSamples * abs(a - b);
        if (opposite) result *= -1;
        return result;
    }

    private static boolean isPointInsidePolygon(Point2D.Double point, List<Point2D.Double> polygon) {
        int numVertices = polygon.size();
        int count = 0;

        for (int i = 0, j = numVertices - 1; i < numVertices; j = i++) {
            if ((polygon.get(i).y > point.y) != (polygon.get(j).y > point.y) &&
                    (point.x < (polygon.get(j).x - polygon.get(i).x) * (point.y - polygon.get(i).y)
                            / (polygon.get(j).y - polygon.get(i).y) + polygon.get(i).x)) {
                count++;
            }
        }
        return count % 2 != 0;
    }

    private static boolean isShapeInscribedInSquare(List<Point2D.Double> points, Point2D.Double squareVertex1, Point2D.Double squareVertex2) {
        double minX = Math.min(squareVertex1.x, squareVertex2.x);
        double maxX = Math.max(squareVertex1.x, squareVertex2.x);
        double minY = Math.min(squareVertex1.y, squareVertex2.y);
        double maxY = Math.max(squareVertex1.y, squareVertex2.y);

        for (Point2D.Double point : points) {
            if (point.x < minX || point.x > maxX || point.y < minY || point.y > maxY) {
                return false;
            }
        }

        return true;
    }

    private static int orientation(Point2D.Double p, Point2D.Double q, Point2D.Double r) {
        double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }
}