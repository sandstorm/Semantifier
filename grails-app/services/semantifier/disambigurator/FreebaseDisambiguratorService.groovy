package semantifier.disambigurator

import ws.palladian.extraction.entity.ner.Annotation
import groovyx.net.http.RESTClient
import net.sf.json.JSONNull
import static groovyx.net.http.ContentType.JSON


class FreebaseDisambiguratorService extends AbstractDisambigurator {
	def grailsApplication
	
	public def disambigurate(Annotation annotation) {
		def freebaseClient = new RESTClient('http://api.freebase.com/api/service/search')
		def query = [query: annotation.entity, limit:5, stemmed:1]
		
		if (annotation.mostLikelyTagName && grailsApplication.config.ner.disambiguration.freebase.tagMapping[annotation.mostLikelyTagName]) {
			query['type'] = grailsApplication.config.ner.disambiguration.freebase.tagMapping[annotation.mostLikelyTagName]
			// TODO: decide whether query type should be accounted for in all cases or not...
			// query['type_strict'] = 'should' // give preference to matching types, but do not include other types.
		}
		
		def response = freebaseClient.get(query: query, contentType: JSON)
		return processPossibleFreebaseResults(response.data.result)
	}
	
	private def processPossibleFreebaseResults(results) {
			// No results found, so we return null
		if (!results) return null;

		return results.collect { result ->
			if (!result) return;
			if (result.class == JSONNull.class) return;

			def possibleSingleResult = [
				id: 'http://rdf.freebase.com/ns' + result.id,
				name: result.name,
				relevanceScore: result['relevance:score']
			]

			if (result.image && result.image.class != JSONNull.class) {
				possibleSingleResult.imageThumbnailUri = 'http://api.freebase.com/api/trans/image_thumb' + result.image.id
			}

			return possibleSingleResult
		}
	}
}
