package br.com.fepweb.exception;

public class InfraException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InfraException(String message, Throwable cause) {
        super(message,cause);
    }

    public InfraException(String message) {
        super(message);
    }
}