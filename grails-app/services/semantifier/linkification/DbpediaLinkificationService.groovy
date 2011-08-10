package semantifier.linkification

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
 * Linkification service which uses Freebase.
 */
class DbpediaLinkificationService extends AbstractLinkifier {
	public String getName() {
		return "dbpedia"
	}
	public def linkify(Annotation annotation) {
		def freebaseClient = new RESTClient('http://lookup.dbpedia.org/api/search.asmx/PrefixSearch') // TODO: PrefixSearch -> KeywordSearch
		def query = [QueryString: annotation.entity, MaxHits:5]
		
		if (annotation.mostLikelyTagName && config.tagMapping[annotation.mostLikelyTagName]) {
			query['QueryClass'] = config.tagMapping[annotation.mostLikelyTagName]
		}
		
		def response = freebaseClient.get(query: query, contentType: XML)
		return processPossibleDbpediaResults(response.data.children())
	}
	
	private def processPossibleDbpediaResults(results) {
		return results.collect { result ->
			return [
				id: result.URI.text(),
				type: result.Classes.Class[0].URI.text(),
				name:  result.Label.text(),
				description: result.Description.text()
			]
		}
	}
}
