package org.grade.starters.webutils.bean;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import fr.gouv.impots.appli.topad.core.bean.ErrorResponse;
import fr.gouv.impots.appli.topad.core.exception.BadRequestException;
import fr.gouv.impots.appli.topad.core.exception.DataCountException;
import fr.gouv.impots.appli.topad.core.exception.NotFoundException;
import fr.gouv.impots.appli.topad.core.exception.UnauthorizedException;

@ControllerAdvice
public class TopadWebExceptionHandler {

	@Autowired
	private TopadWebProperties properties;

	@ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class,
			IllegalArgumentException.class, MultipartException.class, BadRequestException.class,
			MethodArgumentTypeMismatchException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleBadRequest(Exception e) {
		return create400Response(e);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
		if (properties.getExceptionHandler() != null
				&& properties.getExceptionHandler().isHandleInternalServerErrors()) {
			ErrorResponse error = new ErrorResponse();
			error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			if (properties.getExceptionHandler().getMessages() != null
					&& properties.getExceptionHandler().getMessages().getGeneralError() != null) {
				error.setMessage(properties.getExceptionHandler().getMessages().getGeneralError().replace("${message}",
						e.getMessage()));
			} else {
				error.setMessage("General Error: " + e.getMessage());
			}
			return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			throw e;
		}
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
		ErrorResponse error = new ErrorResponse();
		error.setCode(HttpStatus.NOT_FOUND.value());
		if (properties.getExceptionHandler() != null && properties.getExceptionHandler().getMessages() != null
				&& properties.getExceptionHandler().getMessages().getNotFoundError() != null) {
			error.setMessage(properties.getExceptionHandler().getMessages().getNotFoundError().replace("${message}",
					e.getMessage()));
		} else {
			error.setMessage("Not found: " + e.getMessage());
		}
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DataCountException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleDataCountException(DataCountException e) {
		ErrorResponse error = new ErrorResponse();
		error.setCode(HttpStatus.BAD_REQUEST.value());
		if (properties.getExceptionHandler() != null && properties.getExceptionHandler().getMessages() != null
				&& properties.getExceptionHandler().getMessages().getDataCountError() != null) {
			error.setMessage(properties.getExceptionHandler().getMessages().getDataCountError().replace("${message}",
					e.getMessage()));
		} else {
			error.setMessage("Bad request: " + e.getMessage());
		}
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
		ErrorResponse error = new ErrorResponse();
		error.setCode(HttpStatus.UNAUTHORIZED.value());
		if (properties.getExceptionHandler() != null & properties.getExceptionHandler().getMessages() != null
				&& properties.getExceptionHandler().getMessages().getAuthenticationError() != null) {
			error.setMessage(properties.getExceptionHandler().getMessages().getAuthenticationError()
					.replace("${message}", e.getMessage()));
		} else {
			error.setMessage("Unauthorized: " + e.getMessage());
		}
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNAUTHORIZED);
	}

	private ResponseEntity<ErrorResponse> create400Response(Exception e) {
		ErrorResponse error = new ErrorResponse();
		error.setCode(HttpStatus.BAD_REQUEST.value());
		if (e instanceof MethodArgumentNotValidException) {
			BindingResult result = ((MethodArgumentNotValidException) e).getBindingResult();
			List<FieldError> fieldErrors = result.getFieldErrors();
			if (properties.getExceptionHandler() != null && properties.getExceptionHandler().getMessages() != null
					&& properties.getExceptionHandler().getMessages().getInvalidInput() != null) {
				error.setMessage(properties.getExceptionHandler().getMessages().getInvalidInput()
						.replace("${name}", fieldErrors.get(0).getField())
						.replace("${message}", fieldErrors.get(0).getDefaultMessage()));
			} else {
				error.setMessage("Invalid input field '" + fieldErrors.get(0).getField() + "': "
						+ fieldErrors.get(0).getDefaultMessage());
			}
		} else if (e instanceof HttpMessageNotReadableException) {
			error.setMessage(
					properties.getExceptionHandler() != null && properties.getExceptionHandler().getMessages() != null
							&& properties.getExceptionHandler().getMessages().getInvalidBody() != null
									? properties.getExceptionHandler().getMessages().getInvalidBody()
									: "Cannot parse the body contained in the request");
		} else if (e instanceof MethodArgumentTypeMismatchException) {
			MethodArgumentTypeMismatchException cast = (MethodArgumentTypeMismatchException) e;
			error.setMessage(
					properties.getExceptionHandler() != null && properties.getExceptionHandler().getMessages() != null
							&& properties.getExceptionHandler().getMessages().getTypeMismatch() != null
									? properties.getExceptionHandler().getMessages().getTypeMismatch()
											.replace("${parameterName}", cast.getParameter().getParameterName())
											.replace("${parameterType}", cast.getParameter().getClass().getSimpleName())
											.replace("${requieredType}", cast.getRequiredType().getSimpleName())
									: "Type mismatch: " + e.getMessage());
		} else {
			error.setMessage(e.getMessage());
		}
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

}
