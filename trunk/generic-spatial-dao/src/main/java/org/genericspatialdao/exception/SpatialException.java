package org.genericspatialdao.exception;

public class SpatialException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4686446926370218398L;

	public SpatialException(String message) {
		super(message);
	}

	public SpatialException(Exception e) {
		super(e);
	}

	public SpatialException(String message, Exception e) {
		super(message, e);
	}

}
