package semantifier

import com.hp.hpl.jena.ontology.*
import com.hp.hpl.jena.rdf.model.ModelFactory

class TypeCheckerService {

    // TODO: Wrong behavior at http://xmlns.com/foaf/0.1/givenName for example (is a literal, but does not say so... we need some fallback there maybe?)
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
