package org.GreenTeaScript.JVM;

public class NetworkTimeoutException extends Exception {
	private static final long serialVersionUID = 1L;

	public NetworkTimeoutException(String message) {
		super(message);
	}
}