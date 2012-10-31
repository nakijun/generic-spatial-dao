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

	private static final long serialVersionUID = -1791595461594359407L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Type(type = "org.hibernate.spatial.GeometryType")
	private Point point;

	@Type(type = "org.hibernate.spatial.GeometryType")
	private Polygon polygon;

	@Type(type = "org.hibernate.spatial.GeometryType")
	private MultiPolygon multiPolygon;

	@Type(type = "org.hibernate.spatial.GeometryType")
	private Geometry geometry;

	public SpatialTestVO() {

	}

	public SpatialTestVO(Point point) {
		this.point = point;
	}

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

	@Override
	public String toString() {
		return "SpatialTestVO [id=" + id + ", point=" + point + ", polygon="
				+ polygon + ", multiPolygon=" + multiPolygon + ", geometry="
				+ geometry + "]";
	}

}
