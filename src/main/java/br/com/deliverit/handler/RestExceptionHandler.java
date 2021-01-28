package br.com.deliverit.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.deliverit.handler.exception.ExceptionDetails;
import br.com.deliverit.handler.exception.NotFoundException;
import br.com.deliverit.handler.exception.NotFoundExceptionDetails;
import br.com.deliverit.handler.exception.ValidationExceptionDetails;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<NotFoundExceptionDetails> handlerNotFoundException(NotFoundException notFoundException) {
		
		return new ResponseEntity<> (
			NotFoundExceptionDetails.builder()
			.title("Not Found Exception")
			.status(HttpStatus.NOT_FOUND.value())
			.details(notFoundException.getMessage())
			.message(notFoundException.getClass().getName())
			.time(LocalDateTime.now()).build(), HttpStatus.NOT_FOUND);
	}
	
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<FieldError> fieldErros = exception.getBindingResult().getFieldErrors();
		
		String fields = fieldErros.stream().map(FieldError::getField).collect(Collectors.joining(", "));
		String messages = fieldErros.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
		
		return new ResponseEntity<> (ValidationExceptionDetails.builder()
			.title("Bad Request Exception")
			.status(HttpStatus.BAD_REQUEST.value())
			.details("verificar campos de validação.")
			.message(exception.getClass().getName())
			.time(LocalDateTime.now())
			.fields(fields)
			.messages(messages)
			.build(), HttpStatus.BAD_REQUEST);
	}
	
	@Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
        		.title(exception.getCause().getMessage())
                .status(status.value())
                .details(exception.getMessage())
                .message(exception.getClass().getName())
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(exceptionDetails, headers, status);
    }
}