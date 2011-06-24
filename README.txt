README for "Semantifier"


PREREQUISITES for Developing

You need AT LEAST Grails 1.4.0M2 (not released at this point,
so install grails from source according to http://grails.org/Installation)
-> Because of issue http://jira.grails.org/browse/GRAILS-7595

INSTALLATION
1) Place a custom config file at location
   ${userHome}/.grails/semantifier-config.groovy
   with the following contents, inserting your API keys there:

// START CONFIGURATION
ner {
	OpenCalais.apiKey="your-api-key-here"
	Alchemy.apiKey="your-api-key-here"
}
// END CONFIGURATION