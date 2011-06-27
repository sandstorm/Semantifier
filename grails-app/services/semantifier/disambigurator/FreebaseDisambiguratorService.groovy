package semantifier.disambigurator

/*
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

import ws.palladian.extraction.entity.ner.Annotation
import groovyx.net.http.RESTClient
import net.sf.json.JSONNull
import static groovyx.net.http.ContentType.JSON


/**
 * Disambiguration service which uses Freebase.
 */
class FreebaseDisambiguratorService extends AbstractDisambigurator {
	def grailsApplication
	
	public def disambigurate(Annotation annotation) {
		def freebaseClient = new RESTClient('http://api.freebase.com/api/service/search')
		def query = [query: annotation.entity, limit:5, stemmed:1]
		
		if (annotation.mostLikelyTagName && grailsApplication.config.ner.disambiguration.freebase.tagMapping[annotation.mostLikelyTagName]) {
			query['type'] = grailsApplication.config.ner.disambiguration.freebase.tagMapping[annotation.mostLikelyTagName]
			// TODO: decide whether query type should be accounted for in all cases or not...
			// query['type_strict'] = 'should' // give preference to matching types, but do still include other types (uses type only for boosting)
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
