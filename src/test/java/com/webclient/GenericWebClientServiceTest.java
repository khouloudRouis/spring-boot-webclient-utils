package com.webclient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import com.webclient.service.GenericWebClientService;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.io.IOException;
import java.time.Duration;

@ExtendWith(MockitoExtension.class)
public class GenericWebClientServiceTest {

	private GenericWebClientService service;
	WebClient.Builder builder = WebClient.builder();

	private MockWebServer mockWebServer;

	@BeforeEach
	public void setup() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		service = new GenericWebClientService(builder, mockWebServer.url("/").toString());
	}

	@AfterEach
	void tearDown() throws Exception {
		mockWebServer.shutdown();
 
	}

	@Test
	public void gett_success_httpResponse() {
		mockWebServer.enqueue(
				new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json").setBody("OK"));

		StepVerifier.create(this.service.get("/any", null, String.class)).expectNext("OK").verifyComplete();
	}

	@Test
	public void post_error_httpResponse() {

		mockWebServer
				.enqueue(new MockResponse().setResponseCode(500).setBody("{\"message\":\"Internal Server Error\"}"));
		Mono<String> result = service.post("/test", "body", String.class);

		StepVerifier.create(result)
				.expectErrorMatches(
						throwable -> throwable instanceof RuntimeException && throwable.getMessage().contains("500"))
				.verify();

	}

	@Test
	public void post_timeout_httpResponse() {

		mockWebServer.enqueue(new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
				.setBody("delayed").setBodyDelay(10, java.util.concurrent.TimeUnit.SECONDS));

		StepVerifier.create(service.post("/test", "body", String.class)).expectSubscription()
				.expectTimeout(Duration.ofSeconds(10)).verify();
	}

}
