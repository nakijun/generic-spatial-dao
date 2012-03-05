package org.genericspatialdao.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.genericspatialdao.exception.SpatialException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class SpatialUtils {

	private static final String INVALID_POINT = "Invalid point: ";
	private static final String INVALID_POLYGON = "Invalid polygon: ";
	private static final String ERROR = "Error: ";
	private static final String RESULT = "Result: ";

	private static final Logger LOG = Logger.getLogger(SpatialUtils.class);

	public static Point createPoint(String wktPoint, int srid) {
		LOG.info("Creating point from wkt " + wktPoint + " and SRID " + srid);
		try {
			Point point = (Point) new WKTReader().read(wktPoint);
			if (point != null) {
				point.setSRID(srid);
			}
			checkPoint(point);
			LOG.debug(RESULT + point);
			return point;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage());
			throw new SpatialException(ERROR + e.getMessage());
		}
	}

	public static Point createPoint(Coordinate coordinate, int srid) {
		LOG.info("Creating point from coordinate " + coordinate + " and SRID "
				+ srid);
		Point point = new GeometryFactory().createPoint(coordinate);
		if (point != null) {
			point.setSRID(srid);
		}
		checkPoint(point);
		LOG.debug(RESULT + point);
		return point;
	}

	public static Point createPoint(double x, double y, int srid) {
		return createPoint(createCoordinate(x, y), srid);
	}

	public static Point generateLongLatPoint(int srid) {
		LOG.info("Generating long/lat point with SRID " + srid);
		Coordinate coordinate = new Coordinate(randomDouble(-180, 180),
				randomDouble(-90, 90));
		Point generatedPoint = createPoint(coordinate, srid);
		LOG.debug(generatedPoint);
		return generatedPoint;
	}

	public static List<Point> generateLongLatPoints(final int number, int srid) {
		LOG.info("Generating " + number + "long/lat points with SRID " + srid);
		List<Point> list = new ArrayList<Point>();
		Coordinate[] coordinates = new Coordinate[number];
		for (int i = 0; i < number; i++) {
			coordinates[i] = new Coordinate(randomDouble(-180, 180),
					randomDouble(-90, 90));
			Point point = createPoint(coordinates[i], srid);
			list.add(point);
		}
		LOG.debug(list);
		return list;
	}

	public static Coordinate createCoordinate(double x, double y) {
		return new Coordinate(x, y);
	}

	public static Polygon createPolygon(String wktPolygon, int srid) {
		LOG.info("Creating polygon from wkt " + wktPolygon + " and SRID "
				+ srid);
		try {
			Polygon polygon = (Polygon) new WKTReader().read(wktPolygon);
			if (polygon != null) {
				polygon.setSRID(srid);
			}
			checkPolygon(polygon);
			LOG.debug(RESULT + polygon);
			return polygon;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage());
			throw new SpatialException(ERROR + e.getMessage());
		}
	}

	private static void checkPoint(Point point) {
		if (point == null || point.isEmpty() || !point.isValid()) {
			LOG.error(INVALID_POINT + point);
			throw new SpatialException(INVALID_POINT + point);
		}
	}

	private static void checkPolygon(Polygon polygon) {
		if (polygon == null || polygon.isEmpty() || !polygon.isValid()) {
			LOG.error(INVALID_POLYGON + polygon);
			throw new SpatialException(INVALID_POLYGON + polygon);
		}
	}

	private static double randomDouble(double low, double high) {
		return (double) (Math.min(low, high) + Math.random() * (high - low));
	}

}
