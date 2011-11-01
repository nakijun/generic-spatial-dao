package org.genericspatialdao.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

@Entity
public class SpatialTestVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1791595461594359407L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	private Point point;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	private Polygon polygon;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	private MultiPolygon multiPolygon;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	private Geometry geometry;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

	public MultiPolygon getMultiPolygon() {
		return multiPolygon;
	}

	public void setMultiPolygon(MultiPolygon multiPolygon) {
		this.multiPolygon = multiPolygon;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

}
