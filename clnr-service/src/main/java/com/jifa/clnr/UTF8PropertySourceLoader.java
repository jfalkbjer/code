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

package com.jifa.clnr;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

/**
 * Read property files with UTF-8.
 * 
 * Note that for this class to take effect, you need to configure this class in
 * the META-INF/spring.factories file to register this implementation of a
 * PropertySourceLoader.
 */
public class UTF8PropertySourceLoader implements PropertySourceLoader {

	@Override
	public String[] getFileExtensions() {
		return new String[] { "properties" };
	}

	@Override
	public PropertySource<?> load(String name, Resource resource, String profile) throws IOException {
		if (profile == null) {
			Properties properties = new Properties();
			properties.load(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

			if (!properties.isEmpty()) {
				return new PropertiesPropertySource(name, properties);
			}
		}

		return null;
	}

}
