import java.awt.geom.Point2D;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println(MonteCarloMethods.Area(List.of(
                new Point2D.Double(0, 1),
                new Point2D.Double(1, 0),
                new Point2D.Double(0, -1),
                new Point2D.Double(-1, 0)
        ), new Point2D.Double(5,5),  new Point2D.Double(-5,-5)));
        Function testFunction = new Function() {
            @Override
            public double apply(double x) {
                return Math.sqrt(Math.sin(x)) / (Math.sqrt(Math.sin(x)) + Math.sqrt(Math.cos(x))) ;
            }
        };
        System.out.println(MonteCarloMethods.Integration(testFunction, 0, 1.57));
    }
}
