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
 * Disambiguration service using sindice. Mostly useful for people who are not on Freebase, but on the semantic web.
 */
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
				name: result.title[0]
			]
			return possibleSingleResult
		}
	}
}
