package org.genericspatialdao.utils;

import static org.junit.Assert.assertEquals;

import org.genericspatialdao.exception.SpatialException;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class SpatialUtilsTest {

	private static final int SRID = 4326;

	@Test
	public void createPointFromWKTTest() {
		double x = TestUtils.randomDouble();
		double y = TestUtils.randomDouble();
		Point p = SpatialUtils.createPoint("POINT(" + x + " " + y + ")", SRID);
		assertEquals(false, p.isEmpty());
		assertEquals(true, p.isValid());
		assertEquals(SRID, p.getSRID());
	}

	@Test(expected = SpatialException.class)
	public void createPointFromWKTWrongTest() {
		SpatialUtils.createPoint("POINT()", SRID);
	}

	@Test(expected = SpatialException.class)
	public void createPointFromWKTWrong2Test() {
		SpatialUtils.createPoint("", SRID);
	}

	@Test
	public void createPointFromCoordinateTest() {
		double x = TestUtils.randomDouble();
		double y = TestUtils.randomDouble();
		Point p = SpatialUtils.createPoint(new Coordinate(x, y), SRID);
		assertEquals(false, p.isEmpty());
		assertEquals(true, p.isValid());
		assertEquals(SRID, p.getSRID());
	}

	@Test
	public void createPointFromXAndYTest() {
		double x = TestUtils.randomDouble();
		double y = TestUtils.randomDouble();
		Point p = SpatialUtils.createPoint(x, y, SRID);
		assertEquals(false, p.isEmpty());
		assertEquals(true, p.isValid());
		assertEquals(SRID, p.getSRID());
	}

	@Test
	public void createPolygonFromWKTTest() {
		Polygon p = SpatialUtils
				.createPolygon(
						"POLYGON((0 0,4 0,4 4,0 4,0 0),(1 1, 2 1, 2 2, 1 2,1 1))",
						SRID);
		assertEquals(false, p.isEmpty());
		assertEquals(true, p.isValid());
		assertEquals(SRID, p.getSRID());
	}

	@Test(expected = SpatialException.class)
	public void createPolygonFromWKTWrongTest() {
		Polygon p = SpatialUtils.createPolygon("POINT(0 0)", SRID);
		assertEquals(false, p.isEmpty());
		assertEquals(true, p.isValid());
		assertEquals(SRID, p.getSRID());
	}
}
