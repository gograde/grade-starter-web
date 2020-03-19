package org.grade.starters.webutils.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "topad.webutils")
public class TopadWebProperties {
	
	private SwaggerProperties swagger;
	private ExceptionHandler exceptionHandler;
	
	public SwaggerProperties getSwagger() {
		return swagger;
	}
	public void setSwagger(SwaggerProperties swagger) {
		this.swagger = swagger;
	}
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}

class SwaggerProperties {
	private boolean enabled;
	private String redirectUrl;
	private String basePackage;

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getBasePackage() {
		return basePackage;
	}
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	
}

class ExceptionHandler{
	private boolean enabled;
	private boolean handleInternalServerErrors;
	private ErrorLabels messages;
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isHandleInternalServerErrors() {
		return handleInternalServerErrors;
	}
	public void setHandleInternalServerErrors(boolean handleInternalServerErrors) {
		this.handleInternalServerErrors = handleInternalServerErrors;
	}
	public ErrorLabels getMessages() {
		return messages;
	}
	public void setMessages(ErrorLabels messages) {
		this.messages = messages;
	}
}

class ErrorLabels {
	private String invalidBody;
	private String invalidInput;
	private String typeMismatch;
	private String generalError;
	private String notFoundError;
	private String authenticationError;
	private String dataCountError;
	
	public String getNotFoundError() {
		return notFoundError;
	}
	public void setNotFoundError(String notFoundError) {
		this.notFoundError = notFoundError;
	}
	public String getAuthenticationError() {
		return authenticationError;
	}
	public void setAuthenticationError(String authenticationError) {
		this.authenticationError = authenticationError;
	}
	public String getDataCountError() {
		return dataCountError;
	}
	public void setDataCountError(String dataCountError) {
		this.dataCountError = dataCountError;
	}
	public String getGeneralError() {
		return generalError;
	}
	public void setGeneralError(String generalError) {
		this.generalError = generalError;
	}
	public String getInvalidBody() {
		return invalidBody;
	}
	public void setInvalidBody(String invalidBody) {
		this.invalidBody = invalidBody;
	}
	public String getInvalidInput() {
		return invalidInput;
	}
	public void setInvalidInput(String invalidInput) {
		this.invalidInput = invalidInput;
	}
	public String getTypeMismatch() {
		return typeMismatch;
	}
	public void setTypeMismatch(String typeMismatch) {
		this.typeMismatch = typeMismatch;
	}
	
}
