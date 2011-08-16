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
import static groovyx.net.http.ContentType.JSON
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory

/**
 * Linkification service using sindice. Mostly useful for people who are not on Freebase, but on the semantic web.
 */
class SindiceLinkificationService extends AbstractLinkifier {
	public String getName() {
		return "sindice"
	}
	public def linkify(Annotation annotation) {
		def sindiceClient = new RESTClient('http://api.sindice.com/v2/search')
		sindiceClient.client.params.setParameter('http.socket.timeout', new Integer(1500));
		sindiceClient.handler.failure = {}

		def queryString = annotation.entity
		def rdfType = null

		if (annotation.mostLikelyTagName && config.tagMapping[annotation.mostLikelyTagName]) {
			rdfType = config.tagMapping[annotation.mostLikelyTagName]
			queryString += ' ' + rdfType
		}

		def query = [q: queryString, page: 1, qt: 'term']

		def response = sindiceClient.get(query: query, contentType: JSON)
		return processPossibleSindiceResults(response.data.entries, rdfType)
	}

	private def processPossibleSindiceResults(results, rdfType) {
		return results.collect { result ->
			def entityUrl = getEntityUrlForDocument(result.link, rdfType);
			if (!entityUrl) return null;
			return [
				id: entityUrl,
				type: rdfType,
				name: result.title[0],
			]
		}.findAll { it } // only retrieve all non-null results
	}

	public def getEntityUrlForDocument(String documentUrl, String rdfType) {
		return documentUrl; // HACK because it takes too long!
		
		// Graceful fallback if rdfType is not given.
		if (!rdfType) return documentUrl;

		def sindiceLiveClient = new RESTClient('http://api.sindice.com/v3/cache')
		sindiceLiveClient.client.params.setParameter('http.socket.timeout', new Integer(1500));
		sindiceLiveClient.handler.failure = {}
		def query = [url: documentUrl]
		def response = sindiceLiveClient.get(query: query, contentType: JSON)

		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		response.data[documentUrl].explicit_content.each { tripleString ->
			model.read(new StringBufferInputStream(tripleString), null, "N-TRIPLE")
		}

		def firstStatement = model.listStatements(null, model.createProperty('http://www.w3.org/1999/02/22-rdf-syntax-ns#', 'type'), model.createResource(rdfType)).next()
		return firstStatement.subject.getURI()
	}
}
