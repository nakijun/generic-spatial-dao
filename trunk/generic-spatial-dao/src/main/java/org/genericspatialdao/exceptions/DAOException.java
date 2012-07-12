package org.genericspatialdao.exceptions;

/**
 * 
 * @author Joao Savio C. Longo - joaosavio@gmail.com
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = -49185129260304863L;

	public DAOException(String message) {
		super(message);
	}

	public DAOException(Exception e) {
		this(e.getMessage(), e);
	}

	public DAOException(String message, Exception e) {
		super(message, e);
	}

}
