import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonteCarloTests {

    private static final double deltaArea = 0.1;
    private static final double deltaIntegration = 0.2;

    @Test
    public void areaTests() {
        // point
        assertEquals(MonteCarloMethods.Area(List.of(
                new Point2D.Double(0, 1)
        ), new Point2D.Double(5,5),  new Point2D.Double(-5,-5)),-1, deltaArea);

        // line
        assertEquals(MonteCarloMethods.Area(List.of(
                new Point2D.Double(0, 1),
                new Point2D.Double(1, 0)
        ), new Point2D.Double(5,5),  new Point2D.Double(-5,-5)),-1, deltaArea);

        // triangle
        assertEquals(MonteCarloMethods.Area(List.of(
                new Point2D.Double(0, 1),
                new Point2D.Double(3, 0),
                new Point2D.Double(0, -4)
        ), new Point2D.Double(5,5),  new Point2D.Double(-5,-5)),7.5, deltaArea);

        // square
        assertEquals(MonteCarloMethods.Area(List.of(
                new Point2D.Double(0, 1),
                new Point2D.Double(1, 0),
                new Point2D.Double(0, -1),
                new Point2D.Double(-1, 0)
        ), new Point2D.Double(5,5),  new Point2D.Double(-5,-5)),2, deltaArea);

        // Polygon #1
        assertEquals(MonteCarloMethods.Area(List.of(
                new Point2D.Double(1, 5),
                new Point2D.Double(2, 0),
                new Point2D.Double(0, -3),
                new Point2D.Double(-1, 4)
        ), new Point2D.Double(5,5),  new Point2D.Double(-5,-5)),14, deltaArea);

        // Polygon #2
        assertEquals(MonteCarloMethods.Area(List.of(
                new Point2D.Double(5, 1),
                new Point2D.Double(5, -2),
                new Point2D.Double(0, -3),
                new Point2D.Double(-2, 0),
                new Point2D.Double(-2, 4)
        ), new Point2D.Double(5,5),  new Point2D.Double(-5,-5)),33, deltaArea);

        // Polygon #3
        assertEquals(MonteCarloMethods.Area(List.of(
                new Point2D.Double(6, 1),
                new Point2D.Double(5, -2),
                new Point2D.Double(0, -3),
                new Point2D.Double(-2, 0),
                new Point2D.Double(-2, 7)
        ), new Point2D.Double(5,5),  new Point2D.Double(-5,-5)),-1, deltaArea);
    }

    @Test
    public void integrationTests() {

        Function square = x -> x * x;

        Function exp = x -> Math.exp(x) * x * x;

        Function trigonometry = x -> Math.sqrt(Math.sin(x)) / (Math.sqrt(Math.sin(x)) + Math.sqrt(Math.cos(x)));

        //square
        assertEquals(MonteCarloMethods.Integration(square, 0, 1), 0.4, deltaIntegration);
        assertEquals(MonteCarloMethods.Integration(square, -5, 5), 83.33, deltaIntegration);
        assertEquals(MonteCarloMethods.Integration(square, 5, -5), -83.33, deltaIntegration);

        //sqrt
        assertEquals(MonteCarloMethods.Integration(exp, 0, 1), 0.71, deltaIntegration);
        //assertEquals(MonteCarloMethods.Integration(exp, 1, 1), 0, delta);
        assertEquals(MonteCarloMethods.Integration(exp, -5, 1), 2.46, deltaIntegration);

        //trigonometry
        assertEquals(MonteCarloMethods.Integration(exp, 0, 1), 0.71, deltaIntegration);
        assertEquals(MonteCarloMethods.Integration(trigonometry, 0, Math.PI / 2), 0.78, deltaIntegration);

    }
}
