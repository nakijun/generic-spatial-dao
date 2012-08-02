package org.genericspatialdao.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.genericspatialdao.exceptions.SpatialException;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
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

	@Test
	public void createLineStringFromWKTTest() {
		LineString ls = SpatialUtils.createLineString("LINESTRING(4 6,7 10))",
				SRID);
		assertEquals(false, ls.isEmpty());
		assertEquals(true, ls.isValid());
		assertEquals(SRID, ls.getSRID());
	}

	@Test(expected = SpatialException.class)
	public void createLineStringFromWKTWrongTest() {
		LineString ls = SpatialUtils.createLineString("POINT(5 10)", SRID);
		assertEquals(false, ls.isEmpty());
		assertEquals(true, ls.isValid());
		assertEquals(SRID, ls.getSRID());
	}

	@Test
	public void createMultipolygonFromWKTTest() {
		MultiPolygon mp = SpatialUtils
				.createMultiPolygon(
						"MULTIPOLYGON ("
								+ "((-47.200183869063 -22.886767385861, -47.197780609786 -22.886681555173, -47.197780609786 -22.889986036679, -47.200183869063 -22.889986036679, -47.200183869063 -22.886767385861)),"
								+ "((-47.195119858443 -22.886767385861, -47.192029953658 -22.886767385861, -47.192029953658 -22.889943121335, -47.195119858443 -22.889943121335, -47.195119858443 -22.886767385861)),"
								+ "((-47.186236382186 -22.886767385861, -47.189712525069 -22.886767385861, -47.189755440413 -22.889986036679, -47.186150551497 -22.890028952023, -47.186236382186 -22.886767385861)),"
								+ "((-47.200183869063 -22.892475126645, -47.19782352513 -22.892432211301, -47.197780609786 -22.895393370053, -47.200226784407 -22.895393370053, -47.200183869063 -22.892475126645)),"
								+ "((-47.195162773787 -22.892475126645, -47.192072869002 -22.892432211301, -47.192072869002 -22.895436285397, -47.195205689131 -22.895479200742, -47.195162773787 -22.892475126645)),"
								+ "((-47.18962669438 -22.892518041989, -47.186150551497 -22.892475126645, -47.186107636153 -22.895436285397, -47.189669609725 -22.895436285397, -47.18962669438 -22.892518041989))"
								+ ")", SRID);
		assertEquals(false, mp.isEmpty());
		assertEquals(true, mp.isValid());
		assertEquals(SRID, mp.getSRID());
	}

	@Test
	public void createGeometryFromWKTTest() {
		Geometry g = SpatialUtils
				.createGeometry(
						"POLYGON((0 0,4 0,4 4,0 4,0 0),(1 1, 2 1, 2 2, 1 2,1 1))",
						SRID);
		assertEquals(false, g.isEmpty());
		assertEquals(true, g.isValid());
		assertEquals(SRID, g.getSRID());
	}

	@Test(expected = SpatialException.class)
	public void createPolygonFromWKTWrongTest() {
		Polygon p = SpatialUtils.createPolygon("POINT(0 0)", SRID);
		assertEquals(false, p.isEmpty());
		assertEquals(true, p.isValid());
		assertEquals(SRID, p.getSRID());
	}

	@Test
	public void generateLongLatPointTest() {
		Point p = SpatialUtils.generateLongLatPoint(SRID);
		assertEquals(false, p.isEmpty());
		assertEquals(true, p.isValid());
		assertEquals(SRID, p.getSRID());
	}

	@Test
	public void generateLongLatPointsTest() {
		List<Point> list = SpatialUtils.generateLongLatPoints(10, SRID);
		for (Point p : list) {
			assertEquals(false, p.isEmpty());
			assertEquals(true, p.isValid());
			assertEquals(SRID, p.getSRID());
		}
	}

	@Test
	public void changeScaleTest() {
		double factor = 2;
		Polygon polygon = SpatialUtils.createPolygon(
				"POLYGON ((0 0, 0 3, 3 3, 3 0, 0 0))", SRID);
		Polygon expectedPolygon = SpatialUtils.createPolygon(
				"POLYGON ((0 0, 0 6, 6 6, 6 0, 0 0))", SRID);
		Point point = SpatialUtils.createPoint("POINT (4 2)", SRID);
		Point expectedPoint = SpatialUtils.createPoint("POINT (8 4)", SRID);
		assertEquals(expectedPolygon, SpatialUtils.changeScale(polygon, factor));
		assertEquals(expectedPoint, SpatialUtils.changeScale(point, factor));
		// test clone method
		assertEquals(SpatialUtils.createPolygon(
				"POLYGON ((0 0, 0 3, 3 3, 3 0, 0 0))", SRID), polygon);
	}

	@Test
	public void changeScaleCentroidBasedTest() {
		double factor = 2;
		Polygon polygon = SpatialUtils.createPolygon(
				"POLYGON ((0 0, 0 3, 3 3, 3 0, 0 0))", SRID);
		Polygon expectedPolygon = SpatialUtils
				.createPolygon(
						"POLYGON ((-1.5 -1.5, -1.5 4.5, 4.5 4.5, 4.5 -1.5, -1.5 -1.5))",
						SRID);
		Point point = SpatialUtils.createPoint("POINT (4 2)", SRID);
		Point expectedPoint = SpatialUtils.createPoint("POINT (4 2)", SRID);
		assertEquals(expectedPolygon,
				SpatialUtils.changeScaleCentroidBased(polygon, factor));
		assertEquals(expectedPoint,
				SpatialUtils.changeScaleCentroidBased(point, factor));
		// test clone method
		assertEquals(SpatialUtils.createPolygon(
				"POLYGON ((0 0, 0 3, 3 3, 3 0, 0 0))", SRID), polygon);
	}

	@Test
	public void bugJTSTest() {
		Geometry envelope = SpatialUtils
				.createMultiPolygon(
						"MULTIPOLYGON (((-47.200183869063 -22.886767385861, -47.197780609786 -22.886681555173, -47.197780609786 -22.889986036679, -47.200183869063 -22.889986036679, -47.200183869063 -22.886767385861)), ((-47.195119858443 -22.886767385861, -47.192029953658 -22.886767385861, -47.192029953658 -22.889943121335, -47.195119858443 -22.889943121335, -47.195119858443 -22.886767385861)), ((-47.186236382186 -22.886767385861, -47.189712525069 -22.886767385861, -47.189755440413 -22.889986036679, -47.186150551497 -22.890028952023, -47.186236382186 -22.886767385861)), ((-47.200183869063 -22.892475126645, -47.19782352513 -22.892432211301, -47.197780609786 -22.895393370053, -47.200226784407 -22.895393370053, -47.200183869063 -22.892475126645)), ((-47.195162773787 -22.892475126645, -47.192072869002 -22.892432211301, -47.192072869002 -22.895436285397, -47.195205689131 -22.895479200742, -47.195162773787 -22.892475126645)), ((-47.18962669438 -22.892518041989, -47.186150551497 -22.892475126645, -47.186107636153 -22.895436285397, -47.189669609725 -22.895436285397, -47.18962669438 -22.892518041989)))",
						SRID).getEnvelope();
		SpatialUtils.changeScale(envelope, 0.4);
	}
}
