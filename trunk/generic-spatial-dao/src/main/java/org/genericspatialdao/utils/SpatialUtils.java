package org.genericspatialdao.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.genericspatialdao.exception.DAOException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class SpatialUtils {

	private static final String ERROR = "Error: ";
	private static final String RESULT = "Result: ";

	private static final Logger LOG = Logger.getLogger(SpatialUtils.class);

	public static Point createPoint(String wktPoint, int srid) {
		LOG.info("Creating point from wkt " + wktPoint + " and SRID " + srid);
		try {
			Point point = (Point) new WKTReader().read(wktPoint);

			point.setSRID(srid);

			LOG.debug(RESULT + point);
			return point;
		} catch (ParseException e) {
			LOG.error(ERROR + e.getMessage());
			throw new DAOException(ERROR + e.getMessage());
		}
	}

	public static Point createPoint(Coordinate coordinate, int srid) {
		LOG.info("Creating point from coordinate " + coordinate + " and SRID "
				+ srid);
		Point point = new GeometryFactory().createPoint(coordinate);

		point.setSRID(srid);
		LOG.debug(RESULT + point);
		return point;
	}

	public static List<Point> generateLatLongPoints(final int number, int srid) {
		LOG.info("Generating " + number + "lat/long points with SRID " + srid);
		List<Point> list = new ArrayList<Point>();
		Coordinate[] coordinates = new Coordinate[number];
		for (int i = 0; i < number; i++) {
			coordinates[i] = new Coordinate(randomDouble(-90, 90),
					randomDouble(-180, 180));
			Point point = createPoint(coordinates[i], srid);
			list.add(point);
		}
		LOG.debug(list);
		return list;
	}

	public static Coordinate createCoordinate(double x, double y) {
		return new Coordinate(x, y);
	}

	private static double randomDouble(double low, double high) {
		return (double) (Math.min(low, high) + Math.random() * (high - low));
	}

}
