README for "Semantifier"


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