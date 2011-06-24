package semantifier.disambigurator

import ws.palladian.extraction.entity.ner.Annotation
import groovyx.net.http.RESTClient
import net.sf.json.JSONNull
import static groovyx.net.http.ContentType.JSON


class SindiceDisambiguratorService extends AbstractDisambigurator {
	//def grailsApplication
	
	public def disambigurate(Annotation annotation) {
		def sindiceClient = new RESTClient('http://api.sindice.com/v2/search')
		
		// TODO: evaluate type, i.e. if type is "Person", append foaf:Person to the search query
		// TODO: Figure out the actual linked data URI INSTEAD OF document URI (i.e. http://sebastian.kurfuerst.eu/ instead of http://sebastian.kurfuerst.eu/index.rdf)
		def query = [q: annotation.entity, page: 1, qt: 'term']
		
		def response = sindiceClient.get(query: query, contentType: JSON)
		return processPossibleSindiceResults(response.data.entries)
	}
	
	private def processPossibleSindiceResults(results) {
			// No results found, so we return null
		if (!results) return null;

		return results.collect { result ->
			def possibleSingleResult = [
				id: result.link,
				name: result.title
			]
			return possibleSingleResult
		}
	}
}
