/*
 * Copyright (C) 2017 Jimmy Falkbjer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jifa.clnr.gateway.kong;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Conditional(RegisterWithKongCondition.class)
@Component
public class RegisterWithKongApplicationListener
		implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

	// KONG URL to the API for the service.
	private static final String API_URL = "http://localhost:8001/apis/{service}";

	@Value("${spring.application.name}")
	private String service;

	@Override
	public void onApplicationEvent(final EmbeddedServletContainerInitializedEvent event) {
		int port = event.getEmbeddedServletContainer().getPort();
		log.info("Register service {} with KONG using port {}", service, port);

		RequestEntity<MultiValueMap<String, String>> request = createPatchRequest(service, port);
		log.info("Request: " + request);

		ResponseEntity<String> response = createRestTemplate().exchange(request, String.class);
		log.info("Response: " + response);
	}

	private RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(createRequestFactory());
		return restTemplate;
	}

	private RequestEntity<MultiValueMap<String, String>> createPatchRequest(String service, int port) {
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("service", service);

		return RequestEntity.patch(UriComponentsBuilder.fromUriString(API_URL).buildAndExpand(pathParams).toUri())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON)
				.body(getBody(port));
	}

	private MultiValueMap<String, String> getBody(int port) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("upstream_url", "http://localhost:" + port);
		return map;
	}

	private HttpComponentsClientHttpRequestFactory createRequestFactory() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(2000);
		requestFactory.setReadTimeout(5000);
		return requestFactory;
	}
}