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

package com.jifa.clnr.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jifa.clnr.cache.BankNameCache;

/**
 * Configure cache beans.
 */
@Configuration
public class CacheConfiguration {

	/**
	 * Create a {@link BankNameCache} instance. A clearingNumbers property needs
	 * to be specified in application.properties to load the cache with the
	 * configured values. Clearing numbers are configured as single value or as
	 * a range with a bank name associated to the key, example:
	 * 
	 * <pre>
	 *   '1100,1199':'Nordea'
	 * </pre>
	 * 
	 * @param clearingNumbers {@link Map}. See application.properties for
	 *            syntax, regarding the key.
	 * @return {@link BankNameCache} instance.
	 */
	@Bean
	BankNameCache bankNameCache(@Value("#{${clearingNumbers}}") Map<String, String> clearingNumbers) {
		return new BankNameCache(clearingNumbers);
	}
}
