package org.genericspatialdao.utils;

import java.util.UUID;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

public class TestUtils {

	public static String randomString() {
		return UUID.randomUUID().toString();
	}

	public static int randomInt() {
		return (int) (Math.random() * Integer.MAX_VALUE);
	}

	public static int randomInt(int low, int high) {
		return (int) (Math.min(low, high) + Math.random() * (high - low));
	}

	public static long randomLong() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

	public static double randomDouble() {
		return (double) (Math.random() * Double.MAX_VALUE);
	}

	public static double randomDouble(double low, double high) {
		return (double) (Math.min(low, high) + Math.random() * (high - low));
	}

	public static Point randomLatLongPoint(int srid) {
		Coordinate c = SpatialUtils.createCoordinate(randomDouble(-180, 180),
				randomDouble(-90, 90));
		return SpatialUtils.createPoint(c, srid);
	}

	public static Point randomPoint(int xMin, int xMax, int yMin, int yMax,
			int srid) {
		Coordinate c = SpatialUtils.createCoordinate(randomDouble(xMin, xMax),
				randomDouble(yMin, yMax));
		return SpatialUtils.createPoint(c, srid);
	}

}
