package semantifier

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

import groovyx.net.http.RESTClient
import net.sf.json.JSONNull
import static groovyx.net.http.ContentType.JSON
import groovyx.gpars.GParsPool

class AnnotationService {

	def openCalaisConnector
	def alchemyConnector
	def languageClassifier

	public def annotate(String text) {
		def language = languageClassifier.classify(text);
		def rawAnnotations = annotateText(text, language)

		return [language: language, entities: processRawAnnotations(rawAnnotations)];
	}

	private def annotateText(def text, def language) {
		def annotations = []
		if (language == "en") {
			annotations = openCalaisConnector.getAnnotations(text);
		} else {
			annotations = alchemyConnector.getAnnotations(text);
		}
		return annotations
	}

	private def processRawAnnotations(def rawAnnotations) {
		def processedAnnotations = []

		def numberOfThreads = rawAnnotations.size()
		if (numberOfThreads > 10) numberOfThreads = 10
		
		GParsPool.withPool(numberOfThreads) {
			processedAnnotations = rawAnnotations.collectParallel { annotation ->
				def freebaseClient = new RESTClient('http://api.freebase.com/api/service/search')
				def response = freebaseClient.get(query: [query: annotation.entity, limit:5, stemmed:1], contentType: JSON)
	
				return [
					entity: annotation.entity,
					offset: annotation.offset,
					length: annotation.length,
					tag: annotation.mostLikelyTagName, // TODO: work with this one later, to refine freebase results.
					disambiguration: processPossibleFreebaseResults(response.data.result) // TODO: convert to linked data results
				]
			}
		}

		return processedAnnotations;
	}
	private def processPossibleFreebaseResults(results) {
		def possibleResults = [];

			// No results found, so we return the empty result list.
		if (!results) return [];

		results.each { result ->
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

			possibleResults << possibleSingleResult
		}

		return possibleResults
	}
}