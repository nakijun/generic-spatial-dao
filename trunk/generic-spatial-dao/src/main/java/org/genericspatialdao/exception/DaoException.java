package org.genericspatialdao.exception;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 * 
 */
public class DaoException extends RuntimeException {

	private static final long serialVersionUID = -49185129260304863L;

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Exception e) {
		super(e);
	}

	public DaoException(String message, Exception e) {
		super(message, e);
	}
}