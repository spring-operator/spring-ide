/*******************************************************************************
 * Copyright (c) 2015, 2016 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.eclipse.boot.core.initializr;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.ide.eclipse.boot.core.BootActivator;
import org.springframework.ide.eclipse.boot.core.BootPreferences;
import org.springframework.ide.eclipse.boot.core.SimpleUriBuilder;
import org.springframework.ide.eclipse.boot.core.SpringBootStarters;
import org.springframework.ide.eclipse.boot.util.Log;
import org.springsource.ide.eclipse.commons.frameworks.core.downloadmanager.URLConnectionFactory;
import org.springsource.ide.eclipse.commons.frameworks.core.util.IOUtil;

public interface InitializrService {

	public static final InitializrService DEFAULT = create(BootActivator.getUrlConnectionFactory(), BootPreferences::getDefaultInitializrUrl);

	SpringBootStarters getStarters(String bootVersion) throws Exception;

	/**
	 * Generates a pom by contacting intializer service.
	 */
	String getPom(String bootVersion, List<String> starters) throws Exception;

	static InitializrService create(URLConnectionFactory urlConnectionFactory, Supplier<String> baseUrl) {
		return new InitializrService() {
			@Override
			public SpringBootStarters getStarters(String bootVersion) throws Exception {
				URL initializrUrl = new URL(baseUrl.get());
				URL dependencyUrl = dependencyUrl(bootVersion, initializrUrl);
				return SpringBootStarters.load(
						initializrUrl, dependencyUrl,
						BootActivator.getUrlConnectionFactory()
				);
			}

			private URL dependencyUrl(String bootVersion, URL initializerUrl) throws MalformedURLException {
				SimpleUriBuilder builder = new SimpleUriBuilder(initializerUrl.toString()+"/dependencies");
				builder.addParameter("bootVersion", bootVersion);
				return new URL(builder.toString());
			}

			@Override
			public String getPom(String bootVersion, List<String> starters) throws Exception {
				//EXample uri:
				//https://start-development.cfapps.io/starter.zip
				//	?name=demo&groupId=com.example&artifactId=demo
				//  &version=0.0.1-SNAPSHOT
				//  &description=Demo+project+for+Spring+Boot&packageName=com.example.demo
				//  &type=maven-project
				//  &packaging=jar
				//  &javaVersion=1.8
				//  &language=java
				//  &bootVersion=2.0.3.RELEASE
				//  &dependencies=cloud-aws&dependencies=cloud-hystrix-dashboard&dependencies=web

				SimpleUriBuilder builder = new SimpleUriBuilder(baseUrl.get()+"/pom.xml");
				builder.addParameter("bootVersion", bootVersion);
				for (String starter : starters) {
					builder.addParameter("dependencies", starter);
				}
				URLConnection urlConnection = urlConnectionFactory.createConnection(new URL(builder.toString()));
				urlConnection.connect();
				return IOUtil.toString(urlConnection.getInputStream(), "UTF8");
			}
		};
	}

}
