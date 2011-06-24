package semantifier

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

import com.hp.hpl.jena.ontology.*
import com.hp.hpl.jena.rdf.model.ModelFactory


/**
 * Do some type inference checking
 */
class TypeCheckerService {
	
	/**
	 * TODO: Wrong behavior at http://xmlns.com/foaf/0.1/givenName for example (is a literal, but does not say so... we need some fallback there maybe?)
	 *
	 * @param uri
	 * @return TRUE if result is a literal type, FALSE otherwise.
	 */
	public boolean isUriALiteralType(String uri) {
		OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null )

		def literal = model.createResource('http://www.w3.org/2000/01/rdf-schema#Literal')

		model.read(uri)
		OntProperty property = model.getOntProperty(uri);

		if (property == null) {
			throw new Exception('Type ' + uri + ' not found.');
		}
		return (property?.range == literal);
	}
}