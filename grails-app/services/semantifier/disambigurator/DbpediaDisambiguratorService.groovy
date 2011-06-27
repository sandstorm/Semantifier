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
import static groovyx.net.http.ContentType.XML


/**
 * Disambiguration service which uses Freebase.
 */
class DbpediaDisambiguratorService extends AbstractDisambigurator {
	def grailsApplication
	
	public def disambigurate(Annotation annotation) {
		def freebaseClient = new RESTClient('http://lookup.dbpedia.org/api/search.asmx/PrefixSearch') // TODO: PrefixSearch -> KeywordSearch
		def query = [QueryString: annotation.entity, MaxHits:5]
		
		if (annotation.mostLikelyTagName && grailsApplication.config.ner.disambiguration.dbpedia.tagMapping[annotation.mostLikelyTagName]) {
			query['QueryClass'] = grailsApplication.config.ner.disambiguration.dbpedia.tagMapping[annotation.mostLikelyTagName]
		}
		
		def response = freebaseClient.get(query: query, contentType: XML)
		return processPossibleDbpediaResults(response.data.children())
	}
	
	private def processPossibleDbpediaResults(results) {
			// No results found, so we return null
		if (!results.size()) return null;

		return results.collect { result ->
			def possibleSingleResult = [
				id: result.URI.text(),
				name:  result.Label.text()
			]

			return possibleSingleResult
		}
	}
}
