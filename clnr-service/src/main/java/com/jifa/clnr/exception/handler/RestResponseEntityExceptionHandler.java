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

package com.jifa.clnr.exception.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jifa.clnr.model.Error;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private SpanAccessor spanAccessor;

	@ExceptionHandler(value = { IllegalArgumentException.class, Exception.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest webRequest) {
		Span span = spanAccessor.getCurrentSpan();
		HttpStatus httpStatus = getHttpStatus(exception);

		Error error = new Error(Span.idToHex(span.getTraceId()), httpStatus.value(), exception.getMessage());
		return handleExceptionInternal(exception, error, new HttpHeaders(), httpStatus, webRequest);
	}

	private HttpStatus getHttpStatus(Exception exception) {
		if (exception instanceof IllegalArgumentException) {
			return HttpStatus.NOT_FOUND;
		}

		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
