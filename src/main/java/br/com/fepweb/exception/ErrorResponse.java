package br.com.fepweb.exception;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class ErrorResponse implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3145850015412396347L;
	
	private int status;
    private int statusCode;
    private String message;
    private List<String> errors;

    public ErrorResponse(int httpStatus, String message, List<String> errors) {
        super();
        this.status = httpStatus;
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(int httpStatus, String message, String error) {
        super();
        this.status = httpStatus;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
