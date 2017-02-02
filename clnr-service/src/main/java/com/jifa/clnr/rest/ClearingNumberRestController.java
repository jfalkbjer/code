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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jifa.clnr.cache.BankNameCache;
import com.jifa.clnr.model.BankName;
import com.jifa.clnr.model.ClearingNumbers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 */
@Slf4j
@RestController
public class ClearingNumberRestController {

	@Autowired
	BankNameCache bankNameCache;

	@ApiOperation(value = "Get all available clearing numbers.", notes = "bla bla bla")
	@RequestMapping(method = RequestMethod.GET, value = "/numbers")
	public ClearingNumbers getClearingNumbers(@ApiIgnore HttpEntity<String> httpEntity) {
		log.info("Request headers: {}", httpEntity.getHeaders());

		List<String> numbers = new ArrayList<String>(bankNameCache.getClearingNumbers());
		Collections.sort(numbers);

		return new ClearingNumbers(numbers);
	}

	/**
	 * Get bank name based on clearing number. Will return 404 Not found, if no
	 * bank name is associated with the clearing number.
	 * 
	 * @param clearingNumber String with a clearing number.
	 * @return {@link BankName}
	 */
	@ApiOperation(value = "Get bank name for the specified clearing number.", notes = "bla bla bla")
	@RequestMapping(method = RequestMethod.GET, value = "/numbers/{clearing-number}")
	public BankName getBankName(@ApiIgnore HttpEntity<String> httpEntity,
			@ApiParam(value = "Clearing number to get bank name for.") @PathVariable(value = "clearing-number") String clearingNumber) {
		log.info("Request headers: {}", httpEntity.getHeaders());

		if (bankNameCache.contains(clearingNumber)) {
			return bankNameCache.getBankName(clearingNumber);
		}

		throw new IllegalArgumentException("No bank name found for specified clearing number.");
	}
}
