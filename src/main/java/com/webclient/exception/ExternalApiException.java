package com.webclient.exception;
import org.springframework.http.HttpStatusCode;

public class ExternalApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final HttpStatusCode statusCode;
    private final String responseBody;

    public ExternalApiException(HttpStatusCode statusCode, String responseBody) {
        super("External API error: " + statusCode + " - " + (responseBody != null ? responseBody : ""));
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
