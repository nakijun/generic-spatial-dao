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
	
	public static double randomDouble(double low, double high) {
		return (double) (Math.min(low, high) + Math.random() * (high - low));
	}

	public static Point randomLatLongPoint(int srid) {
		Coordinate c = SpatialUtils.createCoordinate(randomDouble(-90,90), randomDouble(-180,180));
		return SpatialUtils.createPoint(c, srid);
	}

}
