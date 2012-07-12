package org.genericspatialdao.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.genericspatialdao.exceptions.SpatialException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public class SpatialUtils {

	private static final String INVALID_GEOMETRY = "Invalid geometry: ";
	private static final String ERROR = "Error: ";
	private static final String RESULT = "Result: ";

	private static final Logger LOG = Logger.getLogger(SpatialUtils.class);

	public static Point createPoint(String wktPoint, int srid) {
		LOG.debug("Creating point from wkt " + wktPoint + " and SRID " + srid);
		try {
			Point point = (Point) new WKTReader().read(wktPoint);
			if (point != null) {
				point.setSRID(srid);
			}
			checkGeometry(point);
			LOG.debug(RESULT + point);
			return point;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage());
			throw new SpatialException(ERROR + e.getMessage());
		}
	}

	public static Point createPoint(Coordinate coordinate, int srid) {
		LOG.debug("Creating point from coordinate " + coordinate + " and SRID "
				+ srid);
		Point point = new GeometryFactory().createPoint(coordinate);
		if (point != null) {
			point.setSRID(srid);
		}
		checkGeometry(point);
		LOG.debug(RESULT + point);
		return point;
	}

	public static Point createPoint(double x, double y, int srid) {
		return createPoint(createCoordinate(x, y), srid);
	}

	public static Point generateLongLatPoint(int srid) {
		LOG.debug("Generating long/lat point with SRID " + srid);
		Coordinate coordinate = new Coordinate(randomDouble(-180, 180),
				randomDouble(-90, 90));
		Point generatedPoint = createPoint(coordinate, srid);
		LOG.debug(generatedPoint);
		return generatedPoint;
	}

	public static List<Point> generateLongLatPoints(final int number, int srid) {
		LOG.debug("Generating " + number + "long/lat points with SRID " + srid);
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

	public static LineString createLineString(String wktLineString, int srid) {
		LOG.debug("Creating linestring from wkt " + wktLineString
				+ " and SRID " + srid);
		try {
			LineString lineString = (LineString) new WKTReader()
					.read(wktLineString);
			if (lineString != null) {
				lineString.setSRID(srid);
			}
			checkGeometry(lineString);
			LOG.debug(RESULT + lineString);
			return lineString;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage());
			throw new SpatialException(ERROR + e.getMessage());
		}
	}

	public static Polygon createPolygon(String wktPolygon, int srid) {
		LOG.debug("Creating polygon from wkt " + wktPolygon + " and SRID "
				+ srid);
		try {
			Polygon polygon = (Polygon) new WKTReader().read(wktPolygon);
			if (polygon != null) {
				polygon.setSRID(srid);
			}
			checkGeometry(polygon);
			LOG.debug(RESULT + polygon);
			return polygon;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage());
			throw new SpatialException(ERROR + e.getMessage());
		}
	}

	public static MultiPolygon createMultiPolygon(String wktMultiPolygon,
			int srid) {
		LOG.debug("Creating multipolygon from wkt " + wktMultiPolygon
				+ " and SRID " + srid);
		try {
			MultiPolygon multiPolygon = (MultiPolygon) new WKTReader()
					.read(wktMultiPolygon);
			if (multiPolygon != null) {
				multiPolygon.setSRID(srid);
			}
			checkGeometry(multiPolygon);
			LOG.debug(RESULT + multiPolygon);
			return multiPolygon;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage());
			throw new SpatialException(ERROR + e.getMessage());
		}
	}

	public static Geometry createGeometry(String wktGeometry, int srid) {
		LOG.debug("Creating geometry from wkt " + wktGeometry + " and SRID "
				+ srid);
		try {
			Geometry geometry = (Geometry) new WKTReader().read(wktGeometry);
			if (geometry != null) {
				geometry.setSRID(srid);
			}
			checkGeometry(geometry);
			LOG.debug(RESULT + geometry);
			return geometry;
		} catch (Exception e) {
			LOG.error(ERROR + e.getMessage());
			throw new SpatialException(ERROR + e.getMessage());
		}
	}

	private static void checkGeometry(Geometry geometry) {
		if (geometry == null || geometry.isEmpty() || !geometry.isValid()) {
			LOG.error(INVALID_GEOMETRY + geometry);
			throw new SpatialException(INVALID_GEOMETRY + geometry);
		}
	}

	private static double randomDouble(double low, double high) {
		return (double) (Math.min(low, high) + Math.random() * (high - low));
	}

}
