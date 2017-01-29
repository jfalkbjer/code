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

package com.jifa.clnr.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jifa.clnr.cache.BankNameCache;
import com.jifa.clnr.model.BankName;
import com.jifa.clnr.model.ClearingNumbers;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@RestController
public class ClearingNumberRestController {

	@Autowired
	BankNameCache bankNameCache;

	@RequestMapping(value = "/numbers")
	public ResponseEntity<ClearingNumbers> getClearingNumbers(HttpEntity<String> httpEntity) {
		log.info("Request headers: {}", httpEntity.getHeaders());

		List<String> numbers = new ArrayList<String>(bankNameCache.getClearingNumbers());
		Collections.sort(numbers);

		return ResponseEntity.ok(new ClearingNumbers(numbers));
	}

	/**
	 * Get bank name based on clearing number. Will return 404 Not found, if no
	 * bank name is associated with the clearing number.
	 * 
	 * @param httpEntity
	 * @param clearingNumber String with a clearing number.
	 * @return {@link ResponseEntity} of type {@link BankName}
	 */
	@RequestMapping(value = "/numbers/{clearingNumber}")
	public ResponseEntity<BankName> getBankName(HttpEntity<String> httpEntity,
			@PathVariable(value = "clearingNumber") String clearingNumber) {
		log.info("Request headers: {}", httpEntity.getHeaders());

		return bankNameCache.contains(clearingNumber) ? ResponseEntity.ok(bankNameCache.getBankName(clearingNumber))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
}
