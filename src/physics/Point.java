package physics;
import javafx.geometry.*;

public class Point {
	public static void main(String[] args) {
		Point3D point = new Point3D(1.0, 2.0, 3.0);
		System.out.println(point.toString());
		Point3D point2 = new Point3D(1.0, 3.0, 3.0);
		System.out.println(point.distance(point2));
		StdOut.print(point.getY());
		
	}

}
