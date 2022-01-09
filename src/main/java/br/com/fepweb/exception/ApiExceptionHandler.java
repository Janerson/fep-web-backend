package br.com.fepweb.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + " : " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ErrorResponse apiError =
                new ErrorResponse(HttpStatus.PRECONDITION_FAILED.value(), ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(
                ex, apiError, headers, HttpStatus.PRECONDITION_FAILED, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        ErrorResponse apiError =
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    //@ExceptionHandler({ConstraintViolationException.class})
    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            Exception ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        Throwable cause = ((TransactionSystemException) ex).getRootCause();
        if (cause instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> violation : ((ConstraintViolationException) cause).getConstraintViolations()) {
                errors.add(violation.getMessage());
            }
        }

        ErrorResponse apiError = new ErrorResponse(HttpStatus.PRECONDITION_FAILED.value(), ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        ErrorResponse apiError = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are: ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        ErrorResponse apiError = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        ErrorResponse apiError = new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        if (ex.getCause() instanceof JsonMappingException) {
            return handleConverterErrors((JsonMappingException) ex.getCause(), request);
        }

        ErrorResponse apiError = new ErrorResponse(HttpStatus.PRECONDITION_FAILED.value(),
                ex.getLocalizedMessage(), ex.getCause().getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<Object> handleConverterErrors(JsonMappingException ex, WebRequest request) {
        List<String> list = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .map(s -> s + ": " + ex.getCause())
                .collect(Collectors.toList());
        ErrorResponse apiError = new ErrorResponse(HttpStatus.PRECONDITION_FAILED.value(), ex.getLocalizedMessage(), list);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> noSuchElement(NoSuchElementException ex, WebRequest request) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({InfraException.class, MultipartException.class})
    public ResponseEntity<Object> alreadyExists(Exception ex, WebRequest request) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.PRECONDITION_FAILED.value(), ex.getMessage(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED);

    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<Object> nullPoint(NoSuchElementException ex, WebRequest request) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

    }



}
