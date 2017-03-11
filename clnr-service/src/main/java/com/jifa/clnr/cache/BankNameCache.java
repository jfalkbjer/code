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

package com.jifa.clnr.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.jifa.clnr.model.BankName;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Cache that keeps a clearing number mapped to a {@link BankName}.
 */
@Slf4j
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class BankNameCache {

	private final Map<String, BankName> map = new HashMap<>();

	/**
	 * Create cache. The key contains a single value or a range of values. For
	 * example:
	 * 
	 * <pre>
	 * Key           Value
	 * '9950'      : 'Nykredit'
	 * '9960-9969' : 'Nordea'
	 * </pre>
	 * 
	 * @param clearingNumbers {@link Map}
	 */
	public BankNameCache(Map<String, String> clearingNumbers) {
		clearingNumbers.entrySet().stream().forEach(entry -> createCache(entry.getKey(), entry.getValue()));
	}

	public Set<String> getClearingNumbers() {
		return map.keySet();
	}

	/**
	 * Check if the specified clearing number is in the cache.
	 * 
	 * @param clearingNumber {@link String} with the clearing number.
	 * @return boolean true if clearing number is in the cache, otherwise false.
	 */
	public boolean contains(String clearingNumber) {
		return map.containsKey(clearingNumber);
	}

	/**
	 * Get {@link BankName} instance from cache for the specified clearing
	 * number. Will return null if not in cache.
	 * 
	 * @param clearingNumber {@link String} with the clearing number.
	 * @return {@link BankName} instance, or null if not in cache.
	 */
	public BankName getBankName(String clearingNumber) {
		return map.get(clearingNumber);
	}

	private void createCache(String range, String bankName) {
		log.info("Setting up {} with range {}", bankName, range);

		BankName bankNameInstance = new BankName(bankName);

		String[] rangeValues = range.split("-");

		if (rangeValues.length == 1) {
			map.put(rangeValues[0], bankNameInstance);
		} else if (rangeValues.length == 2) {
			int from = Integer.valueOf(rangeValues[0]).intValue();
			int to = Integer.valueOf(rangeValues[1]).intValue() + 1;

			IntStream.range(from, to).forEach(i -> map.put(String.valueOf(i), bankNameInstance));
		}
	}

}
