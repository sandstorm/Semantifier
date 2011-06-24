/**
 * This file is part of "Semantifier".
 *
 * Copyright 2011 Sebastian Kurfürst
 *
 * Semantifier is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Semantifier is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Semantifier.  If not, see <http://www.gnu.org/licenses/>.
 */
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
		mavenCentral()
        mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
		mavenRepo 'http://repository.jboss.org/maven2/'
	}
    dependencies {
		// JENA
		compile('com.hp.hpl.jena:jena:2.6.4') {
			excludes 'slf4j-log4j12', 'log4j', 'slf4j-api'
		}


		// Palladian
		compile('ws.palladian:palladian:0.8-SNAPSHOT') {
			 excludes "log4j", "stax", "jaxen", "weka", "lucene-core", "lucene-memory", "lucene-analyzers", "rome", "lingpipe", "junit", "xml-apis", "xercesImpl", "trove", "twitter4j-core", "prefuse", "bonecp", "snakeyaml", "jwbf", "boilerpipe", "nekohtml", "mysql-connector-java", "commons-validator", "h2", "commons-configuration", "slf4j-api"
		}
		compile('commons-configuration:commons-configuration:1.6')
		
		// JSON-LIB
		// compile 'net.sf.json-lib:json-lib:2.4'
		// However, JSON library is included in lib/ directory
		// because http://jira.grails.org/browse/GRAILS-6147
		// the following dependencies are needed for json-lib:
		runtime('commons-beanutils:commons-beanutils:1.8.0')
		runtime('commons-collections:commons-collections:3.2.1')
		runtime('net.sf.ezmorph:ezmorph:1.0.6')
		runtime('xml-resolver:xml-resolver:1.2')

		// HTTP-Builder
		compile( 'org.codehaus.groovy.modules.http-builder:http-builder:0.5.1') {
			transitive = false
		}

		// GPars
		build 'org.codehaus.gpars:gpars:0.12'
    }

    plugins {
        build ":tomcat:$grailsVersion"
    }
}