package com.webclient.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.webclient.exception.ExternalApiException;

import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
public class GenericWebClientService {
	private static final Logger log = LoggerFactory.getLogger(GenericWebClientService.class);
	private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

	private final WebClient webClient;

	public GenericWebClientService(WebClient.Builder builder, @Value("${api.base-url}") String baseUrl) {
		this.webClient = builder.baseUrl(baseUrl).build();
	}

	public <RES> Mono<RES> get(String path, Map<String, String> query, Class<RES> responseType) {
		log.debug("Performing GET request to {}", path);

		return webClient.get().uri(uriBuilder -> {
			uriBuilder.path(path);
			if (query != null && !query.isEmpty())
				query.forEach(uriBuilder::queryParam);
			return uriBuilder.build();
		}).retrieve()
				.onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class)
						.flatMap(body -> Mono.<Throwable>error(new ExternalApiException(resp.statusCode(), body))))
				.bodyToMono(responseType)
				.timeout(DEFAULT_TIMEOUT)
				.doOnError(e -> log.error("GET failed: path={}", path, e));
	}

	public <REQ, RES> Mono<RES> post(String path, REQ requestBody, Class<RES> responseType) {
		log.debug("Performing POST request to {}", path);

		return webClient.post().uri(path, new Object[0])
				.bodyValue(requestBody)
				.retrieve()
				.onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class)
						.flatMap(body -> Mono.<Throwable>error(new ExternalApiException(resp.statusCode(), body))))
				.bodyToMono(responseType)
				.timeout(DEFAULT_TIMEOUT)
				.doOnError(e -> log.error("POST failed: path={}", path, e));
	}

	    public <REQ, RES> Mono<RES> post(String path, REQ requestBody, ParameterizedTypeReference<RES> responseType) {
		log.debug("Performing POST request to {}", path);

		return webClient.post().uri(path, new Object[0])
			.bodyValue(requestBody)
			.retrieve()
			.onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class)
				.flatMap(body -> Mono.<Throwable>error(new ExternalApiException(resp.statusCode(), body))))
			.bodyToMono(responseType)
			.timeout(DEFAULT_TIMEOUT)
			.doOnError(e -> log.error("POST failed: path={}", path, e));
	    }

	public <RES> Mono<RES> delete(String path, Class<RES> responseType) {
		log.debug("Performing DELETE request to {}", path);

		return webClient.delete().uri(path, new Object[0])
				.retrieve()
				.onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class)
						.flatMap(body -> Mono.<Throwable>error(new ExternalApiException(resp.statusCode(), body))))
				.bodyToMono(responseType)
				.timeout(DEFAULT_TIMEOUT)
				.doOnError(e -> log.error("DELETE failed: path={}", path, e));
	}


	public <REQ, RES> Mono<RES> put(String path, REQ requestBody, Class<RES> responseType) {
		log.debug("Performing PUT request to {}", path);
		return webClient.put().uri(path, new Object[0])
				.bodyValue(requestBody)
				.retrieve()
				.onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class)
						.flatMap(body -> Mono.<Throwable>error(new ExternalApiException(resp.statusCode(), body))))
				.bodyToMono(responseType)
				.timeout(DEFAULT_TIMEOUT)
				.doOnError(e -> log.error("PUT failed: path={}", path, e));
	}


	public <REQ, RES> Mono<RES> patch(String path, REQ requestBody, Class<RES> responseType) {
		log.debug("Performing PATCH request to {}", path);
		return webClient.patch().uri(path, new Object[0])
				.bodyValue(requestBody)
				.retrieve()
				.onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class)
						.flatMap(body -> Mono.<Throwable>error(new ExternalApiException(resp.statusCode(), body))))
				.bodyToMono(responseType)
				.timeout(DEFAULT_TIMEOUT)
				.doOnError(e -> log.error("PATCH failed: path={}", path, e));
	}


}