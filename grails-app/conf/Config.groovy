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
// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

grails.config.locations = ["file:${userHome}/.grails/${appName}-config.groovy"]
if(System.properties["${appName}.config.location"]) {
	grails.config.locations << "file:" + System.properties["${appName}.config.location"]
}

//"classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://www.changeme.com"
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }

}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
    debug  'ws'
    //debug 'org', 'com', 'net', 'ws'
}

// Custom Configuration
ner {
	OpenCalais.apiKey="" // your-api-key-here
	Alchemy.apiKey="" // your-api-key-here
	
	annotation {
		languageToNerServiceMapping {
			de = "Alchemy"
			en = "OpenCalais"
		}
	}
	disambiguration {
		disambigruationOrder = "freebase"
		freebase {
			tagMapping {
				// MAPPING from OpenCalais to Freebase terms
//				Anniversary = ""
				City = "/location/citytown"
				Company = "/business/business_corporation"
				Continent = "/location/continent"
				Country = "/location/country"
//				Currency = ""
//				EmailAddress = ""
//				EntertainmentAwardEvent = ""
//				Facility = ""
//				FaxNumber = ""
				Holiday = "/time/holiday"
//				IndustryTerm = ""
				MarketIndex = "/finance/market_index"
				MedicalCondition = "/medicine/disease"
				MedicalTreatment = "/medicine/medical_treatment"
				Movie = "/film/film"
				MusicAlbum = "/music/album"
				MusicGroup = "/music/artist"
//				NaturalFeature = ""
				OperatingSystem = "/computer/operating_system"
				Organization = "/organization/organization"
				Person = "/people/person"
				PhoneNumber = "/common/phone_number"
//				PoliticalEvent = ""
//				Position = ""
				Product = "/business/consumer_product"
				ProgrammingLanguage = "/computer/programming_language"
				ProvinceOrState = "/location/administrative_division"
//				PublishedMedium = ""
				RadioProgram = "/radio/radio_program"
				RadioStation = "/broadcast/radio_station"
				Region = "/location/region"
//				SportsEvent = ""
//				SportsGame = ""
//				SportsLeague = ""
//				Technology = ""
//				TVShow = ""
//				TVStation = ""
//				URL = ""
			}
		}
	}
}