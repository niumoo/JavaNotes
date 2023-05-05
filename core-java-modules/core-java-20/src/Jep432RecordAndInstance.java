/**
 * @author niulang
 */
public class Jep432RecordAndInstance {
    public static void main(String[] args) {
        ColoredPoint coloredPoint1 = new ColoredPoint(new Point(0, 0), Color.RED);
        ColoredPoint coloredPoint2 = new ColoredPoint(new Point(1, 1), Color.GREEN);
        Rectangle rectangle = new Rectangle(coloredPoint1, coloredPoint2);
        printUpperLeftColoredPoint(rectangle);
    }

    static void printUpperLeftColoredPoint(Rectangle r) {
        if (r instanceof Rectangle(ColoredPoint ul, ColoredPoint lr)) {
            System.out.println(ul.c());
        }
    }
}

record Point(int x, int y) {}
enum Color { RED, GREEN, BLUE }
record ColoredPoint(Point p, Color c) {}
record Rectangle(ColoredPoint upperLeft, ColoredPoint lowerRight) {}