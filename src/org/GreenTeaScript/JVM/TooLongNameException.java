package org.GreenTeaScript.JVM;

public class TooLongNameException extends Exception {
	private static final long serialVersionUID = 1L;

	public TooLongNameException(String message) {
		super(message);
	}
}